package com.dailycodework.shopping_cart.DTO.Request;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CollectionRequest {
    private String name;
    private String description;
//    private String imageUrl;
    // Additional fields can be added as needed
}
