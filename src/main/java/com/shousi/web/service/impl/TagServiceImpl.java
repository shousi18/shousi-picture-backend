package com.shousi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.mapper.TagMapper;
import com.shousi.web.model.dto.tag.TagAddRequest;
import com.shousi.web.model.entity.Tag;
import com.shousi.web.model.vo.TagVO;
import com.shousi.web.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 86172
 * @description 针对表【tag(标签表)】的数据库操作Service实现
 * @createDate 2025-03-27 22:20:05
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<TagVO> listHotTags() {
        // 查询最热门前十个标签
        LambdaQueryWrapper<Tag> queryWrapper = new QueryWrapper<Tag>().lambda()
                .orderByDesc(Tag::getTotalNum)
                .last("limit 10");
        List<Tag> list = this.list(queryWrapper);
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream().map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public Long addTag(TagAddRequest tagAddRequest) {
        ThrowUtils.throwIf(tagAddRequest == null, ErrorCode.PARAMS_ERROR);
        Tag tag = new Tag();
        String tagName = tagAddRequest.getTagName();
        tag.setTagName(tagName);
        boolean save = this.save(tag);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        return tag.getId();
    }

    @Override
    public void incrementTagCount(List<Long> tags) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Tag::getId, tags)
                .setSql("totalNum = totalNum + 1");
        boolean result = this.update(updateWrapper);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public void decrementTagCount(List<Long> tags) {
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Tag::getId, tags)
                .setSql("totalNum = totalNum - 1");
        boolean result = this.update(updateWrapper);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public TagVO convertToVO(Tag tag) {
        ThrowUtils.throwIf(tag == null, ErrorCode.PARAMS_ERROR);
        TagVO tagVO = new TagVO();
        BeanUtils.copyProperties(tag, tagVO);
        return tagVO;
    }
}




