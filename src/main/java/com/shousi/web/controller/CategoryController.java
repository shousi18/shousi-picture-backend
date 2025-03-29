package com.shousi.web.controller;

import com.shousi.web.common.BaseResponse;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.model.dto.category.CategoryAddRequest;
import com.shousi.web.model.vo.CategoryVO;
import com.shousi.web.model.vo.TagVO;
import com.shousi.web.service.CategoryService;
import com.shousi.web.utils.ResultUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping("/add")
    public BaseResponse<Long> addCategory(@RequestBody CategoryAddRequest categoryAddRequest) {
        ThrowUtils.throwIf(categoryAddRequest == null, ErrorCode.PARAMS_ERROR);
        Long categoryId = categoryService.addCategory(categoryAddRequest);
        return ResultUtils.success(categoryId);
    }

    @GetMapping("/list/hot")
    public BaseResponse<List<CategoryVO>> listHotCategories() {
        List<CategoryVO> categoryVOList = categoryService.listHotCategories();
        return ResultUtils.success(categoryVOList);
    }
}
