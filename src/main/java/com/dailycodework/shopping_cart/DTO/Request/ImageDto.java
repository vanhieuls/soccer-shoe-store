package com.dailycodework.shopping_cart.DTO.Request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDto {
    private Long imageId;
    private String imageName;
    private String downloadUrl;
}
