package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.ProductsizeDto;
import com.dailycodework.shopping_cart.DTO.Request.ProductSizeRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.ProductSize;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.Interface.IProductSize;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-size")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductSizeController {
    IProductSize productSizeService;
    @GetMapping("/{productId}")
    public ApiResponse<List<ProductsizeDto>> findByProductId(@PathVariable Long productId) {
        List<ProductsizeDto> productSizes = productSizeService.findByProductId(productId);
        if (productSizes.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND);
        }
        return ApiResponse.<List<ProductsizeDto>>builder()
                .code(200)
                .message("Product sizes retrieved successfully")
                .result(productSizes)
                .build();
    }
    @GetMapping("/get-all")
    public ApiResponse<List<ProductsizeDto>> getAll() {
        List<ProductsizeDto> productSizes = productSizeService.getAll();
        if (productSizes.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND);
        }
        return ApiResponse.<List<ProductsizeDto>>builder()
                .code(200)
                .message("All product sizes retrieved successfully")
                .result(productSizeService.getAll())
                .build();
    }
    @GetMapping("/get-by-id/{id}")
    public ApiResponse<ProductsizeDto> getById(@PathVariable Long id) {
        ProductsizeDto productSizeDto = productSizeService.getById(id);
        return ApiResponse.<ProductsizeDto>builder()
                .code(200)
                .message("Product size retrieved successfully")
                .result(productSizeDto)
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<ProductsizeDto> createProductSize(@RequestBody @Valid ProductSizeRequest productSize) {
        ProductsizeDto productsizeDto = productSizeService.createProductSize(productSize);
        return ApiResponse.<ProductsizeDto>builder()
                .code(200)
                .message("Product size created successfully")
                .result(productsizeDto)
                .build();
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteProductSize(@PathVariable Long id) {
        productSizeService.deleteProductSize(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Product size deleted successfully")
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<ProductsizeDto> update(@PathVariable Long id, @RequestBody @Valid ProductSizeRequest ps) {
        ProductsizeDto updatedProductSize = productSizeService.update(id, ps);
        return ApiResponse.<ProductsizeDto>builder()
                .code(200)
                .message("Product size updated successfully")
                .result(updatedProductSize)
                .build();
    }

}
