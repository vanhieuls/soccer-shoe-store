package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.OrderDto;
import com.dailycodework.shopping_cart.DTO.Dto.OrderItemDto;
import com.dailycodework.shopping_cart.Entity.Order;
import com.dailycodework.shopping_cart.Entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PaymentMapper.class})
public interface OrderMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "productSize.id", target = "productSizeId")
    OrderItemDto toDto(OrderItem orderItem);
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(source = "user.id", target = "userId")
//    @Mapping(source = "voucher.id", target = "voucherId")
    OrderDto toOrderDto (Order order);
    List<OrderDto> toListOrderDto (List<Order> orderList);
}
