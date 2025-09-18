package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Dto.PaymentDto;
import com.dailycodework.shopping_cart.Entity.Order;
import com.dailycodework.shopping_cart.Entity.Payment;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import com.dailycodework.shopping_cart.Enum.PaymentMethod;
import com.dailycodework.shopping_cart.Enum.PaymentStatus;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Mapper.PaymentMapper;
import com.dailycodework.shopping_cart.Repository.OrderRepository;
import com.dailycodework.shopping_cart.Repository.PaymentRepository;
import com.dailycodework.shopping_cart.Service.Interface.IPayment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ImpPayment implements IPayment {
    PayOS payOS;
    ImpOrder orderService;
    PaymentRepository paymentRepository;
    OrderRepository orderRepository;
    PaymentMapper paymentMapper;
    @Override
    public ObjectNode payosTransferHandler(ObjectNode body) throws JsonProcessingException, IllegalArgumentException {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        // Convert sang object
        Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);

        try {

            // Init Response
            response.put("error", 0);
            response.put("message", "Webhook delivered");
            response.set("data", null);
            // Verify checksum// Verify dữ liệu webhook
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            // Log dữ liệu đã verify
            System.out.println("======= [WebhookData Verified] =======");
            System.out.println("OrderCode: " + data.getOrderCode());
            System.out.println("Amount: " + data.getAmount());
            System.out.println("Status thanh toan: " + data.getDesc());
            System.out.println("Date: " + data.getTransactionDateTime());
            System.out.println("Ma loi: " + webhookBody.getCode());
            System.out.println("Mo ta loi: " + webhookBody.getDesc());
            System.out.println(data);
            Payment payment = paymentRepository.findByOrderCode(data.getOrderCode()).orElseThrow(()-> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
            Order order = payment.getOrder();
            if(data.getDesc().equals("success")) {
                payment.setStatus(PaymentStatus.PAID);
                payment.setAmount(order.getTotalAmount());
                payment.setAmountPaid(new BigDecimal(data.getAmount()));
                payment.setAmountRemaining(order.getTotalAmount().subtract(payment.getAmountPaid()));
                payment.setCounterAccountBankName(data.getCounterAccountBankName());
                payment.setCounterAccountName(data.getCounterAccountName());
                payment.setCounterAccountNumber(data.getCounterAccountNumber());
                payment.setTransactionId(data.getTransactionDateTime());
                order.setPayment(payment);
            }
            else{
                payment.setStatus(PaymentStatus.FAILED);
                order.setPayment(payment);
                order.setOderStatus(OderStatus.CANCELLED);
            }
            orderRepository.save(order);
            paymentRepository.save(payment);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @Override
    public ObjectNode confirmWebhook(Map<String, String> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            String str = payOS.confirmWebhook(requestBody.get("webhookUrl"));
            response.set("data", objectMapper.valueToTree(str));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @Override
    public ObjectNode cancelOrder(int orderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.cancelPaymentLink(orderId, null);
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @Override
    public ObjectNode getOrderById(long orderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);

            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @Override
    public String Checkout(HttpServletRequest request, HttpServletResponse httpServletResponse, Long orderId, String method) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        String checkoutUrl = "";
        if (method.equalsIgnoreCase("COD")) {
            Payment payment = Payment.builder()
                    .order(order)
                    .amount(order.getTotalAmount())
                    .amountPaid(BigDecimal.ZERO)
                    .amountRemaining(order.getTotalAmount())
                    .method(PaymentMethod.valueOf(method))  // set theo tham số
                    .status(PaymentStatus.UNPAID)
                    .build();
            order.setPayment(payment);
            orderRepository.save(order);
            paymentRepository.save(payment);

        } else if (method.equalsIgnoreCase("PAYOS")) {
            try {
                final String baseUrl = getBaseUrl(request);
                final String productName = order.getOrderItems().stream().map(item -> item.getProduct().getName()).reduce((first, second) -> first + ", " + second).orElse("Don hang");
                final String description = "Thanh toan don hang";
                final String returnUrl = baseUrl + "/payment/success";
                final String cancelUrl = baseUrl + "/payment/cancel";
                final int price = order.getTotalAmount().intValue();
                // Gen order code
                String currentTimeString = String.valueOf(new java.util.Date().getTime());
                Long orderCode = order.getOrderCode();
                List<ItemData> items;
                items = order.getOrderItems().stream().map(item -> ItemData.builder().name(item.getProduct().getName()).quantity(item.getQuantity()).
                        price(item.getProduct().getPrice().intValue()).build()).toList();

                PaymentData paymentData = PaymentData.builder().orderCode(orderCode).amount(price).description(description)
                        .returnUrl(returnUrl).cancelUrl(cancelUrl).items(new ArrayList<>(items)).build();
                Payment payment = Payment.builder()
                        .order(order)
                        .orderCode(orderCode)
                        .description(description)
                        .amount(order.getTotalAmount())
                        .amountPaid(BigDecimal.ZERO)
                        .amountRemaining(order.getTotalAmount())
                        .method(PaymentMethod.valueOf(method))  // set theo tham số
                        .status(PaymentStatus.UNPAID)
                        .build();
                orderRepository.save(order);
                paymentRepository.save(payment);
                CheckoutResponseData data = payOS.createPaymentLink(paymentData);
                checkoutUrl = data.getCheckoutUrl();

//                httpServletResponse.setHeader("Location", checkoutUrl);
//                httpServletResponse.setStatus(302);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new AppException(ErrorCode.PAYMENT_METHOD_NOT_SUPPORTED);

        }
        return checkoutUrl;
    }

    @Override
    public PaymentDto getPaymentById(Long Id) {
        Payment payment = paymentRepository.findById(Id).orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        return paymentMapper.toPaymentDto(payment);
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

//        String url = scheme + "://" + serverName;
//        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
//            url += ":" + serverPort;
//        }
//        url += contextPath;
        return scheme + "://" + serverName + ":5173";
    }
}
