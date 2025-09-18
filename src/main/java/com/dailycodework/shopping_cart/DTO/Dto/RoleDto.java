package com.dailycodework.shopping_cart.DTO.Dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDto {
    String name;
    String description;
    Set<PermissionDto> permissions;
}
