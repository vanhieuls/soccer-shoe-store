package com.dailycodework.shopping_cart.Helper.ProductSpecification;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private String category;
    private String brand;
    private BigDecimal priceMin;
    private BigDecimal priceMax ;
    private String sort = "asc";
    private String propertySort;
    private Integer page = 0;
    private Integer size = 10;
}
