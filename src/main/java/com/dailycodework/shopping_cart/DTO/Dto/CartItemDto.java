package com.dailycodework.shopping_cart.DTO.Dto;

import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Entity.ProductSize;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemDto {
    Long id;
    int quantity;
    BigDecimal unitPrice;
    BigDecimal totalPrice;
    ProductResponse product;
    Long productSizeId;
    boolean selected;

}
