package com.dailycodework.shopping_cart.Controller;

import com.cloudinary.Api;
import com.dailycodework.shopping_cart.DTO.Dto.PermissionDto;
import com.dailycodework.shopping_cart.DTO.Request.PermissionRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.Permission;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.Interface.IPermission;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    IPermission permissionService;
    @PostMapping()
    public ApiResponse<PermissionDto> createPermission(@RequestBody PermissionRequest request) {
        PermissionDto permissionDto = permissionService.createPermission(request);
        return ApiResponse.<PermissionDto>builder()
                .code(200)
                .message("Create permission success")
                .result(permissionDto)
                .build();
    }
    @GetMapping("/{name}")
    public ApiResponse<PermissionDto> getPermissionByName(@PathVariable String name) {
        PermissionDto permissionDto = permissionService.getPermissionByName(name);
        return ApiResponse.<PermissionDto>builder()
                .code(200)
                .message("Get permission success")
                .result(permissionDto)
                .build();
    }
    @DeleteMapping("/{name}")
    public ApiResponse<Void> deletePermissionByName(@PathVariable String name) {
        permissionService.deletePermissionByName(name);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete permission success")
                .build();
    }
    @GetMapping()
    public ApiResponse<List<PermissionDto>> getAllPermissions() {
        List<PermissionDto> permissions = permissionService.getAllPermissions();
        return ApiResponse.<List<PermissionDto>>builder()
                .code(200)
                .message("Get all permissions success")
                .result(permissions)
                .build();
    }
}
