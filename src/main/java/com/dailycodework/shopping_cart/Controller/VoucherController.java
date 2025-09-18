package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.VoucherDto;
import com.dailycodework.shopping_cart.DTO.Request.VoucherRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Entity.Voucher;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.Interface.IVoucher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherController {
    IVoucher voucherService;
    @PostMapping("/create")
    public ApiResponse<VoucherDto> createVoucher (@RequestBody VoucherRequest request){
        return ApiResponse.<VoucherDto>builder()
                .code(200)
                .message("create success")
                .result(voucherService.createVoucher(request))
                .build();
    }
    @GetMapping("/getVoucher/{id}")
    public ApiResponse<VoucherDto> getVoucherById (@PathVariable Long id){
        return ApiResponse.<VoucherDto>builder()
                .code(200)
                .message("get voucher success")
                .result(voucherService.getVoucherById(id))
                .build();
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteVoucher(@PathVariable Long id){
        voucherService.deleteVoucher(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("delete success")
                .build();
    }
    @GetMapping("/getAll")
    public ApiResponse<Page<VoucherDto>> getAllVoucher(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,@RequestParam(required = false, defaultValue = "10") Integer pageSize
            ,@RequestParam(required = false, defaultValue = "asc") String sortDir) {

        Page<VoucherDto> voucherDtoList = voucherService.getAllVoucher(pageNumber,pageSize,sortDir);
        if(voucherDtoList.isEmpty()){
            return ApiResponse.<Page<VoucherDto>>builder()
                    .code(200)
                    .message("List voucher is null")
                    .build();
        }
        return ApiResponse.<Page<VoucherDto>>builder()
                .code(200)
                .message("get List voucher success")
                .result(voucherDtoList)
                .build();
    }
    @PutMapping("/update/{id}")
    public ApiResponse<VoucherDto> updateVoucher(@PathVariable Long id,@RequestBody VoucherRequest request) {
        return ApiResponse.<VoucherDto>builder()
                .code(200)
                .message("update success")
                .result(voucherService.updateVoucher(id,request))
                .build();
    }
    @PostMapping("/addVoucher/{userId}/{voucherId}")
    public ApiResponse<Void> addVoucherToUser(@PathVariable Long userId, @PathVariable Long voucherId) {
        voucherService.addVoucherToUser(userId, voucherId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("add voucher success")
                .build();
    }
    @GetMapping("/getVouchersByUserId/{userId}")
    public ApiResponse<List<VoucherDto>> getVouchersByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<VoucherDto>>builder()
                .code(200)
                .message("get vouchers success")
                .result(voucherService.getVouchersByUserId(userId))
                .build();
    }
    @DeleteMapping("/deleteVoucher/{voucherId}")
    public ApiResponse<Void> deleteVoucherId(@PathVariable Long voucherId) {
        voucherService.deleteVoucher(voucherId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("delete voucher success")
                .build();
    }
    @GetMapping("/findByCode/{code}")
    public ApiResponse<VoucherDto> findByCode(@RequestParam String code) {
        return ApiResponse.<VoucherDto>builder()
                .code(200)
                .message("find voucher by code success")
                .result(voucherService.findByCode(code))
                .build();
    }
}
