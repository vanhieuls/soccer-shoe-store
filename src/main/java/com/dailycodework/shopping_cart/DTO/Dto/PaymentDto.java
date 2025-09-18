package com.dailycodework.shopping_cart.DTO.Dto;

import com.dailycodework.shopping_cart.Entity.Order;
import com.dailycodework.shopping_cart.Enum.PaymentMethod;
import com.dailycodework.shopping_cart.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
    Long id;
    String orderCode;
    BigDecimal amount;
    BigDecimal amountPaid;
    BigDecimal amountRemaining;
    PaymentMethod method;
    PaymentStatus status;
    String description;
    String counterAccountBankName;
    String counterAccountName;
    String counterAccountNumber;
    String transactionId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
