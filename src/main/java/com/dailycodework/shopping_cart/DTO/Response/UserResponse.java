package com.dailycodework.shopping_cart.DTO.Response;

import com.dailycodework.shopping_cart.DTO.Dto.CartDto;
import com.dailycodework.shopping_cart.DTO.Dto.OrderDto;
import com.dailycodework.shopping_cart.DTO.Dto.ReviewDto;
import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String firstName;
    String lastName;
    String phone;
    String avatar;
    String permanentAddress; // địa chỉ thường trú
    String gender;
    String dateOfBirth;
    String email;
    String username;
//    String password;
    boolean checked;
    int pointVoucher;
//    CartDto cart;
//    List<OrderDto> order;
//    List<ReviewDto> review;
}
