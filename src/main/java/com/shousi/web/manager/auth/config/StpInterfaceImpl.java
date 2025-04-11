package com.shousi.web.manager.auth.config;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.json.JSONUtil;
import com.shousi.web.constant.PictureConstant;
import com.shousi.web.context.SpaceUserAuthContext;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.manager.auth.SpaceUserAuthManager;
import com.shousi.web.manager.auth.StpKit;
import com.shousi.web.model.entity.Picture;
import com.shousi.web.model.entity.Space;
import com.shousi.web.model.entity.SpaceUser;
import com.shousi.web.model.entity.User;
import com.shousi.web.model.eums.SpaceRoleEnum;
import com.shousi.web.model.eums.SpaceTypeEnum;
import com.shousi.web.service.PictureService;
import com.shousi.web.service.SpaceService;
import com.shousi.web.service.SpaceUserService;
import com.shousi.web.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.shousi.web.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 自定义获取权限
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Resource
    private SpaceUserAuthManager spaceUserManager;
    
    @Resource
    private SpaceUserService spaceUserService;

    @Resource
    private PictureService pictureService;

    @Resource
    private UserService userService;

    @Resource
    private SpaceService spaceService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 如果登录类型不为空间，则返回空集合
        if (!StpKit.SPACE_TYPE.equals(loginType)) {
            return new ArrayList<>();
        }
        // 管理员权限，表示权限通过
        List<String> ADMIN_PERMISSIONS = spaceUserManager.getPermissionListByRole(SpaceRoleEnum.ADMIN.getValue());
        // 上下文数据
        SpaceUserAuthContext authContextByRequest = getAuthContextByRequest();
        // 如果全为空，则说明查公共图库
        if (isAllFieldsNull(authContextByRequest)) {
            return ADMIN_PERMISSIONS;
        }
        SaSession spaceSession = StpKit.SPACE.getSessionByLoginId(loginId);
        User loginUser = (User) spaceSession.get(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }
        Long userId = loginUser.getId();
        // 优先获取spaceUser对象
        SpaceUser spaceUser = authContextByRequest.getSpaceUser();
        // 如果spaceUser不为空，说明是在团队图库空间中，直接获取指定用户分配的角色的权限
        if (spaceUser != null) {
            return spaceUserManager.getPermissionListByRole(spaceUser.getSpaceRole());
        }
        Long spaceUserId = authContextByRequest.getSpaceUserId();
        if (spaceUserId != null) {
            spaceUser = spaceUserService.getById(spaceUserId);
            ThrowUtils.throwIf(spaceUser == null, ErrorCode.NOT_FOUND_ERROR, "未查询到空间中对应的用户信息");
            // 取出当前登录用户对应的 spaceUser
            SpaceUser loginSpaceUser = spaceUserService.lambdaQuery()
                    .eq(SpaceUser::getSpaceId, authContextByRequest.getSpaceId())
                    .eq(SpaceUser::getUserId, userId)
                    .one();
            if (loginSpaceUser == null) {
                return new ArrayList<>();
            }
            return spaceUserManager.getPermissionListByRole(loginSpaceUser.getSpaceRole());
        }
        // 如果没有spaceUserId，尝试通过 spaceId 或 pictureId 获取space对象
        Long spaceId = authContextByRequest.getSpaceId();
        if (spaceId == null) {
            // 没有spaceId，通过pictureId查询
            Long pictureId = authContextByRequest.getPictureId();
            // 如果连pictureId都没有，则默认通过权限校验
            if (pictureId == null) {
                return ADMIN_PERMISSIONS;
            }
            Picture picture = pictureService.lambdaQuery()
                    .eq(Picture::getId, pictureId)
                    .select(Picture::getId, Picture::getSpaceId, Picture::getUserId)
                    .one();
            if (picture == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未查询到图片信息");
            }
            spaceId = picture.getSpaceId();
            if (spaceId == null) {
                // 公共图库，仅本人和管理员可以操作
                if (userService.isAdmin(loginUser) || userId.equals(picture.getUserId())) {
                    return ADMIN_PERMISSIONS;
                }else {
                    // 不是自己的图片，仅可查看
                    return spaceUserManager.getPermissionListByRole(SpaceRoleEnum.VIEWER.getValue());
                }
            }
        }
        // spaceId存在
        Space space = spaceService.getById(spaceId);
        ThrowUtils.throwIf(space == null, ErrorCode.NOT_FOUND_ERROR, "空间不存在");
        // 判断空间类型
        if (space.getSpaceType() == SpaceTypeEnum.PRIVATE.getValue()) {
            // 私有空间
            if (userService.isAdmin(loginUser) || userId.equals(space.getUserId())) {
                return ADMIN_PERMISSIONS;
            }else {
                // 没有权限
                return new ArrayList<>();
            }
        }else {
            // 团队空间
            spaceUser = spaceUserService.lambdaQuery()
                    .eq(SpaceUser::getSpaceId, spaceId)
                    .eq(SpaceUser::getUserId, userId)
                    .one();
            if (spaceUser == null) {
                // 不存在
                return new ArrayList<>();
            }
            return spaceUserManager.getPermissionListByRole(spaceUser.getSpaceRole());
        }
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        return null;
    }

    /**
     * 从请求中获取上下文对象
     */
    private SpaceUserAuthContext getAuthContextByRequest() {
        // 获取上下文请求
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String contentType = request.getHeader(Header.CONTENT_TYPE.getValue());
        SpaceUserAuthContext authContext;
        // 兼容get和post请求
        if (ContentType.JSON.getValue().equals(contentType)) {
            String body = ServletUtil.getBody(request);
            authContext = JSONUtil.toBean(body, SpaceUserAuthContext.class);
        } else {
            Map<String, String> paramMap = ServletUtil.getParamMap(request);
            authContext = BeanUtil.toBean(paramMap, SpaceUserAuthContext.class);
        }
        // 根据请求路径区分id字段含义
        Long id = authContext.getId();
        if (ObjectUtil.isNotNull(id)) {
            String requestURI = request.getRequestURI();
            // 去掉api/  api/space/getVo => space/getVo
            String partUri = requestURI.replace(contextPath + "/", "");
            // space/getVo => space
            String moduleName = StrUtil.subBefore(partUri, "/", false);
            switch (moduleName) {
                case "picture":
                    authContext.setPictureId(id);
                    break;
                case "space":
                    authContext.setSpaceId(id);
                    break;
                case "spaceUser":
                    authContext.setSpaceUserId(id);
                    break;
                default:
                    break;
            }
        }
        return authContext;
    }

    private boolean isAllFieldsNull(Object object) {
        if (object == null) {
            return true; // 对象本身为空
        }
        // 获取所有字段并判断是否所有字段都为空
        return Arrays.stream(ReflectUtil.getFields(object.getClass()))
                // 获取字段值
                .map(field -> ReflectUtil.getFieldValue(object, field))
                // 检查是否所有字段都为空
                .allMatch(ObjectUtil::isEmpty);
    }
}
