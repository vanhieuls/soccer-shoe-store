package com.dailycodework.shopping_cart.Entity;

import com.dailycodework.shopping_cart.Enum.PaymentMethod;
import com.dailycodework.shopping_cart.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long orderCode;
    // Mỗi Payment thuộc về một Order
    @OneToOne
    @JoinColumn(name = "order_id")
    Order order;

    // Số tiền thanh toán (có thể <= order.total nếu là trả góp)
    @Column(nullable = false)
    BigDecimal amount;

    BigDecimal amountPaid;

    BigDecimal amountRemaining;
    // Phương thức thanh toán (VNPAY, PayPal, Credit Card, COD...)
    @Enumerated(EnumType.STRING)
    @Column(name = "method", length = 50, nullable = false)
    PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    PaymentStatus status;
//Nội dung giao dịch chuyển khoản
    String description;
//Tên ngân hàng đối ứng (nếu có)
    String counterAccountBankName;
    //Tên chủ tài khoản đối ứng (nếu có)
    String counterAccountName;
//    Số tài khoản đối ứng (nếu có)
    String counterAccountNumber;

    // Mã giao dịch trả về từ cổng thanh toán (nếu có)
    String transactionId;

    // Thời gian tạo payment attempt
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    // Thời gian update trạng thái
    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

}
