package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.ReviewDto;
import com.dailycodework.shopping_cart.DTO.Request.ReviewRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Entity.Review;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.Interface.IReview;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ReviewController {
    IReview reviewService;
    @PostMapping("/create")
    public ApiResponse<ReviewDto> createReview(@Valid @RequestBody ReviewRequest request) {
        return ApiResponse.<ReviewDto>builder()
                .code(200)
                .message("create review success")
                .result(reviewService.createReview(request))
                .build();
    }
    @GetMapping("/getList/{productId}")
    public ApiResponse<Page<ReviewDto>> getListReviewByProductId(@PathVariable Long productId, @RequestParam(defaultValue = "0") Integer pageNo,
                                                                 @RequestParam(defaultValue = "5") Integer pageSize,
                                                                 @RequestParam(required = false) Integer rating) {
        Page<ReviewDto> reviewDtos = reviewService.getListReview(pageNo, pageSize, productId, rating);
        return ApiResponse.<Page<ReviewDto>>builder()
                .code(200)
                .message("Get review list success")
                .result(reviewDtos)
                .build();
    }
}
