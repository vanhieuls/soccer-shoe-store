package com.dailycodework.shopping_cart.DTO.Request;

import com.dailycodework.shopping_cart.Entity.Order;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherRequest {
    String code;
    BigDecimal discountAmount;
    boolean percentTage;
    int usageLimit;
    int usedCount;
    LocalDateTime startDate;
    LocalDateTime endDate;
    boolean active;
    BigDecimal minOrderAmount;      // ✅ Đơn hàng tối thiểu
    BigDecimal maxDiscountAmount;   // ✅ Mức giảm tối đa (rất quan trọng)
    int pointRequired; // Số điểm cần thiết để sử dụng voucher
}
