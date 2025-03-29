package com.shousi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.model.entity.PictureCategory;
import com.shousi.web.service.PictureCategoryService;
import com.shousi.web.mapper.PictureCategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 86172
* @description 针对表【picture_category(图片分类关联表)】的数据库操作Service实现
* @createDate 2025-03-27 22:29:34
*/
@Service
public class PictureCategoryServiceImpl extends ServiceImpl<PictureCategoryMapper, PictureCategory>
    implements PictureCategoryService{

    @Override
    public void updatePictureCategory(long pictureId, long categoryId) {
        // 1.删除旧的关联记录
        LambdaQueryWrapper<PictureCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PictureCategory::getPictureId, pictureId);
        this.remove(queryWrapper);

        // 2.保存新分类
        if (categoryId > 0) {
            PictureCategory pictureCategory = new PictureCategory();
            pictureCategory.setPictureId(pictureId);
            pictureCategory.setCategoryId(categoryId);
            this.save(pictureCategory);
        }
    }

    @Override
    public Long getCategoryIdByPictureId(long pictureId) {
        if (pictureId < 0) {
            return null;
        }
        LambdaQueryWrapper<PictureCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PictureCategory::getPictureId, pictureId)
                .select(PictureCategory::getCategoryId);
        return getOne(queryWrapper).getCategoryId();
    }
}




