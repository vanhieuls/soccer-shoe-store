package com.dailycodework.shopping_cart.Helper.UserSpecification;

import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Helper.Specification.SpecSearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
}
