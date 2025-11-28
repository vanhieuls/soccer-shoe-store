package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.AddressDto;
import com.dailycodework.shopping_cart.DTO.Request.AddressRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.Address;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.ImpInterface.ImpAddress;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/address")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Address")
public class AddressController {
    ImpAddress addressService;
    @GetMapping("/get-all")
    @Operation(
            summary = "Lấy toàn bộ địa chỉ"
    )
    public ApiResponse<List<AddressDto>> getAllAddresses() {
        return ApiResponse.<List<AddressDto>>builder()
                .code(200)
                .message("Get all addresses successfully")
                .result(addressService.getAllAddresses())
                .build();
    }
    @Operation(
            summary = "Lấy địa chỉ theo userId"
    )
    @GetMapping("/get-by-user/{userId}")
    public ApiResponse<List<AddressDto>> getAddressesByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<AddressDto>>builder()
                .code(200)
                .message("Get addresses by user id successfully")
                .result(addressService.getAddressesByUserId(userId))
                .build();
    }
    @Operation(
            summary = "Lấy địa chỉ theo id"
    )
    @GetMapping("/get-by-id/{id}")
    public ApiResponse<AddressDto> getAddressById(@PathVariable Long id) {
        return ApiResponse.<AddressDto>builder()
                .code(200)
                .message("Get address by id successfully")
                .result(addressService.getAddressById(id))
                .build();
    }
    @Operation(
            summary = "Tạo địa chỉ"
    )
    @PostMapping("/create")
    public ApiResponse<AddressDto> createAddress(@RequestBody AddressRequest request) {
        return ApiResponse.<AddressDto>builder()
                .code(200)
                .message("Create address successfully")
                .result(addressService.createAddress(request))
                .build();
    }
    @Operation(
            summary = "Cập nhật địa chỉ"
    )
    @PutMapping("/update/{id}")
    public ApiResponse<AddressDto> updateAddress(@PathVariable Long id,@RequestBody AddressRequest request) {
        return ApiResponse.<AddressDto>builder()
                .code(200)
                .message("Update address successfully")
                .result(addressService.updateAddress(id, request))
                .build();
    }
    @Operation(
            summary = "Xoá địa chỉ"
    )
    @DeleteMapping("/delete/{userId}/{id}")
    public ApiResponse<Void> deleteAddress(@PathVariable Long userId, @PathVariable Long id) {
        addressService.deleteAddress(userId,id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete address successfully")
                .build();
    }
}
