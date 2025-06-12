package com.shousi.web.service;

import com.shousi.web.model.dto.category.CategoryAddRequest;
import com.shousi.web.model.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shousi.web.model.vo.CategoryVO;

import java.util.List;

/**
* @author 86172
* @description 针对表【category(分类表)】的数据库操作Service
* @createDate 2025-03-27 22:29:34
*/
public interface CategoryService extends IService<Category> {

    /**
     * 获取热门分类
     *
     * @return
     */
    List<CategoryVO> listHotCategories();

    /**
     * 增加分类使用次数
     * @param categoryId
     */
    void incrementCategoryCount(Long categoryId);

    /**
     * 减少分类使用次数
     * @param categoryId
     */
    void decrementCategoryCount(Long categoryId);

    /**
     * 转换为VO
     * @param category
     * @return
     */
    CategoryVO convertToVO(Category category);

    /**
     * 获取默认分类id
     * @return
     */
    Long getDefaultCategoryId();

    /**
     * 获取所有分类
     * @return
     */
    List<CategoryVO> listCategories();
}
