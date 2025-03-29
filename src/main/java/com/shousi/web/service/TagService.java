package com.shousi.web.service;

import com.shousi.web.model.dto.tag.TagAddRequest;
import com.shousi.web.model.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shousi.web.model.vo.TagVO;

import java.util.List;

/**
 * @author 86172
 * @description 针对表【tag(标签表)】的数据库操作Service
 * @createDate 2025-03-27 22:20:05
 */
public interface TagService extends IService<Tag> {

    /**
     * 获取热门标签（前10）
     *
     * @return
     */
    List<TagVO> listHotTags();

    /**
     * 将Tag对象转换为TagVO对象
     *
     * @param tag
     * @return
     */
    TagVO convertToVO(Tag tag);

    /**
     * 添加标签
     *
     * @param tagAddRequest
     * @return
     */
    Long addTag(TagAddRequest tagAddRequest);

    /**
     * 增加标签使用次数
     * @param tags
     */
    void incrementTagCount(List<Long> tags);

    /**
     * 减少标签使用次数
     * @param tags
     */
    void decrementTagCount(List<Long> tags);

    /**
     * 获取默认标签id
     * @return
     */
    List<Long> getDefaultTagIds();
}
