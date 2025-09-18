package com.dailycodework.shopping_cart.DTO.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSizeRequest {
    @NotNull(message = "Product ID cannot be null")
    Long productId;
    @NotNull(message = "Size ID cannot be null")
    Long sizeId;
//    @NotNull(message = "Color cannot be empty")
    int quantity;
}
