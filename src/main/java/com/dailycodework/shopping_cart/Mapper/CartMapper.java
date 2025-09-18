package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.CartDto;
import com.dailycodework.shopping_cart.Entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toCartDto (Cart cart);
}
