package com.shousi.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.model.entity.PictureTag;
import com.shousi.web.service.PictureTagService;
import com.shousi.web.mapper.PictureTagMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 86172
 * @description 针对表【picture_tag(图片标签关联表)】的数据库操作Service实现
 * @createDate 2025-03-27 22:20:05
 */
@Service
public class PictureTagServiceImpl extends ServiceImpl<PictureTagMapper, PictureTag>
        implements PictureTagService {

    @Resource
    private PictureTagMapper pictureTagMapper;

    @Override
    public void updatePictureTag(long pictureId, List<Long> tags) {
        // 1. 删除所有旧的关联记录
        LambdaQueryWrapper<PictureTag> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(PictureTag::getPictureId, pictureId);
        this.remove(deleteWrapper);

        // 2. 保存新标签（无需判断是否首次添加）
        if (CollUtil.isNotEmpty(tags)) {
            List<PictureTag> pictureTagList = new ArrayList<>();
            for (Long tagId : tags) {
                PictureTag pictureTag = new PictureTag();
                pictureTag.setPictureId(pictureId);
                pictureTag.setTagId(tagId);
                pictureTagList.add(pictureTag);
            }
            // 批量保存
            this.saveBatch(pictureTagList);
        }
    }

    @Override
    public List<Long> getTagIdsByPictureId(long pictureId) {
        if (pictureId < 0) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<PictureTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PictureTag::getPictureId, pictureId)
                .select(PictureTag::getTagId);
        return list(queryWrapper).stream()
                .map(PictureTag::getTagId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByPictureId(long pictureId) {
        LambdaQueryWrapper<PictureTag> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(PictureTag::getPictureId, pictureId);
        boolean result = this.remove(deleteWrapper);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除失败");
    }
}




