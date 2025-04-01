package com.shousi.web.service;

import com.shousi.web.model.entity.PictureTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 86172
 * @description 针对表【picture_tag(图片标签关联表)】的数据库操作Service
 * @createDate 2025-03-27 22:20:05
 */
public interface PictureTagService extends IService<PictureTag> {

    /**
     * 保存图片和标签关系
     *
     * @param id
     * @param tags
     */
    void updatePictureTag(long id, List<Long> tags);

    /**
     * 根据图片id获取标签id
     *
     * @param pictureId
     */
    List<Long> getTagIdsByPictureId(long pictureId);

    /**
     * 根据图片id删除图片和标签关系
     * @param pictureId
     */
    void deleteByPictureId(long pictureId);
}
