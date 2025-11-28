package com.dailycodework.shopping_cart.DTO.Dto;

import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDto {
    Long id;
    Long productId;
    Long userId;
    String username;
    String avatarUser;
    int rating;
    String comment;
    LocalDateTime createAt;
}

