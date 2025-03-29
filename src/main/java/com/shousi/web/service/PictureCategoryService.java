package com.shousi.web.service;

import com.shousi.web.model.entity.PictureCategory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86172
* @description 针对表【picture_category(图片分类关联表)】的数据库操作Service
* @createDate 2025-03-27 22:29:34
*/
public interface PictureCategoryService extends IService<PictureCategory> {

    /**
     * 更新图片分类关联
     * @param pictureId
     * @param categoryId
     */
    void updatePictureCategory(long pictureId, long categoryId);

    /**
     * 根据图片id获取图片分类id
     * @param pictureId
     * @return
     */
    Long getCategoryIdByPictureId(long pictureId);
}
