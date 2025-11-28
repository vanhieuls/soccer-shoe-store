package com.dailycodework.shopping_cart.Helper.OrderSpecification;

import com.dailycodework.shopping_cart.Entity.Order;
import com.dailycodework.shopping_cart.Entity.OrderItem;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {
    public static Specification<Order> OrderSpecification(Long userId, String name, LocalDateTime startDay, LocalDateTime endDay) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (name!= null && !name.isEmpty()) {
                // Làm sạch chuỗi name, bỏ khoảng trắng thừa và ký tự %
                String cleanName = name.trim().replace("%", "");
                // Join sang OrderItem và Product
                Join<Order, OrderItem> orderItemsJoin = root.join("orderItems", JoinType.LEFT);
                Join<OrderItem, Product> productJoin = orderItemsJoin.join("product", JoinType.LEFT);

                // So sánh theo orderCode, user.name hoặc product.name
//                Predicate byOrderCode = cb.like(root.get("orderCode"), cleanName);
                Predicate byProductName = cb.like(productJoin.get("name"), "%" + cleanName + "%");

                // tìm theo orderCode (long) - chỉ khi parse được số
                Predicate byOrderCode = null;
                try {
                    Long codeAsNumber = Long.parseLong(cleanName);
                    byOrderCode = cb.equal(root.get("orderCode"), codeAsNumber);
                } catch (NumberFormatException e) {
                    // nếu không phải số thì bỏ qua
                }

                if (byOrderCode != null) {
                    predicates.add(cb.or(byOrderCode, byProductName));
                } else {
                    predicates.add(byProductName);
                }
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
    public static Specification<Order> hasStatus(OderStatus status) {
        return (root, query, cb) -> {
            if (status != null) {
                try {
                    return cb.equal(root.get("oderStatus"), status);
                }
                catch (IllegalArgumentException e) {
                    return cb.disjunction(); // Trả về điều kiện luôn sai nếu status không hợp lệ
                }
            }
            return cb.conjunction(); // Trả về điều kiện luôn đúng nếu không có status
        };
    }
    public static Specification<Order> filerOrders(Long id, Long orderCode, OderStatus status, LocalDate startDay, LocalDate endDay) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (orderCode != null) {
                predicates.add(cb.equal(root.get("orderCode"), orderCode));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("oderStatus"), status));
            }
            if (startDay != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("orderDate"), startDay));
            }
            if (endDay != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("orderDate"), endDay));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
