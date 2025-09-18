package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.CartItemDto;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.CartItem;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.Interface.ICart;
import com.dailycodework.shopping_cart.Service.Interface.ICartItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cartItem")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartItemController {
    ICartItem cartItemService;
    ICart cartService;
    @PostMapping ("/add-cartItem")
//    public ApiResponse<Void> addItemToCart (@RequestParam(required = false) Long cartId, @RequestParam Long productId, @RequestParam int quantity){
//        if(cartId == null){
//            cartId = cartService.initializeNewCart();
//        }
//        cartItemService.addItemToCart(cartId,productId,quantity);
//        return ApiResponse.<Void>builder()
//                .code(200)
//                .message("add success")
//                .build();
//    }
    public ApiResponse<Void> addItemToCart (@RequestParam(required = false) Long cartId, @RequestParam Long productId, @RequestParam int quantity,@RequestParam (required = false) Long productSizeId){
        if(cartId == null){
            cartId = cartService.initializeNewCart();
        }
        cartItemService.addItemToCart(cartId,productId,quantity, productSizeId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("add success")
                .build();
    }
    @PostMapping ("/remove-cartItem")
    public ApiResponse<Cart> removeItemFromCart (@RequestParam Long cartId, @RequestParam Long cartItemId){
        Cart cart = cartItemService.removeItemFromCart(cartId,cartItemId);
        return ApiResponse.<Cart>builder()
                .code(200)
                .message("remove success")
                .result(cart)
                .build();
    }

    @PutMapping("/update-quantity")
    public ApiResponse<CartItemDto> updateItemQuantity (@RequestParam Long cartId, @RequestParam Long productId, @RequestParam int quantity){
        CartItemDto cart = cartItemService.updateItemQuantity(cartId,productId, quantity);
        return ApiResponse.<CartItemDto>builder()
                .code(200)
                .message("update success")
                .result(cart)
                .build();
    }
    @GetMapping("/get-item/{cartId}")
    public ApiResponse<List<CartItemDto>> getItemFromCart (@PathVariable Long cartId){
        List<CartItemDto> cart = cartItemService.getItemFromCart(cartId);
        return ApiResponse.<List<CartItemDto>>builder()
                .code(200)
                .message("get item success")
                .result(cart)
                .build();
    }
    @GetMapping("/get-item-paging/{cartId}")
    public ApiResponse<Page<CartItemDto>> getItemFromCart (@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "5") Integer pageSize, @PathVariable Long cartId){
        Page<CartItemDto> cart = cartItemService.getItemFromCart(pageNumber, pageSize, cartId);
        return ApiResponse.<Page<CartItemDto>>builder()
                .code(200)
                .message("get item success")
                .result(cart)
                .build();
    }
    @PatchMapping("/selected/{id}")
    public ApiResponse<CartItemDto> updateSelected(@PathVariable Long id,@RequestParam boolean selected) {
        return ApiResponse.<CartItemDto>builder()
                .code(200)
                .message("update success")
                .result(cartItemService.updateSelected(id,selected))
                .build();
    }
    @GetMapping("/get-cartItem/{id}")
    public ApiResponse<CartItemDto> getCartItemById(@PathVariable Long id) {
        CartItemDto cartItem = cartItemService.getCartItemById(id);
        log.info("get cart item by id: {}", id);
        if(cartItem == null) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_EXIST);
        }
        return ApiResponse.<CartItemDto>builder()
                .code(200)
                .message("get success")
                .result(cartItem)
                .build();
    }
}
