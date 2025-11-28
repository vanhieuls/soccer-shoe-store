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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
import vn.payos.model.webhooks.WebhookData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ImpPayment implements IPayment {
    PayOS payOS;
    ImpOrder orderService;
    PaymentRepository paymentRepository;
    OrderRepository orderRepository;
    PaymentMapper paymentMapper;
    APIContext apiContext;
    @Override
    public ObjectNode payosTransferHandler(ObjectNode body) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            // Init Response
            response.put("error", 0);
            response.put("message", "Webhook delivered");
            response.set("data", null);

            // Verify webhook using PayOS 2.0.1 API
            WebhookData data = payOS.webhooks().verify(body);

            // Log webhook data
            log.info("======= [Webhook Received] =======");
            log.info("OrderCode: {}", data.getOrderCode());
            log.info("Amount: {}", data.getAmount());
            log.info("Description: {}", data.getDescription());

            Payment payment = paymentRepository.findByOrderCode(data.getOrderCode())
                    .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
            Order order = payment.getOrder();

            // Check payment status based on code
            if ("success".equalsIgnoreCase(data.getDesc())) {
                payment.setStatus(PaymentStatus.PAID);
                payment.setAmount(order.getTotalAmount());
                payment.setAmountPaid(new BigDecimal(data.getAmount()));
                payment.setAmountRemaining(order.getTotalAmount().subtract(payment.getAmountPaid()));

                // Optional fields
                if (data.getCounterAccountBankName() != null) {
                    payment.setCounterAccountBankName(data.getCounterAccountBankName());
                }
                if (data.getCounterAccountName() != null) {
                    payment.setCounterAccountName(data.getCounterAccountName());
                }
                if (data.getCounterAccountNumber() != null) {
                    payment.setCounterAccountNumber(data.getCounterAccountNumber());
                }
                if (data.getTransactionDateTime() != null) {
                    payment.setTransactionId(data.getTransactionDateTime());
                }
                order.setPayment(payment);
            }
            else {
                log.info("3456789fffffffffffffffffffff");
                payment.setStatus(PaymentStatus.FAILED);
                order.setPayment(payment);
                order.setOderStatus(OderStatus.CANCELLED);
                log.info("Order {} cancelled due to payment failure", order.getOrderCode());
            }

            orderRepository.save(order);
            paymentRepository.save(payment);
            return response;
        } catch (Exception e) {
            log.error("Webhook processing failed: {}", e.getMessage(), e);
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
            String webhookUrl = requestBody.get("webhookUrl");
            // PayOS 2.0.1 may not have confirmWebhook in webhooks(), return success for now
            response.put("data", "Webhook URL configured: " + webhookUrl);
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            log.error("Error confirming webhook: {}", e.getMessage(), e);
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @Override
    public ObjectNode cancelOrder(int orderId) {
        log.info("ccccccccccccccc");
        log.info(String.valueOf(orderId));
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            Order order = orderRepository.findById((long) orderId)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            Payment payment = order.getPayment();
            log.info("ccccccccccccccc");
            // Nếu có payment và phương thức là PAYOS, gọi API PayOS để hủy
            if (payment != null && payment.getMethod() == PaymentMethod.PAYOS) {
                try {
                    payOS.paymentRequests().cancel(payment.getOrderCode());
                } catch (Exception e) {
                    log.warn("PayOS cancel failed (maybe already cancelled): {}", e.getMessage());
                }
            }

            log.info("ddddddddddddddd");
            // Cập nhật status trong database
            if (payment != null) {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
            }

            order.setOderStatus(OderStatus.CANCELLED);
            orderRepository.save(order);
            log.info("Order {} cancelled successfullllllllllllly", order.getOrderCode());
            // PayOS 2.0.1 API for canceling payment link - cancel(long orderCode)
//            Object result = payOS.paymentRequests().cancel((long) orderId);
//            response.set("data", objectMapper.valueToTree(result));
            response.put("data", "Order cancelled successfully");
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            log.error("Error cancelling order: {}", e.getMessage(), e);
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
            // PayOS 2.0.1 API for getting payment link info
            Object result = payOS.paymentRequests().get(orderId);
            response.set("data", objectMapper.valueToTree(result));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            log.error("Error getting order info: {}", e.getMessage(), e);
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
            // Save order first to ensure it exists

            // Create payment for COD
            Payment payment = Payment.builder()
                    .order(order)
                    .orderCode(order.getOrderCode())
                    .description("Thanh toan khi nhan hang")
                    .amount(order.getTotalAmount())
                    .amountPaid(BigDecimal.ZERO)
                    .amountRemaining(order.getTotalAmount())
                    .method(PaymentMethod.COD)
                    .status(PaymentStatus.UNPAID)
                    .build();

            paymentRepository.save(payment);

            // Update order with payment reference
            order.setPayment(payment);
            orderRepository.save(order);

            return "Thanh toan khi nhan hang thanh cong";
        } else if (method.equalsIgnoreCase("PAYOS")) {
            try {
                final String baseUrl = getBaseUrl(request);
                final String description = "Thanh toan don hang";
//                final String returnUrl = baseUrl + "/payment/success";
//                final String cancelUrl = baseUrl + "/payment/cancel";
                final String returnUrl = "https://vnhi-store.vercel.app/xac-nhan-thanh-toan";
                final String cancelUrl = "https://vnhi-store.vercel.app/xac-nhan-thanh-toan";

                final long price = order.getTotalAmount().longValue();

                // Generate unique orderCode if not exists
                Long orderCode = order.getOrderCode();

                // Build items list using PayOS 2.0.1 PaymentLinkItem
                List<PaymentLinkItem> items = new ArrayList<>();
                for (var item : order.getOrderItems()) {
                    PaymentLinkItem paymentItem = PaymentLinkItem.builder()
                            .name(item.getProduct().getName())
                            .quantity(item.getQuantity())
                            .price(item.getPrice().longValue())
                            .build();
                    items.add(paymentItem);
                }

                // Build CreatePaymentLinkRequest using PayOS 2.0.1 API
                CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                        .orderCode(orderCode)
                        .description(description)
                        .amount(price)
                        .items(items)
                        .returnUrl(returnUrl)
                        .cancelUrl(cancelUrl)
                        .build();

                // Create payment object
                Payment payment = Payment.builder()
                        .order(order)
                        .orderCode(orderCode)
                        .description(description)
                        .amount(order.getTotalAmount())
                        .amountPaid(BigDecimal.ZERO)
                        .amountRemaining(order.getTotalAmount())
                        .method(PaymentMethod.PAYOS)
                        .status(PaymentStatus.UNPAID)
                        .build();

                // Call PayOS 2.0.1 API to create payment link
                CreatePaymentLinkResponse responseData = payOS.paymentRequests().create(paymentData);
                checkoutUrl = responseData.getCheckoutUrl();

                // Save payment after successful PayOS API call
                paymentRepository.save(payment);

                // Update order with payment reference
                order.setPayment(payment);
                orderRepository.save(order);


                log.info("PayOS payment link created successfully: {}", checkoutUrl);

//                httpServletResponse.setHeader("Location", checkoutUrl);
//                httpServletResponse.setStatus(302);
            } catch (Exception e) {
                log.error("Error creating PayOS payment link: {}", e.getMessage(), e);
                throw new AppException(ErrorCode.PAYMENT_CREATION_FAILED);
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

    @Override
    public com.paypal.api.payments.Payment createPaymentPayPal(Long orderId) throws PayPalRESTException{
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(order.getTotalAmount().divide(new BigDecimal("25000"), 2, BigDecimal.ROUND_HALF_UP).toString());
        com.paypal.api.payments.Transaction transaction = new com.paypal.api.payments.Transaction();
        transaction.setDescription("Thanh toan don hang co ma code:" + order.getOrderCode());
        transaction.setAmount(amount);
        List<com.paypal.api.payments.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        Payer payer = new Payer();
        payer.setPaymentMethod("PAYPAL");
        com.paypal.api.payments.Payment payment = new com.paypal.api.payments.Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        com.paypal.api.payments.RedirectUrls redirectUrls = new com.paypal.api.payments.RedirectUrls();
        String cancelUrl = "https://vnhi-store.vercel.app/xac-nhan-thanh-toan?status=CANCELLED&cancel=true";
        String successUrl = "https://vnhi-store.vercel.app/xac-nhan-thanh-toan?orderId=" + order.getOrderCode() + "&status=PAID&cancel=false";
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        Payment payments = Payment.builder()
                .order(order)
                .description("Thanh toan bang PayPal cho don hang co ma code:" + order.getOrderCode())
                .amount(order.getTotalAmount())
                .amountPaid(BigDecimal.ZERO)
                .amountRemaining(order.getTotalAmount())
                .method(PaymentMethod.PAYPAL)
                .status(PaymentStatus.UNPAID)
                .build();

        paymentRepository.save(payments);

        // Update order with payment reference
        order.setPayment(payments);
        orderRepository.save(order);

        return payment.create(apiContext);
    }

    @Override
    public com.paypal.api.payments.Payment executePayments(String paymentId, String payerId) throws PayPalRESTException {
        com.paypal.api.payments.Payment payment = new com.paypal.api.payments.Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }

    @Override
    public String checkExcutePayment(Long orderId, String paymentId, String payerId) throws PayPalRESTException {
        Order order = orderRepository.findByOrderCode(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        try {
            com.paypal.api.payments.Payment payment = executePayments(paymentId, payerId);
            Payment payments = order.getPayment();
            if (payment.getState().equals("approved")) {
                PayerInfo payerInfo = payment.getPayer().getPayerInfo();
                payments.setStatus(PaymentStatus.PAID);
                payments.setAmount(order.getTotalAmount());
                payments.setAmountPaid(order.getTotalAmount());
                payments.setAmountRemaining(BigDecimal.ZERO);
                payments.setOrderCode(order.getOrderCode());
//                payments.setTransactionId(data.getTransactionDateTime());
                payments.setCounterAccountName(payerInfo.getFirstName() + " " + payerInfo.getLastName());
                payments.setCounterAccountNumber(payerInfo.getPayerId());
                payments.setCounterAccountBankName("PayPal");
                order.setPayment(payments);
                orderRepository.save(order);
                return "https://vnhi-store.vercel.app/xac-nhan-thanh-toan?orderId=" + order.getOrderCode() + "&status=PAID&cancel=false";
            }
            else{
                payments.setStatus(PaymentStatus.FAILED);
                order.setPayment(payments);
                order.setOderStatus(OderStatus.CANCELLED);
                orderRepository.save(order);
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred during payment execution:: ", e);
        }
        return "https://vnhi-store.vercel.app/xac-nhan-thanh-toan? orderId="+ order.getOrderCode() +"&status=CANCELLED&cancel=true";
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
//        return scheme + "://" + serverName + ":5173";
        return scheme + "://" + serverName;
    }


//    =====================PAYPAL==========================

    public com.paypal.api.payments.Payment createPayment(
            BigDecimal total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency),"%.2f",total));
        com.paypal.api.payments.Transaction transaction = new com.paypal.api.payments.Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        List<com.paypal.api.payments.Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        Payer payer = new Payer();
        payer.setPaymentMethod(method);
        com.paypal.api.payments.Payment payment = new com.paypal.api.payments.Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        com.paypal.api.payments.RedirectUrls redirectUrls = new com.paypal.api.payments.RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);
        return payment.create(apiContext);
    }
    public com.paypal.api.payments.Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        com.paypal.api.payments.Payment payment = new com.paypal.api.payments.Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }
}

