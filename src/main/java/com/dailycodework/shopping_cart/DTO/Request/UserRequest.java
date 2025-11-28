package com.dailycodework.shopping_cart.DTO.Request;

import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.*;
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
//    @PhoneNumber(message = "phone invalid format") // custom annotation
    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Pattern(
            regexp = "^(0(3|5|7|8|9)\\d{8})$",
            message = "Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng 03, 05, 07, 08 hoặc 09 (VN)"
    )
    String phone;
    String avatar;
    String permanentAddress; // địa chỉ thường trú
    String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Pattern(
            regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
            message = "Ngày sinh phải theo định dạng yyyy-MM-dd (ví dụ 2000-12-31)"
    )
    String dateOfBirth;
    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "Please provide an email")
    String email;
    @NotEmpty(message = "Please provide your name")
    String username;
//    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username chỉ được chứa chữ cái và số")
    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).+$",
            message = "Mật khẩu phải có ít nhất 1 chữ hoa và 1 ký tự đặc biệt"
    )
    String password;
//    @Size - Kiểm tra kích thước (chuỗi, collection, mảng) (độ dài)
//    @Min / @Max - Kiểm tra giá trị số tối thiểu/tối đa vis duj độ tuổi >18
}
