package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.CartItem;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Repository.CartItemRepository;
import com.dailycodework.shopping_cart.Repository.CartRepository;
import com.dailycodework.shopping_cart.Service.Interface.ICart;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpCart implements ICart {
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    @Override
    public Cart getCart(Long id) {
       return cartRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.CART_NOT_FOUND));
//       BigDecimal totalAmount = cart.getTotalAmount();
//       cart.setTotalAmount(totalAmount);
//       return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        cartRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.CART_NOT_EXIST));
//        cartItemRepository.deleteAllByCartId(id);
        cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.CART_NOT_FOUND));
        return cart.getTotalAmount();
    }

    @Override
    public Long initializeNewCart() {
        Cart newCart = new Cart();
        return cartRepository.save(newCart).getId();
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST));
    }
    @Override
    public void clearSelectedItems(Long cartId) {
        Cart cart = getCart(cartId);
        Set<CartItem> itemsToRemove = cart.getCartItems()
                .stream()
                .filter(CartItem::isSelected)
                .collect(Collectors.toSet());
        cart.getCartItems().removeAll(itemsToRemove); //Xóa khỏi bộ nhớ cart
//        15/9
        cartRepository.save(cart); // ⬅️ Đây là bước kích hoạt orphanRemoval
//        cartItemRepository.deleteAll(itemsToRemove);  // Xóa khỏi database
    }

}
