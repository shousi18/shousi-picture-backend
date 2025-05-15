package com.shousi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shousi.web.model.entity.Picture;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* @author 86172
* @description 针对表【picture(图片)】的数据库操作Mapper
* @createDate 2025-03-24 10:18:52
* @Entity generator.domain.Picture
*/
public interface PictureMapper extends BaseMapper<Picture> {

    /**
     * 增加图片点赞数
     * @param pictureId
     */
    @Update("update picture set thumbCount = thumbCount + 1 where id = #{pictureId}")
    int incrementThumbCount(Long pictureId);

    /**
     * 减少图片点赞数
     * @param pictureId
     * @return
     */
    @Update("UPDATE picture SET thumbCount = GREATEST(thumbCount - 1, 0) WHERE id = #{pictureId}")
    int decrementThumbCount(@Param("pictureId") Long pictureId);
}




