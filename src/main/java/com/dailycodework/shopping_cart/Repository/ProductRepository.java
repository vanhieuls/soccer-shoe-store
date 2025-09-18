package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.DTO.Request.ProductUpdateRequest;
import com.dailycodework.shopping_cart.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByCategoryName(String category);
    List<Product> findByBrand (String brand);
    List<Product> findByName(String name);
    List<Product> findByCategoryNameAndBrand(String category, String brand);
    List<Product> findByCategoryNameAndName(String category, String name);
    List<Product> findByBrandAndName(String brand, String name);
    Long countByBrandAndName(String brand, String name);
    Page<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);
}
