package com.dailycodework.shopping_cart.DTO.Dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressDto {
    Long id;
    String addressLine; // số nhà, tên đường
    String wardCommune; // phường/xã
    String state;        // tỉnh/bang
    String postalCode;   // mã bưu chính
    String country;      // quốc gia
    Long userId;          // ID người dùng sở hữu địa chỉ này
}
