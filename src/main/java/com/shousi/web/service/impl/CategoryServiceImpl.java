package com.shousi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.model.entity.Category;
import com.shousi.web.service.CategoryService;
import com.shousi.web.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 86172
* @description 针对表【category(分类表)】的数据库操作Service实现
* @createDate 2025-03-27 22:29:34
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




