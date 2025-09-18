package com.dailycodework.shopping_cart.DTO.Request;

import com.dailycodework.shopping_cart.Entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ProductUpdateRequest {
    String name;
    String brand;
    BigDecimal price;
    int inventory;
    String description;
    Category category;
}
