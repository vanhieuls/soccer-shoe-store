package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.Service.ImpInterface.ImpPayment;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
@Controller
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payments")
public class TestController {
    ImpPayment impPayment;
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/create")
    public RedirectView createPayment(
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("method") String method,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description
    ) {
        try {
            String cancelUrl = "http://localhost:8080/api/payments/cancel";
            String returnUrl = "http://localhost:8080/api/payments/success";
            Payment payment = impPayment.createPayment(
                    amount,
                    currency,
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    returnUrl
            );
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return new RedirectView(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
        return new RedirectView("/api/payments/error");
    }

    @GetMapping("/success")
    public String successPayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = impPayment.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "paymentSuccess";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred during payment execution:: ", e);
        }
        return "paymentSuccess";
    }
    @GetMapping("/cancel")
    public String cancelPayment() {
        return "paymentCancel";
    }
    @GetMapping("/error")
    public String errorPayment() {
        return "paymentError";
    }
}
