package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OderItemRepository extends JpaRepository<OrderItem, Long> {
}
