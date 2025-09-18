package com.dailycodework.shopping_cart.DTO.Dto;

import com.dailycodework.shopping_cart.Entity.CartItem;
import com.dailycodework.shopping_cart.Entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDto {
    Long id;
    BigDecimal totalAmount;
    Set<CartItemDto> cartItems;
}
