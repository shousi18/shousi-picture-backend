package com.shousi.web.mapper;

import com.shousi.web.model.entity.PictureTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shousi.web.model.vo.space.analyze.SpaceTagAnalyzeResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86172
* @description 针对表【picture_tag(图片标签关联表)】的数据库操作Mapper
* @createDate 2025-03-27 22:20:05
* @Entity com.shousi.web.model.entity.PictureTag
*/
public interface PictureTagMapper extends BaseMapper<PictureTag> {

    List<SpaceTagAnalyzeResponse> selectTagStatistics(@Param("pictureIdList") List<Long> pictureIdList);
}




