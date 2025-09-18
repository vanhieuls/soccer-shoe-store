package com.dailycodework.shopping_cart.DTO.Request;

import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
//    Long id;
//    String firstName;
//    String lastName;
//    String email;
//    String username;
//    String password;
    String firstName;
    String lastName;
    @NotEmpty(message = "Please provide your phone number")
    @Pattern(regexp = "^(0[0-9]{9})$", message = "Invalid phone number format")
    String phone;
    String avatar;
    String permanentAddress; // địa chỉ thường trú
    String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    String dateOfBirth;
    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "Please provide an email")
    String email;
    @NotEmpty(message = "Please provide your name")
    String username;
//    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username chỉ được chứa chữ cái và số")
    @Size(min = 5, message = "Your password must have at least 5 characters")
    @NotEmpty(message = "Please provide your password")
    String password;
//    @Size - Kiểm tra kích thước (chuỗi, collection, mảng) (độ dài)
//    @Min / @Max - Kiểm tra giá trị số tối thiểu/tối đa vis duj độ tuổi >18
}
