package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.ImpInterface.ImpCart;
import com.dailycodework.shopping_cart.Service.Interface.ICart;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    ICart cartService;
    @GetMapping("/get-cart/{id}")
    public ApiResponse<Cart> getCart(@PathVariable Long id) {
        return ApiResponse.<Cart>builder()
                .code(200)
                .message("get cart success")
                .result(cartService.getCart(id))
                .build();
    }
    @DeleteMapping("/delete-cart/{id}")
    public ApiResponse<Void> clearCart(@PathVariable Long id) {
        cartService.clearCart(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("delete success")
                .build();

    }
    @GetMapping("/get-price/{id}")
    public ApiResponse<BigDecimal> getPrice(@PathVariable Long id) {
        BigDecimal totalAmount= cartService.getTotalPrice(id);
        return ApiResponse.<BigDecimal>builder()
                .code(200)
                .message("get total amount success")
                .result(totalAmount)
                .build();

    }
}
