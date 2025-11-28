package com.dailycodework.shopping_cart.Entity;

import com.dailycodework.shopping_cart.Enum.OderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @CreationTimestamp
    LocalDateTime orderDate;
    BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    OderStatus oderStatus;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 50)
    Set<OrderItem> orderItems = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
//    @ManyToOne
//    @JoinColumn(name = "voucher_id")
//    Voucher voucher;
    BigDecimal discountApplied;
    BigDecimal shippingFee = BigDecimal.valueOf(20000);
    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;
    @Column(unique = true, nullable = false, updatable = false)
    Long orderCode;
    String shippingAddress;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    Payment payment;
    @PrePersist
    public void generateOrderCode() {
        if (this.orderCode == null || this.orderCode==0) {
//            this.orderCode = "ORD-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
            this.orderCode = System.currentTimeMillis() + (int)(Math.random() * 1000);
        }
    }
}
