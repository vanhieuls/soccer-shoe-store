package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Transactional
    void deleteAllByCartId (Long id);
    List<CartItem> findByCartId (Long id);
    Optional<Page<CartItem>> findByCartId(Long id, Pageable pageable);
}
