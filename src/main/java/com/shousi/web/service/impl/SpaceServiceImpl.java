package com.shousi.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.mapper.SpaceMapper;
import com.shousi.web.model.dto.space.SpaceAddRequest;
import com.shousi.web.model.dto.space.SpaceQueryRequest;
import com.shousi.web.model.entity.Space;
import com.shousi.web.model.entity.SpaceUser;
import com.shousi.web.model.entity.User;
import com.shousi.web.model.eums.SpaceLevelEnum;
import com.shousi.web.model.eums.SpaceRoleEnum;
import com.shousi.web.model.eums.SpaceTypeEnum;
import com.shousi.web.model.eums.UserRoleEnum;
import com.shousi.web.model.vo.SpaceVO;
import com.shousi.web.model.vo.UserVO;
import com.shousi.web.service.SpaceService;
import com.shousi.web.service.SpaceUserService;
import com.shousi.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space>
        implements SpaceService {

    @Resource
    private UserService userService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private SpaceUserService spaceUserService;

    @Override
    public long addSpace(SpaceAddRequest spaceAddRequest, User loginUser) {
        ThrowUtils.throwIf(spaceAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 1.填充参数默认值
        if (StrUtil.isBlank(spaceAddRequest.getSpaceName())) {
            spaceAddRequest.setSpaceName("默认空间");
        }
        if (spaceAddRequest.getSpaceLevel() == null) {
            spaceAddRequest.setSpaceLevel(SpaceLevelEnum.COMMON.getValue());
        }
        if (spaceAddRequest.getSpaceType() == null) {
            spaceAddRequest.setSpaceType(SpaceTypeEnum.PRIVATE.getValue());
        }
        Space space = new Space();
        BeanUtils.copyProperties(spaceAddRequest, space);
        this.fillSpaceBySpaceLevel(space);
        // 2.校验参数
        validSpace(space, true);
        // 3.校验权限，非管理员只能创建普通级别的空间
        if (!userService.isAdmin(loginUser) && space.getSpaceLevel() != SpaceLevelEnum.COMMON.getValue()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限创建指定级别的空间");
        }
        // 4.控制同一用户只能创建一个私有空间
        Long userId = loginUser.getId();
        space.setUserId(userId);
        // 每个用户上一把锁
        String lock = String.valueOf(userId).intern();
        synchronized (lock) {
            // 在该部分逻辑添加事务，防止因锁释放但事务没有提交导致重复创建私有空间
            Long newSpaceId = transactionTemplate.execute(status -> {
                boolean exists = this.lambdaQuery()
                        .eq(Space::getUserId, userId)
                        .eq(Space::getSpaceType, space.getSpaceType())
                        .exists();
                ThrowUtils.throwIf(exists, ErrorCode.OPERATION_ERROR, "每类空间你能创建一个");
                boolean save = this.save(space);
                ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "创建私有空间失败，请联系工作人员");
                // 如果是团队空间，关联新增团队成员记录
                if (SpaceTypeEnum.TEAM.getValue() == space.getSpaceType()) {
                    SpaceUser spaceUser = new SpaceUser();
                    spaceUser.setSpaceId(space.getId());
                    spaceUser.setUserId(userId);
                    spaceUser.setSpaceRole(SpaceRoleEnum.ADMIN.getValue());
                    spaceUser.setCreateUserId(userId);
                    save = spaceUserService.save(spaceUser);
                    ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "创建团队空间失败，请联系工作人员");
                }
                // 返回新创建空间的id
                return space.getId();
            });
            return Optional.ofNullable(newSpaceId).orElse(-1L);
        }
    }

    @Override
    public void validSpace(Space space, boolean add) {
        ThrowUtils.throwIf(space == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String spaceName = space.getSpaceName();
        Integer spaceLevel = space.getSpaceLevel();
        SpaceLevelEnum spaceLevelEnum = SpaceLevelEnum.getEnumByValue(spaceLevel);
        Integer spaceType = space.getSpaceType();
        SpaceTypeEnum spaceTypeEnum = SpaceTypeEnum.getEnumByValue(spaceType);
        // 创建时校验
        if (add) {
            if (StrUtil.isBlank(spaceName)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间名称不能为空");
            }
            if (spaceLevel == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间级别不能为空");
            }
            if (spaceType == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间类型不能为空");
            }
        }
        // 修改数据时，空间名称进行校验
        if (StrUtil.isNotBlank(spaceName) && spaceName.length() > 30) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间名称过长");
        }
        // 修改数据时，空间级别进行校验
        if (spaceLevel != null && spaceLevelEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间级别不存在");
        }
        // 修改数据时，空间类型进行校验
        if (spaceType != null && spaceTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间类型不存在");
        }
    }

    @Override
    public SpaceVO getSpaceVO(Space space, HttpServletRequest request) {
        // 对象转封装类
        SpaceVO spaceVO = SpaceVO.objToVo(space);
        // 关联查询用户信息
        Long userId = space.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            spaceVO.setUser(userVO);
        }
        return spaceVO;
    }

    @Override
    public Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request) {
        List<Space> spaceList = spacePage.getRecords();
        Page<SpaceVO> spaceVOPage = new Page<>(spacePage.getCurrent(), spacePage.getSize(), spacePage.getTotal());
        if (CollUtil.isEmpty(spaceList)) {
            return spaceVOPage;
        }
        // 对象列表 => 封装对象列表
        List<SpaceVO> spaceVOList = spaceList.stream()
                .map(SpaceVO::objToVo)
                .collect(Collectors.toList());
        // 1. 关联查询用户信息
        // 1,2,3,4
        Set<Long> userIdSet = spaceList.stream().map(Space::getUserId).collect(Collectors.toSet());
        // 1 => user1, 2 => user2
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 填充信息
        spaceVOList.forEach(spaceVO -> {
            Long userId = spaceVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            spaceVO.setUser(userService.getUserVO(user));
        });
        spaceVOPage.setRecords(spaceVOList);
        return spaceVOPage;
    }

    @Override
    public QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest) {
        QueryWrapper<Space> queryWrapper = new QueryWrapper<>();
        if (spaceQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = spaceQueryRequest.getId();
        Long userId = spaceQueryRequest.getUserId();
        String spaceName = spaceQueryRequest.getSpaceName();
        Integer spaceLevel = spaceQueryRequest.getSpaceLevel();
        String sortField = spaceQueryRequest.getSortField();
        String sortOrder = spaceQueryRequest.getSortOrder();
        Integer spaceType = spaceQueryRequest.getSpaceType();
        // 拼接查询条件
        queryWrapper.eq(ObjUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjUtil.isNotEmpty(spaceLevel), "spaceLevel", spaceLevel);
        queryWrapper.eq(ObjUtil.isNotEmpty(spaceType), "spaceType", spaceType);
        queryWrapper.like(StrUtil.isNotBlank(spaceName), "spaceName", spaceName);
        // 排序
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public void fillSpaceBySpaceLevel(Space space) {
        // 根据空间级别，自动填充限额
        SpaceLevelEnum spaceLevelEnum = SpaceLevelEnum.getEnumByValue(space.getSpaceLevel());
        long maxSize = spaceLevelEnum.getMaxSize();
        // 管理员设置的空间属性优先（更加灵活）
        if (space.getMaxSize() == null) {
            space.setMaxSize(maxSize);
        }
        long maxCount = spaceLevelEnum.getMaxCount();
        if (space.getMaxCount() == null) {
            space.setMaxCount(maxCount);
        }
    }

    @Override
    public void checkSpaceAuth(User loginUser, Space space) {
        if (!space.getUserId().equals(loginUser.getId()) && !loginUser.getUserRole().equals(UserRoleEnum.ADMIN.getValue())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "没有空间权限");
        }
    }
}

