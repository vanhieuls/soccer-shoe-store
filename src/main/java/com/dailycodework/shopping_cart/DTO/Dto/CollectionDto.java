package com.dailycodework.shopping_cart.DTO.Dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionDto {
    Long id;
    String name;
    String description;
    String imageUrl;
    // Additional fields can be added as needed
}
