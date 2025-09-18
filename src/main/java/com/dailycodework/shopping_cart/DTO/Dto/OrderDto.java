package com.dailycodework.shopping_cart.DTO.Dto;

import com.dailycodework.shopping_cart.Entity.OrderItem;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    Long id;
    Long userId;
    LocalDateTime orderDate;
    BigDecimal totalAmount;
    OderStatus oderStatus;
    List<OrderItemDto> orderItems;
    BigDecimal discountApplied;
    LocalDateTime createdAt;
    String orderCode;
    String shippingAddress;
    PaymentDto payment;
//    Long voucherId;
}
