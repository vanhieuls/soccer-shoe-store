package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.PaymentDto;
import com.dailycodework.shopping_cart.Entity.Payment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface PaymentMapper {
    PaymentDto toPaymentDto (Payment payment);
    List<PaymentDto> toPaymentsDto (List<Payment> payments);
}
