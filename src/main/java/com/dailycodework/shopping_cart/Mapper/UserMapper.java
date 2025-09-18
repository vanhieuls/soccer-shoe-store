package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.OrderDto;
import com.dailycodework.shopping_cart.DTO.Dto.OrderItemDto;
import com.dailycodework.shopping_cart.DTO.Request.UserRequest;
import com.dailycodework.shopping_cart.DTO.Request.UserUpdateRequest;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Entity.Order;
import com.dailycodework.shopping_cart.Entity.OrderItem;
import com.dailycodework.shopping_cart.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",uses = {OrderMapper.class,ReviewMapper.class})
public interface UserMapper {
    User toUser (UserRequest request);
    @Mapping(source = "checked", target = "checked")
    UserResponse toUserResponse (User user);
    void updateUser (@MappingTarget User user, UserUpdateRequest request);
}
