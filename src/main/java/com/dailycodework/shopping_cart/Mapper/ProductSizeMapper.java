package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.ProductsizeDto;
import com.dailycodework.shopping_cart.DTO.Request.ProductSizeRequest;
import com.dailycodework.shopping_cart.Entity.ProductSize;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductSizeMapper {
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "size.id", source = "sizeId")
    ProductSize toProductSize(ProductSizeRequest request);
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "size.size", target = "sizeName")
    ProductsizeDto toProductSizeDto(ProductSize productSize);
    List<ProductsizeDto> toListProductSizeDto(List<ProductSize> productSizes);
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "size.id", source = "sizeId")
    void updateProductSize(@MappingTarget ProductSize productSize, ProductSizeRequest request);
}
