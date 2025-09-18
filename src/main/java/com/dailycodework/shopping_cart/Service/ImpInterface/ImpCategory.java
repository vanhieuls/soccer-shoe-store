package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Request.CategoryRequest;
import com.dailycodework.shopping_cart.Entity.Category;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Mapper.CategoryMapper;
import com.dailycodework.shopping_cart.Repository.CategoryRepository;
import com.dailycodework.shopping_cart.Service.Interface.ICategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpCategory implements ICategory {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(CategoryRequest category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED); // Nếu tồn tại, ném lỗi
        }
        return categoryRepository.save(categoryMapper.toCategory(category));
    }

    @Override
    public Category updateCategory(CategoryRequest categoryRequest, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryMapper.updateCategory(category,categoryRequest);
        return Category.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.deleteById(id);
    }
}
