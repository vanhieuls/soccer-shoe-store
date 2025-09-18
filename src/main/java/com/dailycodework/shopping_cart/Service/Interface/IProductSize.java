package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.ProductsizeDto;
import com.dailycodework.shopping_cart.DTO.Request.ProductSizeRequest;
import com.dailycodework.shopping_cart.Entity.ProductSize;

import java.util.List;

public interface IProductSize {
    List<ProductsizeDto> findByProductId(Long productId);
    List<ProductsizeDto> getAll();
    ProductsizeDto getById(Long id);
    ProductSize getProductSizeById(Long id);
    ProductsizeDto createProductSize(ProductSizeRequest productSize);
    void deleteProductSize(Long id);
    ProductsizeDto update(Long id, ProductSizeRequest ps);
}
