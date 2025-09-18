package com.dailycodework.shopping_cart.DTO.Request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {
    Long productId;
    Long userId;
    int rating;
    @Size(min = 30, message = "comment must be at least 30 characters")
    String comment;
}
