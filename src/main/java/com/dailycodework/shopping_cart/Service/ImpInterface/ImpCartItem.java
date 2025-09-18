package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Dto.CartItemDto;
import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.CartItem;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Mapper.CartItemMapper;
import com.dailycodework.shopping_cart.Mapper.ProductMapper;
import com.dailycodework.shopping_cart.Mapper.ProductSizeMapper;
import com.dailycodework.shopping_cart.Repository.CartItemRepository;
import com.dailycodework.shopping_cart.Repository.CartRepository;
import com.dailycodework.shopping_cart.Service.Interface.ICartItem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpCartItem implements ICartItem {
    CartItemRepository cartItemRepository;
    CartRepository cartRepository;
    CartItemMapper mapper;
    ImpProduct productService;
    ImpProductSize productSizeService;
    ImpCart cartService;
    boolean selected=false;
    ProductMapper productMapper;
    @Override
//    public void addItemToCart(Long cartId, Long productId, int quantity) {
//        Cart cart = cartService.getCart(cartId);
//        Product product =  productMapper.responseToProduct(productService.getProductById(productId));
//        CartItem cartItem = cart.getCartItems().stream().filter(item->item.getProduct().getId().equals(productId))
//                .findFirst().orElse(new CartItem());
//        if(cartItem.getId()==null){
//            cartItem = CartItem.builder()
//                    .cart(cart)
//                    .product(product)
//                    .quantity(quantity)
//                    .unitPrice(product.getPrice())
//                    .build();
//        }
//        else{
//            cartItem.setQuantity(quantity);
//        }
//        //update price in cart item
//        cartItem.setTotalPrice();
//        //add cart item into cart
//        cart.getCartItems().add(cartItem);
//        //update totalAmount in cart
//        BigDecimal totalAmount = cart.updateTotalAmount();
//        cart.setTotalAmount(totalAmount);
//        cartItem.setCart(cart);
//        //save to db
//        cartItemRepository.save(cartItem);
//        cartRepository.save(cart);
//    }
    public void addItemToCart(Long cartId, Long productId, int quantity, Long productSizeId) {
        Cart cart = cartService.getCart(cartId);
        Product product =  productMapper.responseToProduct(productService.getProductById(productId));
        CartItem cartItem = cart.getCartItems().stream().filter(item->item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if(cartItem.getId()==null){
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .productSize(productSizeService.getProductSizeById(productSizeId))
                    .quantity(quantity)
                    .unitPrice(product.getPrice())
                    .build();
        }
        else{
            cartItem.setQuantity(quantity);
        }
        //update price in cart item
        cartItem.setTotalPrice();
        //add cart item into cart
        cart.getCartItems().add(cartItem);
        //update totalAmount in cart
        BigDecimal totalAmount = cart.updateTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartItem.setCart(cart);
        //save to db
        //==================9/11===========
//        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public Cart removeItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItemRemove = cart.getCartItems().stream().filter(item -> item.getId().equals(cartItemId)).findFirst().orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        cart.getCartItems().remove(cartItemRemove);
        cart.setTotalAmount(cart.updateTotalAmount());
//        cart.updateTotalAmount();
        return cartRepository.save(cart);
    }


    @Override
    public CartItemDto updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = cart.getCartItems().stream().filter(cartItem1 -> cartItem1.getProduct().getId().equals(productId)).findFirst().orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(cartItem.getProduct().getPrice());
        cartItem.setTotalPrice();
        BigDecimal totalAmount = cart.updateTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
        return mapper.cartItemDto(cartItem);
    }
    @Override
    public Page<CartItemDto> getItemFromCart(Integer pageNumber, Integer pageSize, Long cartId) {
        Pageable pageable = null;
        if(pageNumber == null || pageNumber < 0){
            pageNumber = 0;
        }
        if(pageSize == null || pageSize <=0){
            pageSize = 5;
        }
        pageable = PageRequest.of(pageNumber,pageSize);
        Page<CartItemDto> cartItemDtos = cartItemRepository.findByCartId(cartId,pageable).orElseThrow(()-> new AppException(ErrorCode.CART_NOT_EXIST)).map(mapper::cartItemDto);
        if(cartItemDtos.isEmpty()){
            throw  new AppException(ErrorCode.CART_ITEM_NOT_EXIST);
        }
        return cartItemDtos;
    }
    @Override
    public List<CartItemDto> getItemFromCart(Long cartId) {
        return mapper.cartItemsDto(cartItemRepository.findByCartId(cartId));
    }
    @Transactional
    @Override
    public CartItemDto updateSelected(Long id, boolean selected) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.CART_ITEM_NOT_EXIST));
        cartItem.setSelected(selected);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        return  mapper.cartItemDto(updatedCartItem);
    }

    @Override
    public CartItemDto getCartItemById(Long id) {
        return mapper.cartItemDto(
                cartItemRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_EXIST))
        );
    }
}
