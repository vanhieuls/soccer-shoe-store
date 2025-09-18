package com.dailycodework.shopping_cart.DTO.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AuthenticationDto {
    @JsonProperty("access_token")
    String token;
    @JsonProperty("refresh_token")
    String refreshToken;
    boolean authentication;
}
