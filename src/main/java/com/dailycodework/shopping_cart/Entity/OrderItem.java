package com.dailycodework.shopping_cart.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.security.cert.CertPathBuilder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name="order_id")
    Order order;
    @ManyToOne
    @JoinColumn(name="product_id")
    Product product;
    int quantity;
    BigDecimal price;
    @ManyToOne
    @JoinColumn(name="product_size_id")
    ProductSize productSize;

}
