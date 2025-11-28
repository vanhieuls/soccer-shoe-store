package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.cloudinary.Cloudinary;
import com.dailycodework.shopping_cart.DTO.Dto.CollectionDto;
import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.Entity.Collections;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Mapper.CollectionMapper;
import com.dailycodework.shopping_cart.Mapper.ProductMapper;
import com.dailycodework.shopping_cart.Repository.CollectionRepository;
import com.dailycodework.shopping_cart.Repository.ProductRepository;
import com.dailycodework.shopping_cart.Service.Interface.ICollection;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpCollection implements ICollection {
    CollectionRepository collectionRepository;
    ProductMapper productMapper;
    CollectionMapper collectionMapper;
    Cloudinary cloudinary;
    ProductRepository productRepository;
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
        if (collectionRepository.existsByName(name)) {
            throw new AppException(ErrorCode.COLLECTION_EXISTED);
        }
        try {
            String imageUrl;
            // kiểm tra xem có file ảnh không
            if (imageFile != null && !imageFile.isEmpty()) {
                Map data = cloudinary.uploader().upload(imageFile.getBytes(), Map.of());
                imageUrl = data.get("secure_url").toString();
            } else {
                // nếu không có ảnh, bạn có thể set ảnh mặc định hoặc null
                imageUrl = "https://cdn.hstatic.net/themes/200000278317/1001392934/14/slideshow_4.webp?v=23"; // hoặc để null
            }
            Collections collection = Collections.builder()
                    .name(name)
                    .description(description)
                    .imageUrl(imageUrl)
                    .build();
            return collectionMapper.toCollectionDto(collectionRepository.save(collection));
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    @Override
    public Page<CollectionDto> getAllCollections(Integer page, Integer size) {
        Pageable pageable = null;
        if(page == null || page <=0){
            page = 0;
        }
        if(size == null || size <=0){
            size = 10;
        }
        pageable = PageRequest.of(page, size);
        Page<Collections> collections = collectionRepository.findAll(pageable);
        return collections.map(collectionMapper::toCollectionDto);
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

        try {
            // Nếu không gửi ảnh, giữ nguyên ảnh cũ
            if (imageFile != null && !imageFile.isEmpty()) {
                Map data = cloudinary.uploader().upload(imageFile.getBytes(), Map.of());
                collection.setImageUrl(data.get("secure_url").toString());
            }

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

    @Override
    public void deleteProductFromCollection(Long collectionId, Long productId) {
        Collections collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));

        Product product = collection.getProducts().stream().map(p -> {
            if (p.getId().equals(productId)) {
                return p;
            }
            return null;
        }).filter(Objects::nonNull).findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND_IN_COLLECTION));
        product.setCollection(null);
        collection.getProducts().remove(product);
        productRepository.save(product);
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
