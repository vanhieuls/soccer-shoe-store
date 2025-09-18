package com.dailycodework.shopping_cart.Helper.OrderSpecification;

import com.dailycodework.shopping_cart.Entity.Order;
import com.dailycodework.shopping_cart.Entity.OrderItem;
import com.dailycodework.shopping_cart.Entity.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {
    public static Specification<Order> OrderSpecification(String name, LocalDateTime startDay, LocalDateTime endDay) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                // Join sang OrderItem và Product
                Join<Order, OrderItem> orderItemsJoin = root.join("orderItems", JoinType.LEFT);
                Join<OrderItem, Product> productJoin = orderItemsJoin.join("product", JoinType.LEFT);

                // So sánh theo orderCode, user.name hoặc product.name
                Predicate byOrderCode = cb.like(root.get("orderCode"), "%" + name + "%");
                Predicate byProductName = cb.like(productJoin.get("name"), "%" + name + "%");

                predicates.add(cb.or(byOrderCode, byProductName));
            }

            if (startDay != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDay));
            }

            if (endDay != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDay));
            }

            // Tránh trùng dữ liệu khi join
            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
