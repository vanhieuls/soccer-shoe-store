package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Request.ProductRequest;
import com.dailycodework.shopping_cart.DTO.Request.ProductUpdateRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Helper.ProductSpecification.ProductFilter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
public interface IProduct {
    ApiResponse<List<ProductResponse>> getAllProducts();
    ProductResponse getProductById(Long id);
    ApiResponse<Void> deleteProductById(Long id);
    List<Product> getProductsByCategory(String category);
    List<ProductResponse> getProductsByBrand(String brand);
    List<ProductResponse> getProductsByName(String name);
    List<ProductResponse> getProductsByCategoryAndBrand(String category, String brand);
    List<ProductResponse> getProductsByCategoryAndName(String category, String name);
    List<ProductResponse> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
    Product createProduct (ProductRequest request);
    ProductResponse updateProductExisted(ProductUpdateRequest request, Long id);
//    Page<ProductResponse> getProducts (Integer pageNumber, Integer pageSize, String properties,String sortDir,
//                                                BigDecimal minPrice,
//                                                BigDecimal maxPrice);

    Page<ProductResponse> getProducts (Integer pageNumber, Integer pageSize,String category, String brand, String properties,String sortDir,
                                       BigDecimal minPrice,
                                       BigDecimal maxPrice);
    Page<ProductResponse> getProductByFilter (ProductFilter filter);
    Long countByDiscount();
    List<ProductResponse> findTop10ByCreatedDesc();
}
