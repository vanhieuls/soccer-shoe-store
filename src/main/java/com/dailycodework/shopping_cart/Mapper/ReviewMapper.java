package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.ReviewDto;
import com.dailycodework.shopping_cart.Entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id",target = "productId")
    ReviewDto toReviewDto (Review review);
    List<ReviewDto> toReviewsDto (List<Review> reviews);
}
