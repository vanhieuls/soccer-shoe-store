package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.ReviewDto;
import com.dailycodework.shopping_cart.DTO.Request.ReviewRequest;
import com.dailycodework.shopping_cart.Entity.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IReview {
    ReviewDto createReview (ReviewRequest request);
    Page<ReviewDto> getListReview(Integer pageNo, Integer pageSize , Long productId, Integer rating);
}
