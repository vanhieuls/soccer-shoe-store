package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.PaymentDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface IPayment {
    ObjectNode payosTransferHandler(ObjectNode body) throws JsonProcessingException, IllegalArgumentException;
    ObjectNode confirmWebhook(Map<String, String> requestBody);
    ObjectNode cancelOrder(int orderId);
    ObjectNode getOrderById(long orderId);
    String Checkout(HttpServletRequest request, HttpServletResponse httpServletResponse, Long orderId, String method);
    PaymentDto getPaymentById(Long Id);
}
