package com.dailycodework.shopping_cart.DTO.Request;

import com.dailycodework.shopping_cart.Entity.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
//    Long id;
    String name;
    String brand;
    BigDecimal price;
    int inventory;
    String description;
    Long collectionId;
    Category category;
}
