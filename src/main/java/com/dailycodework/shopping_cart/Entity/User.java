package com.dailycodework.shopping_cart.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String firstName;
    String lastName;
    @NotEmpty(message = "Please provide your phone number")
    @Pattern(regexp = "^(0[0-9]{9})$", message = "Invalid phone number format")
    String phone;
    String avatar;
    String permanentAddress; // địa chỉ thường trú
    String gender;
    @NotEmpty(message = "Please provide your date of birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    String dateOfBirth;
    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "Please provide an email")
    String email;
    @NotEmpty(message = "Please provide your name")
    String username;
    @Size(min = 5, message = "Your password must have at least 5 characters")
    @NotEmpty(message = "Please provide your password")
    String password;
    boolean checked;
    String verificationCode;
    LocalDateTime verificationExpiresAt;
    String resetPasswordToken;
    LocalDateTime resetPasswordExpiry;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Cart cart;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Order> order;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Review> review;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Address> address;
    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    Set<Voucher> vouchers; // Danh sách voucher đã sử dụng
    int pointVoucher; // Điểm tích lũy từ voucher
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
