package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.CartItemDto;
import com.dailycodework.shopping_cart.Entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source = "productSize.id", target = "productSizeId")
    CartItemDto cartItemDto(CartItem cartItem);
    List<CartItemDto> cartItemsDto (List<CartItem> cartItems);
}
