package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.CartItemDto;
import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.CartItem;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICartItem {
//    void addItemToCart (Long cartId, Long productId, int quantity);
    void addItemToCart (Long cartId, Long productId, int quantity, Long productSizeId);
    Cart removeItemFromCart(Long cartId, Long productId);
    CartItemDto updateItemQuantity (Long cartId, Long productId, int quantity);
    Page<CartItemDto> getItemFromCart(Integer pageNumber, Integer pageSize, Long cartId);
    List<CartItemDto> getItemFromCart(Long cartId);
    CartItemDto updateSelected(Long id, boolean selected);
    CartItemDto getCartItemById(Long id);
}
