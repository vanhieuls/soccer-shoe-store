package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.SizeDto;
import com.dailycodework.shopping_cart.DTO.Request.SizeRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.Size;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.Interface.ISize;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/size")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Size")
public class SizeController {
    ISize iSizeService;
    @GetMapping("/get-all-size")
    public ApiResponse<List<SizeDto>> getAllSizes() {
        List<SizeDto> sizes = iSizeService.getAllSizes();
        if(sizes.isEmpty()) {
            throw new AppException(ErrorCode.SIZE_EMPTY);
        }
        return ApiResponse.<List<SizeDto>>builder()
                .code(200)
                .message("Get all sizes successfully")
                .result(sizes)
                .build();
    }
    @GetMapping("/get-size-by-id/{id}")
    public ApiResponse<SizeDto> getSizeById(@PathVariable Long id) {
        SizeDto sizeDto = iSizeService.getSizeById(id);
        return ApiResponse.<SizeDto>builder()
                .code(200)
                .message("Get size by id successfully")
                .result(sizeDto)
                .build();
    }
    @PostMapping("/create-size")
    public ApiResponse<SizeDto> createSize( @RequestBody @Valid SizeRequest request) {
        SizeDto sizeDto = iSizeService.createSize(request);
        return ApiResponse.<SizeDto>builder()
                .code(200)
                .message("Create size successfully")
                .result(sizeDto)
                .build();
    }
    @DeleteMapping("/delete-size/{id}")
    public ApiResponse<Void> deleteSize(@PathVariable Long id) {
        iSizeService.deleteSize(id);
        log.info("Deleted size with id: {}", id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete size successfully")
                .build();
    }
    @PutMapping("/update-size/{id}")
    public ApiResponse<SizeDto> updateSize(@PathVariable Long id,@RequestBody @Valid SizeRequest request) {
        SizeDto sizeDto = iSizeService.updateSize(id, request);
        return ApiResponse.<SizeDto>builder()
                .code(200)
                .message("Update size successfully")
                .result(sizeDto)
                .build();
    }

}
