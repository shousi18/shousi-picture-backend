package com.shousi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.model.dto.category.CategoryAddRequest;
import com.shousi.web.model.entity.Category;
import com.shousi.web.model.entity.Tag;
import com.shousi.web.model.vo.CategoryVO;
import com.shousi.web.service.CategoryService;
import com.shousi.web.mapper.CategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.shousi.web.constant.PictureConstant.DEFAULT_CATEGORY_NAME;

/**
* @author 86172
* @description 针对表【category(分类表)】的数据库操作Service实现
* @createDate 2025-03-27 22:29:34
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Override
    public List<CategoryVO> listHotCategories() {
        // 查询最热门前十个分类
        LambdaQueryWrapper<Category> queryWrapper = new QueryWrapper<Category>().lambda()
                .orderByDesc(Category::getTotalNum)
                .last("limit 6");
        List<Category> list = this.list(queryWrapper);
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream().map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementCategoryCount(Long categoryId) {
        if (categoryId == null) {
            return;
        }
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Category::getId, categoryId)
                .setSql("totalNum = totalNum + 1");
        boolean result = this.update(updateWrapper);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public void decrementCategoryCount(Long categoryId) {
        if (categoryId == null) {
            return;
        }
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Category::getId, categoryId)
                .setSql("totalNum = totalNum - 1");
        boolean result = this.update(updateWrapper);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public CategoryVO convertToVO(Category category) {
        ThrowUtils.throwIf(category == null, ErrorCode.NOT_FOUND_ERROR);
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category, categoryVO);
        return categoryVO;
    }

    @Override
    public Long getDefaultCategoryId() {
        LambdaQueryWrapper<Category> query = new LambdaQueryWrapper<>();
        query.eq(Category::getCategoryName, DEFAULT_CATEGORY_NAME); // 根据你的默认分类名称查询
        Category defaultCategory = this.getOne(query);
        if (defaultCategory == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "默认分类未配置");
        }
        this.incrementCategoryCount(defaultCategory.getId());
        return defaultCategory.getId();
    }

    @Override
    public List<CategoryVO> listCategories() {
        List<Category> list = this.list();
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream().map(this::convertToVO)
                .collect(Collectors.toList());
    }
}




