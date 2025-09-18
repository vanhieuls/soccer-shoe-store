package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Dto.ReviewDto;
import com.dailycodework.shopping_cart.DTO.Request.ReviewRequest;
import com.dailycodework.shopping_cart.Entity.Order;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Entity.Review;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Helper.Filter.ProfanityFilter;
import com.dailycodework.shopping_cart.Mapper.ReviewMapper;
import com.dailycodework.shopping_cart.Repository.OrderRepository;
import com.dailycodework.shopping_cart.Repository.ProductRepository;
import com.dailycodework.shopping_cart.Repository.ReviewRepository;
import com.dailycodework.shopping_cart.Repository.UserRepository;
import com.dailycodework.shopping_cart.Service.Interface.IReview;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpReview implements IReview {
    UserRepository userRepository;
    ProductRepository productRepository;
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    @Override
    public ReviewDto createReview(ReviewRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if(ProfanityFilter.filterProfanity(request.getComment())){
            throw new AppException(ErrorCode.INAPPROPRIATE_CONTENT);
        }
        if(ProfanityFilter.isBlockLink(request.getComment())){
            throw new AppException(ErrorCode.LINK_IN_COMMENT);
        }
        Review review = Review.builder()
                .comment(request.getComment())
                .rating(request.getRating())
                .product(product)
                .user(user)
                .build();
        reviewRepository.save(review);
        return reviewMapper.toReviewDto(review);
    }

    @Override
    public Page<ReviewDto> getListReview(Integer pageNo, Integer pageSize, Long productId, Integer rating) {
        Pageable pageable = null;
        if(pageNo == null || pageNo < 0) pageNo = 0;
        if(pageSize == null || pageSize <= 0) pageSize = 5;
        pageable = PageRequest.of(pageNo,pageSize);
        Page<ReviewDto> reviews;
        if(rating == null){
            reviews = reviewRepository.findByProductId(productId,pageable).map(reviewMapper::toReviewDto);
        }
        else reviews = reviewRepository.findByProductIdAndRating(productId,rating,pageable).map(reviewMapper::toReviewDto);
        if(reviews.isEmpty()){
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
        }
        return reviews;
    }

//    @Override
//    public List<ReviewDto> getListReviewByRating(int rating) {
//        List<ReviewDto> ListReviews = reviewRepository.findByRating(rating).stream().map(reviewMapper::toReviewDto).toList();
//        if(ListReviews.isEmpty()){
//            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
//        }
//        return ListReviews;
//    }

}
