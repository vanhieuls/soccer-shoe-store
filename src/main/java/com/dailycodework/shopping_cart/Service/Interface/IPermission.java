package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.PermissionDto;
import com.dailycodework.shopping_cart.DTO.Request.PermissionRequest;

import java.util.List;

public interface IPermission {
    PermissionDto createPermission(PermissionRequest request);
    PermissionDto getPermissionByName(String name);
    void deletePermissionByName(String name);
    List<PermissionDto> getAllPermissions();
}
