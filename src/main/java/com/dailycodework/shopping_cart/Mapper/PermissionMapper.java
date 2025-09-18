package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.PermissionDto;
import com.dailycodework.shopping_cart.DTO.Request.PermissionRequest;
import com.dailycodework.shopping_cart.Entity.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission (PermissionRequest permissionRequest);
    PermissionDto toPermissionDto (Permission permission);
    List<PermissionDto> toPermissionsDto (List<Permission> permissions);
}
