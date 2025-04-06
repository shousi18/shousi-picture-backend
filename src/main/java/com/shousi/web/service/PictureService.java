package com.shousi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shousi.web.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.shousi.web.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.shousi.web.model.dto.picture.*;
import com.shousi.web.model.entity.Picture;
import com.shousi.web.model.entity.User;
import com.shousi.web.model.vo.PictureVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 86172
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-03-24 10:18:52
*/
public interface PictureService extends IService<Picture> {
    /**
     * 上传图片（文件 / URL）
     *
     * @param inputSource 输入源
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(Object inputSource,
                            PictureUploadRequest pictureUploadRequest,
                            User loginUser);

    /**
     * 公共查询条件封装
     * @param pictureQueryRequest
     * @return
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /**
     * 图片信息脱敏
     * @param picture
     * @param request
     * @return
     */
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    /**
     * 分页获取图片封装
     * @param picturePage
     * @param request
     * @return
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    /**
     * 图片信息校验
     * @param picture
     */
    void validPicture(Picture picture);

    /**
     * 图片审核
     * @param pictureReviewRequest
     * @param loginUser
     */
    void pictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);

    /**
     * 补充审核参数
     * @param picture
     * @param loginUser
     */
    void fillReviewParams(Picture picture, User loginUser);

    /**
     * 校验图片权限
     *
     * @param loginUser
     * @param picture
     */
    void checkPictureAuth(User loginUser, Picture picture);

    /**
     * 转换为对应的VO类
     * @param picture
     * @return
     */
    PictureVO convertToVO(Picture picture);

    /**
     * 清除图片文件（异步进行清理）
     * @param oldPicture
     */
    void clearPictureFile(Picture oldPicture);

    /**
     * 根据颜色搜索图片
     * @param spaceId
     * @param picColor
     * @param loginUser
     * @return
     */
    List<PictureVO> searchPictureByColor(Long spaceId, String picColor, User loginUser);

    /**
     * 批量编辑图片
     * @param pictureUploadByBatchRequest
     * @param loginUser
     */
    void editPictureByBatch(PictureEditByBatchRequest pictureUploadByBatchRequest, User loginUser);

    /**
     * 删除图片
     * @param id
     * @param loginUser
     */
    void deletePicture(long id, User loginUser);

    /**
     * 创建图片补图任务
     * @param createPictureOutPaintingTaskRequest
     * @param loginUser
     * @return
     */
    CreateOutPaintingTaskResponse createOutPaintingTask(CreatePictureOutPaintingTaskRequest createPictureOutPaintingTaskRequest, User loginUser);
}
