package com.shousi.web.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.shousi.web.common.BaseResponse;
import com.shousi.web.constant.RedisKeyConstant;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.model.dto.category.CategoryAddRequest;
import com.shousi.web.model.vo.CategoryVO;
import com.shousi.web.model.vo.TagVO;
import com.shousi.web.service.CategoryService;
import com.shousi.web.utils.ResultUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/add")
    public BaseResponse<Long> addCategory(@RequestBody CategoryAddRequest categoryAddRequest) {
        ThrowUtils.throwIf(categoryAddRequest == null, ErrorCode.PARAMS_ERROR);
        Long categoryId = categoryService.addCategory(categoryAddRequest);
        return ResultUtils.success(categoryId);
    }

    @GetMapping("/list/hot")
    public BaseResponse<List<CategoryVO>> listHotCategories() {
        String categoryList = stringRedisTemplate.opsForValue().get(RedisKeyConstant.PICTURE_CATEGORY_LIST);
        if (StrUtil.isNotBlank(categoryList)) {
            return ResultUtils.success(JSONUtil.toList(categoryList, CategoryVO.class));
        }
        List<CategoryVO> categoryVOList = categoryService.listHotCategories();
        stringRedisTemplate.opsForValue().set(
                RedisKeyConstant.PICTURE_CATEGORY_LIST,
                JSONUtil.toJsonStr(categoryVOList),
                30,
                TimeUnit.DAYS
        );
        return ResultUtils.success(categoryVOList);
    }
}
