package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.RoleDto;
import com.dailycodework.shopping_cart.DTO.Request.RoleRequest;
import com.dailycodework.shopping_cart.Entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole (RoleRequest roleRequest);
    RoleDto toRoleDto (Role role);
    List<RoleDto> toRolesDto (List<Role> roles);
}
