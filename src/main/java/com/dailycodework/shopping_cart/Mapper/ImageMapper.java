package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Request.ImageDto;
import com.dailycodework.shopping_cart.Entity.Image;
import jakarta.persistence.Lob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Blob;

@Mapper(componentModel = "spring")
public interface ImageMapper {
//    @Mapping(source = "id", target = "imageId")
//    @Mapping(source = "fileName", target = "imageName")
    ImageDto imageDto(Image image);
}
