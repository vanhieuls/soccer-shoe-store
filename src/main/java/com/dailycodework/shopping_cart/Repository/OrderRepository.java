package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.Order;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>, JpaSpecificationExecutor<Order> {
    List<Order> findByUserId(Long id);
    Optional<List<Order>> findByUserAndOderStatus(User user, OderStatus status);
    Page<Order> findByUserAndOderStatus(User user, OderStatus status, Pageable pageable);
    Page<Order> findAllStatusByUser(User user, Pageable pageable);
    //Lấy doanh thu theo ngày hoặc tháng hoặc năm
    @Query("""
   select coalesce(sum(o.totalAmount), 0)
   from Order o
   where o.oderStatus = :status
     and o.orderDate >= :start
     and o.orderDate < :end
""")
    BigDecimal getTotalRevenue(@Param("status") OderStatus status,
                               @Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);
    Long countByOderStatus(OderStatus status);
    List<Order> findByOderStatus(OderStatus status);
    @EntityGraph(attributePaths = {"payment"})
    @Query("select o from Order o")
    Page<Order> pageOrders(Pageable pageable);
    Optional<Order> findByOrderCode(Long orderCode);
}
