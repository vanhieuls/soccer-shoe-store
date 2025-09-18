package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Request.CategoryRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.Category;
import com.dailycodework.shopping_cart.Service.Interface.ICategory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    ICategory categoryService;

    @GetMapping("/getcategory/{name}")
    public ApiResponse<Category> getCategoryByName(@PathVariable String name){
        Category category = categoryService.getCategoryByName(name);
        return ApiResponse.<Category>builder()
                .code(200)
                .message("get category success")
                .result(category)
                .build();
    }
    @GetMapping("/getcategory/{id}")
    public ApiResponse<Category> getCategoryById(@PathVariable Long id){
        Category category = categoryService.getCategoryById(id);
        return ApiResponse.<Category>builder()
                .code(200)
                .message("get category success")
                .result(category)
                .build();
    }
    @GetMapping("/getall")
    public ApiResponse<List<Category>> getAllCategories(){
        List<Category> category = categoryService.getAllCategories();
        return ApiResponse.<List<Category>>builder()
                .code(200)
                .message("get category success")
                .result(category)
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<Category> createCategory(@RequestBody CategoryRequest request){

        Category category = categoryService.createCategory(request);
        return ApiResponse.<Category>builder()
                .code(200)
                .message("create category success")
                .result(category)
                .build();
    }
    @PutMapping("/update/{id}")
    public ApiResponse<Category> updateCategory (@RequestBody CategoryRequest request, @PathVariable Long id){
        Category category = categoryService.updateCategory(request, id);
        return ApiResponse.<Category>builder()
                .code(200)
                .message("create category success")
                .result(category)
                .build();
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("category was delete")
                .build();
    }


}
