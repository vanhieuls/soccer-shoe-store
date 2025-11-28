package com.dailycodework.shopping_cart.DTO.Dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductsizeDto {
    Long id;
    Long productId;
    Long sizeId;
    Long sizeName;
    int quantity;
}
