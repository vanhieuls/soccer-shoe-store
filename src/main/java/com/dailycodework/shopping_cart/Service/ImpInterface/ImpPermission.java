package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Dto.PermissionDto;
import com.dailycodework.shopping_cart.DTO.Request.PermissionRequest;
import com.dailycodework.shopping_cart.Entity.Permission;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Mapper.PermissionMapper;
import com.dailycodework.shopping_cart.Repository.PermissionRepository;
import com.dailycodework.shopping_cart.Service.Interface.IPermission;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpPermission implements IPermission {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    @Override
    public PermissionDto createPermission(PermissionRequest request) {
        if(permissionRepository.existsByName(request.getName())){
            throw  new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionDto(permission);
    }

    @Override
    public PermissionDto getPermissionByName(String name) {
        Permission permission = permissionRepository.findByName(name)
                .orElseThrow(()->new AppException(ErrorCode.PERMISSION_NOT_EXIST));
        return permissionMapper.toPermissionDto(permission);
    }

    @Override
    public void deletePermissionByName(String name) {
        Permission permission = permissionRepository.findByName(name)
                .orElseThrow(()->new AppException(ErrorCode.PERMISSION_NOT_EXIST));
        permissionRepository.delete(permission);
    }

    @Override
    public List<PermissionDto> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissionMapper.toPermissionsDto(permissions);
    }
}
