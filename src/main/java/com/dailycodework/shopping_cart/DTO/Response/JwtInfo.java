package com.dailycodework.shopping_cart.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtInfo {
    String jwtId;
    Date issuedAt;
    Date expiredTime;
}
