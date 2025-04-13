package com.shousi.web.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.shousi.web.annotation.AuthCheck;
import com.shousi.web.common.BaseResponse;
import com.shousi.web.constant.RedisKeyConstant;
import com.shousi.web.constant.UserConstant;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.model.dto.tag.TagAddRequest;
import com.shousi.web.model.entity.Tag;
import com.shousi.web.model.vo.TagVO;
import com.shousi.web.service.PictureTagService;
import com.shousi.web.service.TagService;
import com.shousi.web.utils.ResultUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addTag(@RequestBody TagAddRequest tagAddRequest) {
        ThrowUtils.throwIf(tagAddRequest == null, ErrorCode.PARAMS_ERROR);
        Long tagId = tagService.addTag(tagAddRequest);
        return ResultUtils.success(tagId);
    }

    @GetMapping("/list/hot")
    public BaseResponse<List<TagVO>> listHotTags() {
        String tagList = stringRedisTemplate.opsForValue().get(RedisKeyConstant.PICTURE_HOT_TAG_LIST);
        if (StrUtil.isNotBlank(tagList)) {
            return ResultUtils.success(JSONUtil.toList(tagList, TagVO.class));
        }
        List<TagVO> tagVOList = tagService.listHotTags();
        stringRedisTemplate.opsForValue().set(
                RedisKeyConstant.PICTURE_HOT_TAG_LIST,
                JSONUtil.toJsonStr(tagVOList),
                30,
                TimeUnit.DAYS
        );
        return ResultUtils.success(tagVOList);
    }

    @GetMapping("/list")
    public BaseResponse<List<TagVO>> listTags() {
        String tagList = stringRedisTemplate.opsForValue().get(RedisKeyConstant.PICTURE_TAG_LIST);
        if (StrUtil.isNotBlank(tagList)) {
            return ResultUtils.success(JSONUtil.toList(tagList, TagVO.class));
        }
        List<TagVO> tagVOList = tagService.listTags();
        stringRedisTemplate.opsForValue().set(
                RedisKeyConstant.PICTURE_TAG_LIST,
                JSONUtil.toJsonStr(tagVOList),
                30,
                TimeUnit.DAYS
        );
        return ResultUtils.success(tagVOList);
    }
}
