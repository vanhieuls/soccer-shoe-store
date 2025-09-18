package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Request.CategoryRequest;
import com.dailycodework.shopping_cart.Entity.Category;

import java.util.List;

public interface ICategory {
    Category getCategoryByName(String name);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category createCategory(CategoryRequest category);
    Category updateCategory (CategoryRequest category, Long id);
    void deleteCategoryById(Long id);
}
