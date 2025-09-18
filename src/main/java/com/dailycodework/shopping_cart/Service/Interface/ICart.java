package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.CartItem;
import com.dailycodework.shopping_cart.Entity.User;

import java.math.BigDecimal;

public interface ICart {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice (Long id);
    Long initializeNewCart();
    Cart getCartByUserId(Long userId);
    void clearSelectedItems(Long cartId);
}
