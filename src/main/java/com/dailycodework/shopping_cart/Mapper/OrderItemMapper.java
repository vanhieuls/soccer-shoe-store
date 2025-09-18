package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.OrderItemDto;
import com.dailycodework.shopping_cart.Entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
//    OrderItemDto toDto (OrderItem orderItem);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "productSize.id", target = "productSizeId")
    OrderItemDto toDto(OrderItem orderItem);
    List<OrderItemDto> toDtoList (List<OrderItem> orderItemList);

}
