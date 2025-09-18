package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.OrderDto;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.*;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.Interface.IOrder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static com.dailycodework.shopping_cart.Enum.OderStatus.PENDING;

@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    IOrder orderService;
    @PostMapping("/createOrder")
    public ApiResponse<OrderDto> placeOrderWithOptionalVoucher(@RequestParam(required = true) Long userId,@RequestParam(required = true) Long userAddressId,
                                                  @RequestParam(required = false) Long voucherId) {
        return ApiResponse.<OrderDto>builder()
                .code(200)
                .message("create order success")
                .result(orderService.placeOrderWithOptionalVoucher(userId, userAddressId, voucherId))
                .build();
    }
    @PostMapping("/create/{userId}/{userAddressId}")
    public ApiResponse<OrderDto> createOrder(@PathVariable Long userId, @PathVariable Long userAddressId) {
        try {
            OrderDto order = orderService.placeOrder(userId, userAddressId);
            return ApiResponse.<OrderDto>builder()
                    .code(200)
                    .message("create success")
                    .result(order)
                    .build();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-order/{orderId}")
    public ApiResponse<OrderDto> getOrderById(@PathVariable Long orderId) {
        return ApiResponse.<OrderDto>builder()
                .code(200)
                .message("get success")
                .result(orderService.getOrder(orderId))
                .build();
    }
    @GetMapping("/history-order/{userId}")
    public ApiResponse<Page<OrderDto>> getUserOrders(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        Page<OrderDto>  orderDtos = orderService.getAllOrderPaganationByUser(page, size, userId);
        if(orderDtos.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return ApiResponse.<Page<OrderDto>>builder()
                .code(200)
                .message("get history order of user" + userId + " success")
                .result(orderDtos)
                .build();
    }
    @PostMapping("/applyVoucher/{orderId}/{voucherId}/{userAddressId}")
    public ApiResponse<OrderDto> applyVoucher(@PathVariable Long orderId,@PathVariable Long voucherId, @PathVariable Long userAddressId) {
        return ApiResponse.<OrderDto>builder()
                .code(200)
                .message("apply voucher success")
                .result(orderService.applyVoucher(orderId,voucherId, userAddressId))
                .build();
    }
//    @PostMapping("/applyVoucher/{orderId}")
//    public ApiResponse<OrderDto> testApplyVoucher(@PathVariable Long orderId,@RequestParam Long voucherCode) {
//        return ApiResponse.<OrderDto>builder()
//                .code(200)
//                .message("Test apply voucher success")
//                .result(orderService.testApplyVoucher(orderId,voucherCode))
//                .build();
//    }
    @PutMapping("/update-order-status/{orderId}")
    public ApiResponse<OrderDto> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return ApiResponse.<OrderDto>builder()
                .code(200)
                .message("update order status success")
                .result(orderService.updateOrderStatus(orderId, OderStatus.valueOf(status)))
                .build();
    }
    @GetMapping("/get-order-status/{userId}")
    public ApiResponse<List<OrderDto>> getOrderStatus(@PathVariable Long userId, @RequestParam String status) {
        List<OrderDto> orderDtos = orderService.getOrderStatus(userId, OderStatus.valueOf(status));
        if(orderDtos.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return ApiResponse.<List<OrderDto>>builder()
                .code(200)
                .message("get order status success")
                .result(orderDtos)
                .build();
    }
    @GetMapping("/order-user-status/{userId}")
    public ApiResponse<Page<OrderDto>> getOrdersByUserAndStatus(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,     // có thể null
            @RequestParam(defaultValue = "0") Integer page,       // trang hiện tại
            @RequestParam(defaultValue = "10") Integer size     // số item / trang
    ) {
        Page<OrderDto> orderPage = orderService.getOrderPaganation(page, size,userId, OderStatus.valueOf(status));
        return ApiResponse.<Page<OrderDto>>builder()
                .code(200)
                .message("get order pagination success")
                .result(orderPage)
                .build();
    }
    @GetMapping("/find-Order-By-Name-And-Date/{userId}")
    public ApiResponse<Page<OrderDto>> findOrderByNameAndDate(
            @PathVariable Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String startDay,
            @RequestParam(required = false) String endDay,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<OrderDto> orderPage = orderService.findOrderByNameAndDate( pageNumber, pageSize, name, startDay, endDay);
        if(orderPage.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return ApiResponse.<Page<OrderDto>>builder()
                .code(200)
                .message("find order by name and date success")
                .result(orderPage)
                .build();
    }
    @PatchMapping("/request-cancel-order/{orderId}")
    public ApiResponse<Void> requestCancelOrder(@PathVariable Long orderId) {
        orderService.requestCancelOrder(orderId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Request cancel order success")
                .build();
    }

    @PatchMapping("/approve-cancel-order/{orderId}")
    public ApiResponse<Void> ApproveCancelOrder(@PathVariable Long orderId) {
        orderService.ApproveCancelOrder(orderId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Approve cancel order success")
                .build();
    }
}
