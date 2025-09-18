package com.dailycodework.shopping_cart.DTO.Request;

import com.dailycodework.shopping_cart.Entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AddressRequest {
    String addressLine; // số nhà, tên đường
    String wardCommune; // phường/xã
    String state;        // tỉnh/bang
    String postalCode;   // mã bưu chính
    String country;      // quốc gia
    Long userId;          // ID người dùng sở hữu địa chỉ này
}
