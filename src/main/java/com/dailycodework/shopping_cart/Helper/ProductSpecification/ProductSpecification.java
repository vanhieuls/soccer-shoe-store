package com.dailycodework.shopping_cart.Helper.ProductSpecification;

import com.dailycodework.shopping_cart.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
//Predicate	là Đại diện một điều kiện WHERE (ví dụ: category=1)
public class ProductSpecification {
//    public static Specification<Product> findProducts(ProductFilter productFilter){
//        return (root, query, cb) ->{
//            List<Predicate> predicates = new ArrayList<>();
//            if(productFilter.getCategory()!=null && !productFilter.getCategory().isEmpty()){
//                predicates.add( cb.equal(root.get("category").get("name"),productFilter.getCategory()));
//            }
//            if(productFilter.getBrand()!=null && !productFilter.getBrand().isEmpty()){
//                predicates.add(cb.equal(root.get("brand"),productFilter.getBrand()));
//            }
//            if (productFilter.getPriceMin() != null) {
//                predicates.add( cb.greaterThanOrEqualTo(root.get("price"), productFilter.getPriceMin()));
//            }
//            if (productFilter.getPriceMax() != null) {
//                predicates.add(cb.lessThanOrEqualTo(root.get("price"), productFilter.getPriceMax()));
//            }
//            return cb.and(predicates.toArray(new Predicate[0]));
//        };
//    }
    public static Specification<Product> findProducts(
            String category,
            String brand,
            BigDecimal priceMin,
            BigDecimal priceMax
    ) {
        return (root, query, cb) -> {
    //        Key: predicates là bước trung gian dễ dàng thêm hoặc bỏ điều kiện, sau đó
    //        mới kết hợp tất cả để trả về một Predicate duy nhất từ toPredicate.
            List<Predicate> predicates = new ArrayList<>();
    //        Mỗi phần tử trong list là một Predicate, tức là một điều kiện WHERE trong SQL.
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category").get("id"), category));
            }
            if (brand != null && !brand.isEmpty()) {
                predicates.add(cb.equal(root.get("brand"), brand));
            }
            if (priceMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), priceMin));
            }
            if (priceMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), priceMax));
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
