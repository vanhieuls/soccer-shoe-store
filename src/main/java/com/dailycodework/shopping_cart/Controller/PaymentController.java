package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.PaymentDto;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.Order;
import com.dailycodework.shopping_cart.Entity.Payment;
import com.dailycodework.shopping_cart.Enum.PaymentMethod;
import com.dailycodework.shopping_cart.Enum.PaymentStatus;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.Interface.IPayment;
import com.dailycodework.shopping_cart.Validation.ValidationEnum.EnumValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.usertype.UserType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    IPayment paymentService;
    @PostMapping("/payos_transfer_handler")
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body) throws JsonProcessingException, IllegalArgumentException {
        return paymentService.payosTransferHandler(body);
    }
    @PostMapping("/confirm-webhook")
    public ObjectNode confirmWebhook(@RequestBody Map<String, String> requestBody) {
        return paymentService.confirmWebhook(requestBody);
    }
    @PutMapping("/{orderId}")
    public ObjectNode cancelOrder(@PathVariable("orderId") int orderId) {
        return paymentService.cancelOrder(orderId);
    }
    @GetMapping("/{orderId}")
    public ObjectNode getOrderById(@PathVariable("orderId") long orderId) {
        return paymentService.getOrderById(orderId);
    }
    @PostMapping(path = "/create-payment-link", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResponse<String> Checkout(HttpServletRequest request, HttpServletResponse httpServletResponse, @RequestParam Long orderId,
                                @NotNull(message = "Payment method is required") @EnumValue(name = "paymentMethod", enumClass = PaymentMethod.class) @RequestParam String paymentMethod) {
        String checkoutUrl = paymentService.Checkout(request,httpServletResponse,orderId,paymentMethod);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Create payment link success")
                .result(checkoutUrl)
                .build();
    }
    @GetMapping("/{Id}/details")
    public ApiResponse<PaymentDto> getPaymentById(@PathVariable Long Id) {
        PaymentDto payment = paymentService.getPaymentById(Id);
        return ApiResponse.<PaymentDto>builder()
                .code(200)
                .message("Get payment success")
                .result(payment)
                .build();
    }
}
