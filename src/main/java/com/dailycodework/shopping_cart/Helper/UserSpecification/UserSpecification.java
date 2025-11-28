package com.dailycodework.shopping_cart.Helper.UserSpecification;

import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Entity.Role;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Enum.Roles;
import com.dailycodework.shopping_cart.Helper.Specification.SpecSearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserSpecification{
    public static Specification<User> findProducts(
            String name,
            String id,
            Boolean checked,
            String email

    ) {
        return (root, query, cb) -> {
            //        Key: predicates là bước trung gian dễ dàng thêm hoặc bỏ điều kiện, sau đó
            //        mới kết hợp tất cả để trả về một Predicate duy nhất từ toPredicate.
            List<Predicate> predicates = new ArrayList<>();
            //        Mỗi phần tử trong list là một Predicate, tức là một điều kiện WHERE trong SQL.
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.equal(root.get("username"), name));
            }
            if (id != null && !id.isEmpty()) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (checked != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("checked"), checked));
            }
            if (email != null && !email.isEmpty()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("email"), email));
            }
            //        cb.and(...) nhận một mảng Predicate và trả về một Predicate duy nhất,
            //        mà đó chính là Predicate tổng hợp của tất cả điều kiện.
            //        new Predicate[0] chỉ để xác định kiểu mảng (Predicate[]).
            //   vis dụ:     List<String> list = List.of("A", "B", "C");
            //String[] arr = list.toArray(new String[0]); // Java tạo mảng mới có size=3
            //        Nếu dùng new String[3] cũng được, nhưng [0] tiện hơn, không cần tính size.
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
//    public static Specification<User> getUserManagers(){
//        return (root, query, cb) ->{
//            List<Predicate> predicates = new ArrayList<>();
//            predicates.add(cb.equal(root.get("roles").get("name"), "ROLE_ADMIN"));
//            predicates.add(cb.equal(root.get("roles").get("name"), "ROLE_STAFF"));
//            return cb.or(predicates.toArray(new Predicate[0]));
//        };
//    }
    public static Specification<User> hasAnyRole(Set<String> roleNames) {
        return (root, query, cb) -> {
            // để Spring Data tạo count query đúng khi phân trang
            query.distinct(true);

            Join<User, Role> roles = root.join("roles", JoinType.INNER);
            CriteriaBuilder.In<String> in = cb.in(roles.get("name"));
            roleNames.forEach(in::value);
            return in; // tương đương WHERE r.name IN (:roleNames)
        };
    }
    public static Specification<User> hasRole(Roles role) { // Roles = enum của bạn
        return (root, query, cb) -> {
            if (role == null) return cb.conjunction();
            Join<User, Role> j = root.join("roles", JoinType.INNER);
            return cb.equal(j.get("name"), role); // name là enum: @Enumerated(EnumType.STRING)
        };
    }
    public static Specification<User> nameContains(String username) {
        return (root, query, cb) -> {
            if (username == null || username.trim().isEmpty()) return cb.conjunction();
            String like = "%" + username.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("username")), like);
        };
    }

    public static Specification<User> emailContains(String email) {
        return (root, query, cb) -> {
            if (email == null || email.trim().isEmpty()) return cb.conjunction();
            String like = "%" + email.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("email")), like);
        };
    }

    // kết hợp tất cả
    public static Specification<User> filterManagers(Roles roleNames, String name, String email) {
        return Specification
                .where(hasRole(roleNames))
                .and(nameContains(name))
                .and(emailContains(email));
    }
}
