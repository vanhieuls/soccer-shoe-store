package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Request.ImageDto;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.Image;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Service.Interface.IImage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageController {
    IImage imageService;
    @DeleteMapping("/delete/{imageID}")
    public ApiResponse<Void> deleteImage(@PathVariable Long imageID){
        try {
            imageService.deleteImageById(imageID);
            return ApiResponse.<Void>builder().code(200).message("Delete success!").build();
        } catch (AppException e) {
            return ApiResponse.<Void>builder().code(401).message(e.getErrorCode().getMessage()).build();
        }
    }
    @PostMapping("/upload")
    public ApiResponse<List<ImageDto>> saveImages(@RequestParam List<MultipartFile> files,@RequestParam Long productId){
        try {
            List<ImageDto> result = imageService.saveImage(files, productId);
            return ApiResponse.<List<ImageDto>>builder()
                    .code(200)
                    .message("Upload success")
                    .result(result)
                    .build();
        }
        catch (Exception e){
            return ApiResponse.<List<ImageDto>>builder().message("Upload failed!").code(401).build();
        }
    }
    @PutMapping("/update/{imageId}")
    public ApiResponse<ImageDto> updateImage(@RequestParam MultipartFile file,@PathVariable Long imageId){
        Image image = imageService.getImageByID(imageId);
        if(image!=null){
            ImageDto image1 = imageService.updateImage(file,imageId);
            return ApiResponse.<ImageDto>builder().code(200).message("Update success!").result(image1).build();
        }
        return ApiResponse.<ImageDto>builder().code(401).message("Update failed!").build();
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) throws SQLException{
        Image image = imageService.getImageByID(id);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+image.getFileName()+"\"")
                .body(resource);
    }

}
