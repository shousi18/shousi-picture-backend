package com.shousi.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.api.aliyunai.AliYunAiApi;
import com.shousi.web.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.shousi.web.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.manager.CosManager;
import com.shousi.web.manager.upload.FilePictureUpload;
import com.shousi.web.manager.upload.PictureUploadTemplate;
import com.shousi.web.manager.upload.UrlPictureUpload;
import com.shousi.web.mapper.PictureMapper;
import com.shousi.web.model.dto.file.UploadPictureResult;
import com.shousi.web.model.dto.picture.*;
import com.shousi.web.model.entity.*;
import com.shousi.web.model.eums.PictureReviewStatusEnum;
import com.shousi.web.model.eums.SpaceTypeEnum;
import com.shousi.web.model.eums.UserRoleEnum;
import com.shousi.web.model.vo.PictureVO;
import com.shousi.web.model.vo.TagVO;
import com.shousi.web.model.vo.UserVO;
import com.shousi.web.service.*;
import com.shousi.web.utils.ColorSimilarUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.shousi.web.constant.PictureConstant.PICTURE_EDIT_HISTORY_KEY;


/**
 * @author 86172
 * @description 针对表【picture(图片)】的数据库操作Service实现
 * @createDate 2025-03-24 10:18:52
 */
@Service
@Slf4j
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
        implements PictureService {

    @Resource
    private UserService userService;

    @Resource
    private PictureCategoryService pictureCategoryService;

    @Resource
    private PictureTagService pictureTagService;

    @Resource
    private TagService tagService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private FilePictureUpload filePictureUpload;

    @Resource
    private UrlPictureUpload urlPictureUpload;

    @Resource
    private CosManager cosManager;

    @Resource
    private SpaceService spaceService;

    @Resource
    private AliYunAiApi aliYunAiApi;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest, User loginUser) {
        // 校验参数
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        // 校验空间是否存在
        Long spaceId = pictureUploadRequest.getSpaceId();
        if (spaceId != null) {
            Space space = spaceService.getById(spaceId);
            ThrowUtils.throwIf(space == null, ErrorCode.NOT_FOUND_ERROR, "空间不存在");
            if (space.getTotalSize() > space.getMaxSize()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间大小不足");
            }
            if (space.getTotalCount() > space.getMaxCount()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间条数不足");
            }
        }
        // 判断是新增还是更新，如果是更新则使用原本的图片id
        Long pictureId = null;
        if (pictureUploadRequest.getId() != null) {
            pictureId = pictureUploadRequest.getId();
        }
        // 如果是更新，判断图片是否存在
        Picture oldPicture = new Picture();
        if (pictureId != null) {
            oldPicture = this.getById(pictureId);
            ThrowUtils.throwIf(oldPicture == null, ErrorCode.NOT_FOUND_ERROR, "图片不存在");
            // 校验空间是否一致
            if (spaceId == null) {
                // 传图片的时候没有传空间id，则复用原本的空间id
                if (oldPicture.getSpaceId() != null) {
                    spaceId = oldPicture.getSpaceId();
                }
            } else {
                // 如果传了空间id，则判断是不是同一个空间id
                if (!oldPicture.getSpaceId().equals(spaceId)) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间不一致");
                }
            }
        }
        // 上传图片，得到图片信息
        // 根据用户id分组 => 按照空间划分目录
        String uploadPathPrefix;
        if (spaceId == null) {
            // 公共图库
            uploadPathPrefix = String.format("/public/%s", loginUser.getId());
        } else {
            // 空间图库
            uploadPathPrefix = String.format("space/%s", spaceId);
        }
        PictureUploadTemplate pictureUploadTemplate = filePictureUpload;
        if (inputSource instanceof String) {
            pictureUploadTemplate = urlPictureUpload;
        }
        UploadPictureResult uploadPictureResult = pictureUploadTemplate.uploadPicture(inputSource, uploadPathPrefix);
        // 如果是更新操作，并且新图片上传成功，则清除原本的云端文件
        if (pictureId != null) {
            clearPictureFile(oldPicture);
        }
        // 构造要入库的图片信息
        Picture picture = new Picture();
        picture.setUrl(uploadPictureResult.getUrl());
        picture.setOriginUrl(uploadPictureResult.getOriginUrl());
        picture.setThumbnailUrl(uploadPictureResult.getThumbnailUrl());
        picture.setSpaceId(spaceId);
        String picName = uploadPictureResult.getPicName();
        if (pictureUploadRequest != null && StrUtil.isNotBlank(pictureUploadRequest.getPicName())) {
            picName = pictureUploadRequest.getPicName();
        }
        picture.setName(picName);
        picture.setPicSize(uploadPictureResult.getPicSize());
        picture.setPicWidth(uploadPictureResult.getPicWidth());
        picture.setPicHeight(uploadPictureResult.getPicHeight());
        picture.setPicScale(uploadPictureResult.getPicScale());
        picture.setPicFormat(uploadPictureResult.getPicFormat());
        picture.setPicColor(uploadPictureResult.getPicColor());
        picture.setUserId(loginUser.getId());
        // 填充审核参数
        fillReviewParams(picture, loginUser);
        // 操作数据库 判断是保存还是更新
        if (pictureId != null) {
            // 更新图片信息
            picture.setId(pictureId);
            picture.setEditTime(new Date());
            boolean result = this.updateById(picture);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "上传图片操作失败");
            // 更新空间的额度，更新图片只需要计算占用空间大小差值
            if (spaceId != null) {
                boolean update = spaceService.lambdaUpdate()
                        .eq(Space::getId, spaceId)
                        .setSql("totalSize = totalSize + " + (picture.getPicSize() - oldPicture.getPicSize()))
                        .update();
                ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "更新空间信息失败");
                Space space = spaceService.getById(spaceId);
                if (space.getSpaceType() == SpaceTypeEnum.TEAM.getValue()) {
                    // 如果是公共图库，更新后redis保存的之前的历史操作。
                    String historyKey = PICTURE_EDIT_HISTORY_KEY + pictureId;
                    stringRedisTemplate.delete(historyKey);
                }
            }
            return convertToVO(picture);
        }
        // 保存图片信息
        boolean isSuccess = this.save(picture);
        ThrowUtils.throwIf(!isSuccess, ErrorCode.OPERATION_ERROR, "上传图片操作失败");
        // 更新空间的额度，需要计算占用空间大小并且数量加一
        if (spaceId != null) {
            boolean update = spaceService.lambdaUpdate()
                    .eq(Space::getId, spaceId)
                    .setSql("totalSize = totalSize + " + picture.getPicSize())
                    .setSql("totalCount = totalCount + 1")
                    .update();
            ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "更新空间信息失败");
        }
        // 需要设置默认分类和标签
        Long savedPictureId = picture.getId();
        setDefaultTagCategory(savedPictureId);
        return this.convertToVO(picture);
    }

    @Override
    public QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest) {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        if (pictureQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = pictureQueryRequest.getId();
        String name = pictureQueryRequest.getName();
        String introduction = pictureQueryRequest.getIntroduction();
        Long picSize = pictureQueryRequest.getPicSize();
        Integer picWidth = pictureQueryRequest.getPicWidth();
        Integer picHeight = pictureQueryRequest.getPicHeight();
        Double picScale = pictureQueryRequest.getPicScale();
        String picFormat = pictureQueryRequest.getPicFormat();
        String searchText = pictureQueryRequest.getSearchText();
        Long spaceId = pictureQueryRequest.getSpaceId();
        boolean nullSpaceId = pictureQueryRequest.isNullSpaceId();
        Long userId = pictureQueryRequest.getUserId();
        String sortField = pictureQueryRequest.getSortField();
        String sortOrder = pictureQueryRequest.getSortOrder();
        Integer reviewStatus = pictureQueryRequest.getReviewStatus();
        String reviewMessage = pictureQueryRequest.getReviewMessage();
        Date startEditTime = pictureQueryRequest.getStartEditTime();
        Date endEditTime = pictureQueryRequest.getEndEditTime();
        Long reviewerId = pictureQueryRequest.getReviewerId();
        // 从多字段中搜索
        if (StrUtil.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("name", searchText)
                    .or()
                    .like("introduction", searchText)
            );
        }
        queryWrapper.eq(ObjUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        queryWrapper.like(StrUtil.isNotBlank(introduction), "introduction", introduction);
        queryWrapper.like(StrUtil.isNotBlank(picFormat), "picFormat", picFormat);
        queryWrapper.like(StrUtil.isNotBlank(reviewMessage), "reviewMessage", reviewMessage);
        queryWrapper.eq(ObjUtil.isNotEmpty(picWidth), "picWidth", picWidth);
        queryWrapper.eq(ObjUtil.isNotEmpty(spaceId), "spaceId", spaceId);
        queryWrapper.isNull(nullSpaceId, "spaceId");
        queryWrapper.eq(ObjUtil.isNotEmpty(picHeight), "picHeight", picHeight);
        queryWrapper.eq(ObjUtil.isNotEmpty(picSize), "picSize", picSize);
        queryWrapper.eq(ObjUtil.isNotEmpty(picScale), "picScale", picScale);
        queryWrapper.eq(ObjUtil.isNotEmpty(reviewStatus), "reviewStatus", reviewStatus);
        queryWrapper.eq(ObjUtil.isNotEmpty(reviewerId), "reviewerId", reviewerId);
        queryWrapper.ge(ObjUtil.isNotEmpty(startEditTime), "editTime", startEditTime);
        queryWrapper.lt(ObjUtil.isNotEmpty(endEditTime), "editTime", endEditTime);
        // 排序
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        // 新增分类和标签参数
        Long categoryId = pictureQueryRequest.getCategoryId();
        List<Long> tagIds = pictureQueryRequest.getTagIds();
        // 分类查询（使用子查询）
        if (ObjUtil.isNotEmpty(categoryId)) {
            queryWrapper.inSql("id",
                    "SELECT pictureId FROM picture_category WHERE categoryId = " + categoryId);
        }
        // 标签查询（多个标签AND查询）
        if (CollUtil.isNotEmpty(tagIds)) {
            for (Long tagId : tagIds) {
                queryWrapper.inSql("id",
                        "SELECT pictureId FROM picture_tag WHERE tagId = " + tagId);
            }
        }
        return queryWrapper;
    }

    @Override
    public PictureVO getPictureVO(Picture picture, HttpServletRequest request) {
        // 对象转封装类
        PictureVO pictureVO = this.convertToVO(picture);
        // 关联查询用户信息
        Long userId = picture.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            pictureVO.setUser(userVO);
        }
        return pictureVO;
    }

    /**
     * 分页获取图片封装
     */
    @Override
    public Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request) {
        List<Picture> pictureList = picturePage.getRecords();
        Page<PictureVO> pictureVOPage = new Page<>(picturePage.getCurrent(), picturePage.getSize(), picturePage.getTotal());
        if (CollUtil.isEmpty(pictureList)) {
            return pictureVOPage;
        }
        // 对象列表 => 封装对象列表
        List<PictureVO> pictureVOList = pictureList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        // 1. 关联查询用户信息
        Set<Long> userIdSet = pictureList.stream().map(Picture::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 填充信息
        pictureVOList.forEach(pictureVO -> {
            Long userId = pictureVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            pictureVO.setUser(userService.getUserVO(user));
        });
        pictureVOPage.setRecords(pictureVOList);
        return pictureVOPage;
    }

    @Override
    public void validPicture(Picture picture) {
        ThrowUtils.throwIf(picture == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        Long id = picture.getId();
        String url = picture.getUrl();
        String introduction = picture.getIntroduction();
        // 修改数据时，id 不能为空，有参数则校验
        ThrowUtils.throwIf(ObjUtil.isNull(id), ErrorCode.PARAMS_ERROR, "id 不能为空");
        if (StrUtil.isNotBlank(url)) {
            ThrowUtils.throwIf(url.length() > 1024, ErrorCode.PARAMS_ERROR, "url 过长");
        }
        if (StrUtil.isNotBlank(introduction)) {
            ThrowUtils.throwIf(introduction.length() > 800, ErrorCode.PARAMS_ERROR, "简介过长");
        }
    }

    @Override
    public void pictureReview(PictureReviewRequest pictureReviewRequest, User loginUser) {
        // 校验参数
        ThrowUtils.throwIf(pictureReviewRequest == null, ErrorCode.PARAMS_ERROR);
        Long id = pictureReviewRequest.getId();
        Integer reviewStatus = pictureReviewRequest.getReviewStatus();
        PictureReviewStatusEnum reviewStatusEnum = PictureReviewStatusEnum.getEnumByValue(reviewStatus);
        String reviewMessage = pictureReviewRequest.getReviewMessage();
        // 待审核状态
        if (id == null || reviewMessage == null || PictureReviewStatusEnum.REVIEWING.equals(reviewStatusEnum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断数据是否存在
        Picture oldPicture = this.getById(id);
        ThrowUtils.throwIf(oldPicture == null, ErrorCode.NOT_FOUND_ERROR);
        // 不能是重复的状态
        if (oldPicture.getReviewStatus().equals(reviewStatus)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请勿重复审核");
        }
        // 更新审核状态
        Picture picture = new Picture();
        BeanUtils.copyProperties(pictureReviewRequest, picture);
        picture.setReviewerId(loginUser.getId());
        picture.setReviewTime(new Date());
        boolean result = this.updateById(picture);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }
    @Override
    public void pictureReviewBatch(List<Long> pictureIdList, User loginUser) {
        List<Picture> pictureList = baseMapper.selectByIds(pictureIdList);
        pictureList.forEach(picture -> this.fillReviewParams(picture, loginUser));
        boolean result = this.updateBatchById(pictureList);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public void fillReviewParams(Picture picture, User loginUser) {
        if (UserRoleEnum.ADMIN.getValue().equals(loginUser.getUserRole())) {
            // 管理员自动过审
            picture.setReviewerId(loginUser.getId());
            picture.setReviewTime(new Date());
            picture.setReviewMessage("管理员审核通过");
            picture.setReviewStatus(PictureReviewStatusEnum.PASS.getValue());
        } else {
            // 非管理员，创建修改图片都修改为待审核状态
            picture.setReviewStatus(PictureReviewStatusEnum.REVIEWING.getValue());
        }
    }

    @Override
    public void checkPictureAuth(User loginUser, Picture picture) {
        Long userId = loginUser.getId();
        Long spaceId = picture.getSpaceId();
        if (spaceId == null) {
            // 公共图库，仅管理员和本人可以修改
            if (!userService.isAdmin(loginUser) && !userId.equals(picture.getUserId())) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限修改本张图片");
            }
        } else {
            // 私有空间图库，仅本人可以修改
            if (!userId.equals(picture.getUserId())) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限修改本张图片");
            }
        }
    }

    @Override
    public PictureVO convertToVO(Picture picture) {
        PictureVO pictureVO = new PictureVO();
        BeanUtils.copyProperties(picture, pictureVO);
        List<Long> pictureTagIdList = pictureTagService.getTagIdsByPictureId(picture.getId());
        if (CollUtil.isNotEmpty(pictureTagIdList)) {
            List<Tag> tagList = tagService.listByIds(pictureTagIdList);
            List<TagVO> tagVOList = tagList.stream().map(item -> tagService.convertToVO(item)).collect(Collectors.toList());
            pictureVO.setTagList(tagVOList);
        } else {
            pictureVO.setTagList(new ArrayList<>());
        }
        Long pictureCategoryId = pictureCategoryService.getCategoryIdByPictureId(picture.getId());
        if (pictureCategoryId != null) {
            Category category = categoryService.getById(pictureCategoryId);
            pictureVO.setCategory(categoryService.convertToVO(category));
        } else {
            pictureVO.setCategory(null);
        }
        return pictureVO;
    }

    @Async
    @Override
    public void clearPictureFile(Picture oldPicture) {
        // 判断该图片是否被多条记录使用
        String pictureUrl = oldPicture.getUrl();
        long count = this.lambdaQuery()
                .eq(Picture::getUrl, pictureUrl)
                .count();
        // 有不止一条记录用到了该图片，不清理
        if (count > 1) {
            return;
        }
        cosManager.deleteObject(subStringUrl(pictureUrl));
        // 清理缩略图
        String thumbnailUrl = oldPicture.getThumbnailUrl();
        if (StrUtil.isNotBlank(thumbnailUrl)) {
            cosManager.deleteObject(subStringUrl(thumbnailUrl));
        }
        // 清理原始文件
        String originUrl = oldPicture.getOriginUrl();
        if (StrUtil.isNotBlank(originUrl)) {
            cosManager.deleteObject(subStringUrl(originUrl));
        }
    }

    @Override
    public List<PictureVO> searchPictureByColor(Long spaceId, String picColor, User loginUser) {
        // 1.校验参数
        ThrowUtils.throwIf(StrUtil.isBlank(picColor), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(spaceId == null, ErrorCode.PARAMS_ERROR);
        // 2.校验空间权限
        Space space = spaceService.getById(spaceId);
        ThrowUtils.throwIf(space == null, ErrorCode.NOT_FOUND_ERROR, "空间不存在");
        spaceService.checkSpaceAuth(loginUser, space);
        // 3.查询空间下有色调的图片
        List<Picture> pictureList = this.lambdaQuery()
                .eq(Picture::getSpaceId, spaceId)
                .isNotNull(Picture::getPicColor)
                .list();
        if (CollUtil.isEmpty(pictureList)) {
            // 如果为空，返回空列表
            return Collections.emptyList();
        }
        Color targetColor = Color.decode(picColor);
        // 4.计算相似度并排序
        List<Picture> sortedPictureList = pictureList.stream()
                .sorted(Comparator.comparingDouble(picture -> {
                    // 将字符串转为十六进制
                    Color color = Color.decode(picture.getPicColor());
                    return ColorSimilarUtils.calculateSimilarity(color, targetColor);
                }))
                .limit(10)
                .collect(Collectors.toList());
        // 5.返回结果
        return sortedPictureList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void editPictureByBatch(PictureEditByBatchRequest pictureEditByBatchRequest, User loginUser) {
        List<Long> pictureIdList = pictureEditByBatchRequest.getPictureIdList();
        List<Long> tagIds = pictureEditByBatchRequest.getTagIds();
        ThrowUtils.throwIf(CollUtil.isEmpty(tagIds), ErrorCode.PARAMS_ERROR, "至少选择一个标签");
        Long categoryId = pictureEditByBatchRequest.getCategoryId();
        ThrowUtils.throwIf(categoryId == null, ErrorCode.PARAMS_ERROR, "请选择一个分类");
        Long spaceId = pictureEditByBatchRequest.getSpaceId();
        // 1.校验参数
        ThrowUtils.throwIf(spaceId == null || CollUtil.isEmpty(pictureIdList), ErrorCode.PARAMS_ERROR);
        // 2.校验权限
        Space space = spaceService.getById(spaceId);
        ThrowUtils.throwIf(space == null, ErrorCode.NOT_FOUND_ERROR, "空间不存在");
        spaceService.checkSpaceAuth(loginUser, space);
        // 3.查询指定图片
        List<Picture> pictureList = this.lambdaQuery()
                .select(Picture::getId, Picture::getSpaceId)
                .eq(Picture::getSpaceId, spaceId)
                .in(Picture::getId, pictureIdList)
                .list();
        if (CollUtil.isEmpty(pictureList)) {
            return;
        }
        // 4.更新分类和标签
        pictureList.forEach(picture -> {
            // 更新标签和分类使用次数
            Long pictureId = picture.getId();
            // 查询关联表中该图片关联的标签id，并且减去旧的标签使用次数，并加上新的标签使用次数
            List<Long> oldTagList = pictureTagService.getTagIdsByPictureId(pictureId);
            tagService.decrementTagCount(oldTagList);
            tagService.incrementTagCount(tagIds);
            // 2.更新分类使用次数
            // 查询关联表中该图片关联的分类id，并且减去旧的分类使用次数，并加上新的分类使用次数
            Long oldCategoryId = pictureCategoryService.getCategoryIdByPictureId(pictureId);
            categoryService.decrementCategoryCount(oldCategoryId);
            categoryService.incrementCategoryCount(categoryId);
            // 更新关联表
            pictureCategoryService.updatePictureCategory(pictureId, categoryId);
            pictureTagService.updatePictureTag(pictureId, tagIds);
        });
        // 5.批量更新名称
        // 批量重命名
        String nameRule = pictureEditByBatchRequest.getNameRule();
        fillPictureWithNameRule(pictureList, nameRule);
        this.updateBatchById(pictureList);
    }

    @Override
    @Transactional
    public void deletePicture(long id, User loginUser) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        Picture oldPicture = this.getById(id);
        ThrowUtils.throwIf(oldPicture == null, ErrorCode.NOT_FOUND_ERROR);
        // 校验权限，已经改为注解释鉴权
//        this.checkPictureAuth(loginUser, oldPicture);
        // 操作数据库
        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除图片失败");
        // 更新空间额度
        if (oldPicture.getSpaceId() != null) {
            boolean update = spaceService.lambdaUpdate()
                    .eq(Space::getId, oldPicture.getSpaceId())
                    .setSql("totalSize = totalSize - " + oldPicture.getPicSize())
                    .setSql("totalCount = totalCount - 1")
                    .update();
            ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "更新空间信息失败");
        }
        // 清理云端文件
        this.clearPictureFile(oldPicture);
    }

    @Override
    public CreateOutPaintingTaskResponse createOutPaintingTask(CreatePictureOutPaintingTaskRequest createPictureOutPaintingTaskRequest, User loginUser) {
        Long pictureId = createPictureOutPaintingTaskRequest.getPictureId();
        Picture picture = this.getById(pictureId);
        ThrowUtils.throwIf(picture == null, ErrorCode.NOT_FOUND_ERROR, "图片不存在");
        // 校验权限，已经改为注解式鉴权
//        this.checkPictureAuth(loginUser, picture);
        // 构造请求参数
        CreateOutPaintingTaskRequest taskRequest = new CreateOutPaintingTaskRequest();
        CreateOutPaintingTaskRequest.Input input = new CreateOutPaintingTaskRequest.Input();
        input.setImageUrl(picture.getOriginUrl());
        taskRequest.setInput(input);
        BeanUtil.copyProperties(createPictureOutPaintingTaskRequest, taskRequest);
        // 创建任务
        return aliYunAiApi.createOutPaintingTask(taskRequest);
    }

    /**
     * 切割url
     *
     * @param url
     * @return
     */
    private String subStringUrl(String url) {
        int publicIndex = url.indexOf("/public");
        if (publicIndex == -1) {
            publicIndex = url.indexOf("/space");
        }
        return url.substring(publicIndex);
    }

    /**
     * nameRule 格式：图片{序号}
     *
     * @param pictureList
     * @param nameRule
     */
    private void fillPictureWithNameRule(List<Picture> pictureList, String nameRule) {
        if (CollUtil.isEmpty(pictureList) || StrUtil.isBlank(nameRule)) {
            return;
        }
        long count = 1;
        try {
            for (Picture picture : pictureList) {
                String pictureName = nameRule.replaceAll("\\{序号}", String.valueOf(count++));
                picture.setName(pictureName);
            }
        } catch (Exception e) {
            log.error("名称解析错误", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "名称解析错误");
        }
    }

    /**
     * 设置默认分类和标签
     *
     * @param savedPictureId
     */
    private void setDefaultTagCategory(Long savedPictureId) {
        try {
            // 1.设置默认分类
            Long defaultCategoryId = categoryService.getDefaultCategoryId();
            PictureCategory pictureCategory = new PictureCategory();
            pictureCategory.setPictureId(savedPictureId);
            pictureCategory.setCategoryId(defaultCategoryId);
            pictureCategoryService.save(pictureCategory);

            // 2.设置默认标签
            List<Long> defaultTagIds = tagService.getDefaultTagIds();
            List<PictureTag> pictureTags = defaultTagIds.stream()
                    .map(tagId -> {
                        PictureTag pt = new PictureTag();
                        pt.setPictureId(savedPictureId);
                        pt.setTagId(tagId);
                        return pt;
                    }).collect(Collectors.toList());
            pictureTagService.saveBatch(pictureTags);
        } catch (BusinessException e) {
            // 如果设置默认值失败，删除已创建的图片记录
            this.removeById(savedPictureId);
            throw e;
        }
    }
}




