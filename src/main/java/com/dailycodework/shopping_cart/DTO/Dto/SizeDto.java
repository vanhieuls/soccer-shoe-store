package com.dailycodework.shopping_cart.DTO.Dto;

import com.dailycodework.shopping_cart.Entity.ProductSize;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeDto {
    Long id;
    int size;
//    List<ProductsizeDto> productSizes;
}
