package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Request.ImageDto;
import com.dailycodework.shopping_cart.DTO.Request.ProductRequest;
import com.dailycodework.shopping_cart.DTO.Request.ProductUpdateRequest;
import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.Entity.Image;
import com.dailycodework.shopping_cart.Entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "id", target = "imageId")
    @Mapping(source = "fileName", target = "imageName")
    ImageDto imageDto(Image image);
    @Mapping(source = "collection.name", target = "collectionId")
    @Mapping(target = "images", source = "images")
    ProductResponse toProductResponse (Product product);
    Product responseToProduct (ProductResponse product);
    Product toProduct (ProductRequest request);
    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);
//    @Mapping(target = "ProductResponse.images", source = "Product.images")
    List<ProductResponse> toProductResponses(List<Product> products);
}
