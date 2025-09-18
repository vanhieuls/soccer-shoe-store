package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.Order;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>, JpaSpecificationExecutor<Order> {
    List<Order> findByUserId(Long id);
    Optional<List<Order>> findByUserAndOderStatus(User user, OderStatus status);
    Page<Order> findByUserAndOderStatus(User user, OderStatus status, Pageable pageable);
    Page<Order> findAllStatusByUser(User user, Pageable pageable);

}
