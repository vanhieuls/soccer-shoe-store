package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.CollectionDto;
import com.dailycodework.shopping_cart.DTO.Request.CollectionRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.Entity.Collections;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.ImpInterface.ImpCollection;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/collection")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CollectionController {
    ImpCollection impCollection;
    @GetMapping("/get-all-products-by-collection-id/{collectionId}")
    public ApiResponse<List<ProductResponse>> getAllProductsInCollection(@PathVariable Long collectionId) {
        List<ProductResponse> products = impCollection.getAllProductsInCollection(collectionId);
        return ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("Get all products in collection success")
                .result(products)
                .build();
    }
    @DeleteMapping("/delete-collection/{collectionId}")
    public ApiResponse<Void> deleteCollection(@PathVariable Long collectionId) {
        impCollection.deleteCollection(collectionId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Collection deleted successfully")
                .build();
    }
    @PostMapping("/create-collection")
    public ApiResponse<CollectionDto> createCollection(@RequestParam String name, @RequestParam String description, @RequestParam(required = false) MultipartFile imageFile) {
        return ApiResponse.<CollectionDto>builder()
                .code(200)
                .message("Collection created successfully")
                .result(impCollection.createCollection(name, description, imageFile))
                .build();
    }
    @GetMapping("/get-all")
    public ApiResponse<List<CollectionDto>> getAllCollections() {
        return ApiResponse.<List<CollectionDto>>builder()
                .code(200)
                .message("Get all collections success")
                .result(impCollection.getAllCollections())
                .build();
    }

//    @GetMapping("/get-collection-by-id/{collectionId}")
//    public ApiResponse<Collections> getCollectionById(@PathVariable Long collectionId) {
//        return ApiResponse.<Collections>builder()
//                .code(200)
//                .message("Get collection by ID success")
//                .result(impCollection.getCollectionById(collectionId))
//                .build();
//    }
//
//    @GetMapping("/get-all-collections")
//    public ApiResponse<List<Collections>> getAllCollection() {
//        return ApiResponse.<List<Collections>>builder()
//                .code(200)
//                .message("Get all collections success")
//                .result(impCollection.getAllCollection())
//                .build();
//    }

    @PutMapping("/update-collection/{collectionId}")
    public ApiResponse<CollectionDto> updateCollection(@RequestParam String name,@RequestParam String description ,@RequestParam(required = false) MultipartFile file,@PathVariable Long collectionId) {
        return ApiResponse.<CollectionDto>builder()
                .code(200)
                .message("Collection updated successfully")
                .result(impCollection.updateCollection(name, description,file,collectionId))
                .build();
    }
    @GetMapping("/get-collection-by-id/{collectionId}")
    public ApiResponse<CollectionDto> getCollectionById(@PathVariable Long collectionId) {
        CollectionDto collection = impCollection.getCollectionById(collectionId);
        if (collection == null) {
            throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);
        }
        return ApiResponse.<CollectionDto>builder()
                .code(200)
                .message("Get collection by ID success")
                .result(collection)
                .build();
    }
    @GetMapping("/get-collection-by-name")
    public ApiResponse<List<CollectionDto>> getCollectionByName(@RequestParam String name) {
        List<CollectionDto> collection = impCollection.getCollectionByName(name);
        if (collection == null) {
            throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);
        }
        return ApiResponse.<List<CollectionDto>>builder()
                .code(200)
                .message("Get collection by name success")
                .result(collection)
                .build();
    }
}
