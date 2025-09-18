package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findByProductId(Long productId, Pageable pageable);
    List<Review> findByRating(int rating);
    Page<Review> findByProductIdAndRating(Long productId, Integer Rating,
                                          Pageable pageable);
}
