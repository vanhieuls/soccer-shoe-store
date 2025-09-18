package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Request.ImageDto;
import com.dailycodework.shopping_cart.Entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImage {
    Image getImageByID(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImage(List <MultipartFile> file, Long productId);
    ImageDto updateImage(MultipartFile file, Long imageId);
}
