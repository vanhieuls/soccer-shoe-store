package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Request.CategoryRequest;
import com.dailycodework.shopping_cart.DTO.Response.CategoryResponse;
import com.dailycodework.shopping_cart.Entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory (CategoryRequest categoryRequest);
    CategoryResponse tocategoryResponse (Category category);
    void updateCategory(@MappingTarget Category category, CategoryRequest request);
}
