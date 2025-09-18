package com.dailycodework.shopping_cart.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String addressLine; // số nhà, tên đường
    String wardCommune; // phường/xã
    String state;        // tỉnh/bang
    String postalCode;   // mã bưu chính
    String country;      // quốc gia
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;          // người dùng sở hữu địa chỉ này
}