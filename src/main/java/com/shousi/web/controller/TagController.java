package com.shousi.web.controller;

import com.shousi.web.annotation.AuthCheck;
import com.shousi.web.common.BaseResponse;
import com.shousi.web.constant.UserConstant;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.model.dto.tag.TagAddRequest;
import com.shousi.web.model.entity.Tag;
import com.shousi.web.model.vo.TagVO;
import com.shousi.web.service.PictureTagService;
import com.shousi.web.service.TagService;
import com.shousi.web.utils.ResultUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @Resource
    private PictureTagService pictureTagService;

    @PostMapping("/add")
    public BaseResponse<Long> addTag(@RequestBody TagAddRequest tagAddRequest) {
        ThrowUtils.throwIf(tagAddRequest == null, ErrorCode.PARAMS_ERROR);
        Long tagId = tagService.addTag(tagAddRequest);
        return ResultUtils.success(tagId);
    }

    @GetMapping("/list/hot")
    public BaseResponse<List<TagVO>> listHotTags() {
        List<TagVO> tagVOList = tagService.listHotTags();
        return ResultUtils.success(tagVOList);
    }
}
