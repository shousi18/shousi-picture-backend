package com.shousi.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.manager.CosManager;
import com.shousi.web.manager.upload.*;
import com.shousi.web.mapper.PictureMapper;
import com.shousi.web.model.dto.file.UploadPictureResult;
import com.shousi.web.model.dto.picture.PictureQueryRequest;
import com.shousi.web.model.dto.picture.PictureReviewRequest;
import com.shousi.web.model.dto.picture.PictureUploadByBatchRequest;
import com.shousi.web.model.dto.picture.PictureUploadRequest;
import com.shousi.web.model.entity.*;
import com.shousi.web.model.eums.PictureReviewStatusEnum;
import com.shousi.web.model.eums.UserRoleEnum;
import com.shousi.web.model.vo.PictureVO;
import com.shousi.web.model.vo.TagVO;
import com.shousi.web.model.vo.UserVO;
import com.shousi.web.service.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


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

    @Override
    @Transactional
    public PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest, User loginUser) {
        // 校验参数
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        // 判断是新增还是更新，如果是更新则使用原本的图片id
        Long pictureId = null;
        if (pictureUploadRequest != null) {
            pictureId = pictureUploadRequest.getId();
        }
        // 如果是更新，判断图片是否存在
        Picture oldPicture = new Picture();
        if (pictureId != null) {
            oldPicture = this.getById(pictureId);
            ThrowUtils.throwIf(oldPicture == null, ErrorCode.NOT_FOUND_ERROR, "图片不存在");
            // 仅本人和管理员可以修改
            if (!oldPicture.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 上传图片，得到图片信息
        // 根据用户id分组
        String uploadPathPrefix = String.format("/public/%s", loginUser.getId());
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
            return convertToVO(picture);
        }
        // 保存图片信息
        boolean isSuccess = this.save(picture);
        ThrowUtils.throwIf(!isSuccess, ErrorCode.OPERATION_ERROR, "上传图片操作失败");
        // 需要设置默认分类和标签
        Long savedPictureId = picture.getId();
        if (pictureId == null) {
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
        Long userId = pictureQueryRequest.getUserId();
        String sortField = pictureQueryRequest.getSortField();
        String sortOrder = pictureQueryRequest.getSortOrder();
        Integer reviewStatus = pictureQueryRequest.getReviewStatus();
        String reviewMessage = pictureQueryRequest.getReviewMessage();
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
        queryWrapper.eq(ObjUtil.isNotEmpty(picHeight), "picHeight", picHeight);
        queryWrapper.eq(ObjUtil.isNotEmpty(picSize), "picSize", picSize);
        queryWrapper.eq(ObjUtil.isNotEmpty(picScale), "picScale", picScale);
        queryWrapper.eq(ObjUtil.isNotEmpty(reviewStatus), "reviewStatus", reviewStatus);
        queryWrapper.eq(ObjUtil.isNotEmpty(reviewerId), "reviewerId", reviewerId);
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
        List<PictureVO> pictureVOList = pictureList.stream().map(this::convertToVO).collect(Collectors.toList());
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
    public void fillReviewParams(Picture picture, User loginUser) {
        if (UserRoleEnum.ADMIN.equals(loginUser.getUserRole())) {
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

    /**
     * 切割url
     * @param url
     * @return
     */
    private String subStringUrl(String url) {
        int publicIndex = url.indexOf("/public");
        return url.substring(publicIndex);
    }
}




