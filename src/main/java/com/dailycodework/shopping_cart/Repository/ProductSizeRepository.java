package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize,Long> {
    Optional<List<ProductSize>> findByProductId(Long productId);
    boolean existsBySizeIdAndProductId(Long sizeId, Long productId);
}
