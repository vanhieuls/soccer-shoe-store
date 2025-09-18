package com.dailycodework.shopping_cart.DTO.Dto;

import com.dailycodework.shopping_cart.Entity.Order;
import com.dailycodework.shopping_cart.Entity.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemDto {
    Long productId;
    Long productSizeId;
    String productName;
    int quantity;
    BigDecimal price;
}
