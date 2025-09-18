package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.cloudinary.Cloudinary;
import com.dailycodework.shopping_cart.DTO.Request.ImageDto;
import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.Entity.Image;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Mapper.ProductMapper;
import com.dailycodework.shopping_cart.Repository.ImageRepository;
import com.dailycodework.shopping_cart.Service.Interface.IImage;
import com.dailycodework.shopping_cart.Service.Interface.IProduct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpImage implements IImage {
    Cloudinary cloudinary;
    ImageRepository imageRepository;
    IProduct productService;
    ProductMapper productMapper;
    @Override
    public Image getImageByID(Long id) {
        return imageRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.IMAGE_NOT_FOUND));
    }

    @Override
    public void deleteImageById(Long id) {
//        imageRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.IMAGE_NOT_FOUND));
//        imageRepository.deleteById(id);
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,
                () ->{ throw new AppException(ErrorCode.IMAGE_NOT_FOUND);});
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        ProductResponse product = productService.getProductById(productId);
        List<ImageDto> imageDtos = new ArrayList<>();
        for(MultipartFile file : files){
            try{
                Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
                Image image = Image.builder()
                        .fileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .downloadUrl(data.get("secure_url").toString())
                        .image(new SerialBlob(file.getBytes()))
                        .product(productMapper.responseToProduct(product))
                        .build();
                Image savedImage = imageRepository.save(image);
                ImageDto imageDto = ImageDto.builder()
                        .imageId(savedImage.getId())
                        .imageName(savedImage.getFileName())
                        .downloadUrl(savedImage.getDownloadUrl())
                        .build();
                imageDtos.add(imageDto);
            }catch (IOException io){
                throw new RuntimeException("Image upload fail");
            } catch (SerialException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
//            try{
//                Image image = new Image();
//                image.setFileName(fille.getOriginalFilename());
//                image.setFileType(fille.getContentType());
//                image.setImage(new SerialBlob(fille.getBytes()));
//                image.setProduct(productMapper.responseToProduct(product));
//                String buildDownloadUrl ="api/v1/images/download/";
//                String downloadUrl = buildDownloadUrl + image.getId();
//                image.setDownloadUrl(downloadUrl);
//                Image savedImage = imageRepository.save(image);
//                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
//                imageRepository.save(savedImage);
//                ImageDto imageDto = ImageDto.builder()
//                        .imageId(savedImage.getId())
//                        .imageName(savedImage.getFileName())
//                        .downloadUrl(savedImage.getDownloadUrl())
//                        .build();
//                imageDtos.add(imageDto);
//                ImageDto imageDto = ImageDto.builder()
//                        .imageId(image.getId())
//                        .imageName(image.getFileName())
//                        .downloadUrl(image.getDownloadUrl())
//                        .build();
//                imageDtos.add(imageDto);
//            }
//            catch (SQLException |IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
        return imageDtos;

    }

    @Override
    public ImageDto updateImage(MultipartFile file, Long imageId) {
        Image image = getImageByID(imageId);
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setDownloadUrl(data.get("secure_url").toString());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
            ImageDto imageDto = ImageDto.builder()
                    .imageId(image.getId())
                    .imageName(image.getFileName())
                    .downloadUrl(image.getDownloadUrl())
                    .build();
            return imageDto;
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
