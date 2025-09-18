package com.dailycodework.shopping_cart.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
public record ChangePasswordRequest (
    String oldPassword,
    String newPassword,
    String confirmPassword
){}
