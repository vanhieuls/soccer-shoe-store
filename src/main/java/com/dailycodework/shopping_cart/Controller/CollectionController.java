package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.CollectionDto;
import com.dailycodework.shopping_cart.DTO.Request.CollectionRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.Entity.Collections;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.ImpInterface.ImpCollection;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Operation(
            summary = "Lấy tất cả sản phẩm trong bộ sưu tập theo collectionId"
    )
    @GetMapping("/get-all-products-by-collection-id/{collectionId}")
    public ApiResponse<List<ProductResponse>> getAllProductsInCollection(@PathVariable Long collectionId) {
        List<ProductResponse> products = impCollection.getAllProductsInCollection(collectionId);
        return ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("Get all products in collection success")
                .result(products)
                .build();
    }
    @Operation(
            summary = "Xóa bộ sưu tập theo collectionId / dành cho admin và staff"
    )
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @DeleteMapping("/delete-collection/{collectionId}")
    public ApiResponse<Void> deleteCollection(@PathVariable Long collectionId) {
        impCollection.deleteCollection(collectionId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Collection deleted successfully")
                .build();
    }
    @Operation(
            summary = "Tạo mới bộ sưu tập / dành cho admin và staff"
    )
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping(value ="/create-collection", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CollectionDto> createCollection(@RequestParam String name, @RequestParam String description, @RequestParam(required = false) MultipartFile imageFile) {
        return ApiResponse.<CollectionDto>builder()
                .code(200)
                .message("Collection created successfully")
                .result(impCollection.createCollection(name, description, imageFile))
                .build();
    }
    @Operation(
            summary = "Lấy tất cả bộ sưu tập"
    )
    @GetMapping("/get-all")
    public ApiResponse<Page<CollectionDto>> getAllCollections(@RequestParam(required = false, defaultValue = "10") Integer size,
                                                              @RequestParam(required = false, defaultValue = "0") Integer page) {
        return ApiResponse.<Page<CollectionDto>>builder()
                .code(200)
                .message("Get all collections success")
                .result(impCollection.getAllCollections(page, size))
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
    @Operation(
            summary = "Cập nhật bộ sưu tập / dành cho admin vs staff "
    )
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping(value = "/update-collection/{collectionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CollectionDto> updateCollection(@RequestParam String name,@RequestParam String description ,@RequestParam(required = false) MultipartFile imageFile,@PathVariable Long collectionId) {
        return ApiResponse.<CollectionDto>builder()
                .code(200)
                .message("Collection updated successfully")
                .result(impCollection.updateCollection(name, description,imageFile,collectionId))
                .build();
    }
    @Operation(
            summary = "Lấy bộ sưu tập theo collectionId"
    )
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
    @Operation(
            summary = "Lấy bộ sưu tập theo tên"
    )
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
    @DeleteMapping("/delete-product-from-collection/{collectionId}/{productId}")
    public ApiResponse<Void> deleteProductFromCollection(@PathVariable Long collectionId, @PathVariable Long productId) {
        try {
            impCollection.deleteProductFromCollection(collectionId, productId);
            return ApiResponse.<Void>builder()
                    .code(200)
                    .message("Product removed from collection successfully")
                    .build();
        }
        catch (AppException e) {
            log.error("Error removing product from collection: {}", e.getMessage());
            throw e;
        }
    }
}
