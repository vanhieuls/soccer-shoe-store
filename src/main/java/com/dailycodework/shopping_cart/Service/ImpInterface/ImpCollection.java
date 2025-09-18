package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.cloudinary.Cloudinary;
import com.dailycodework.shopping_cart.DTO.Dto.CollectionDto;
import com.dailycodework.shopping_cart.DTO.Request.CollectionRequest;
import com.dailycodework.shopping_cart.DTO.Request.ImageDto;
import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.Entity.Collections;
import com.dailycodework.shopping_cart.Entity.Image;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Mapper.CollectionMapper;
import com.dailycodework.shopping_cart.Mapper.ProductMapper;
import com.dailycodework.shopping_cart.Repository.CollectionRepository;
import com.dailycodework.shopping_cart.Service.Interface.ICollection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpCollection implements ICollection {
    CollectionRepository collectionRepository;
    ProductMapper productMapper;
    CollectionMapper collectionMapper;
    Cloudinary cloudinary;
    @Override
    public List<ProductResponse> getAllProductsInCollection(Long collectionId) {
        return collectionRepository.findById(collectionId)
                .map(Collections::getProducts)
                .map(productMapper::toProductResponses)
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));
    }

    @Override
    public void deleteCollection(Long collectionId) {
        if (!collectionRepository.existsById(collectionId)) {
                throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);
        }
        collectionRepository.deleteById(collectionId);
    }

    @Override
    public CollectionDto createCollection(String name, String description, MultipartFile imageFile) {
        try {
            Map data = this.cloudinary.uploader().upload(imageFile.getBytes(), Map.of());
            if (collectionRepository.existsByName(name)) {
                throw new AppException(ErrorCode.COLLECTION_EXISTED);
            }
            Collections collection = Collections.builder()
                    .name(name)
                    .description(description)
                    .imageUrl(data.get("secure_url").toString())
                    .build();
            return collectionMapper.toCollectionDto(collectionRepository.save(collection));
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }



    @Override
    public List<CollectionDto> getAllCollections() {
        return collectionRepository.findAll()
                .stream()
                .map(collectionMapper::toCollectionDto)
                .toList();
    }

    @Override
    public CollectionDto getCollectionById(Long collectionId) {
        return collectionRepository.findById(collectionId)
                .map(collectionMapper::toCollectionDto)
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));
    }

    @Override
    public CollectionDto updateCollection(String name, String description, MultipartFile imageFile, Long collectionId) {
        Collections collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));
//        if (collectionRepository.existsByName(request.getName()) && !collection.getName().equals(request.getName())) {
//            throw new AppException(ErrorCode.COLLECTION_EXISTED);
//        }
        try {
            Map data = this.cloudinary.uploader().upload(imageFile.getBytes(), Map.of());
            collection.setImageUrl(data.get("secure_url").toString());
            collection.setName(name);
            collection.setDescription(description);
            Collections updatedCollection = collectionRepository.save(collection);
            return collectionMapper.toCollectionDto(updatedCollection);
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    @Override
    public List<CollectionDto> getCollectionByName(String name) {
        List<Collections> collections = collectionRepository.SearchByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));
        return collectionMapper.toCollectionDtos(collections);
    }


//    @Override
//    public Collections getCollectionById(Long collectionId) {
//        return collectionRepository.findById(collectionId)
//                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));
//    }
//
//    @Override
//    public List<Collections> getAllCollection() {
//        if (collectionRepository.findAll().isEmpty()) {
//            throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);
//        }
//        return collectionRepository.findAll();
//    }


}
