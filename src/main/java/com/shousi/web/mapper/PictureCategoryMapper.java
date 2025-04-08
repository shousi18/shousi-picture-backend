package com.shousi.web.mapper;

import com.shousi.web.model.entity.PictureCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shousi.web.model.vo.space.analyze.SpaceCategoryAnalyzeResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 86172
 * @description 针对表【picture_category(图片分类关联表)】的数据库操作Mapper
 * @createDate 2025-03-27 22:29:34
 * @Entity com.shousi.web.model.entity.PictureCategory
 */
public interface PictureCategoryMapper extends BaseMapper<PictureCategory> {

    List<SpaceCategoryAnalyzeResponse> selectCategoryStatistics(@Param("pictureIdList") List<Long> pictureIdList);
}




