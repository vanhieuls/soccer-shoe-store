package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.OrderDto;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrder {
//    OrderDto placeOrder (Long userId);
    OrderDto placeOrder (Long userId, Long userAddressId);
    OrderDto getOrder (Long orderId);
    Page<OrderDto> getOrdersByUserId(Integer pageNumber, Integer pageSize, Long userId);
    List<OrderDto> getUserOrders (Long userId);
//    OrderDto applyVoucher(Long orderId, Long voucherId);
    OrderDto applyVoucher(Long orderId, Long voucherId, Long userAddressId);
    OrderDto testApplyVoucher(Long orderId, Long voucherId);
    OrderDto updateOrderStatus(Long orderId, OderStatus status);
    List<OrderDto> getOrderStatus(Long userId, OderStatus status);
    OrderDto placeOrderWithOptionalVoucher(Long userId, Long userAddressId, Long voucherId);
    Page<OrderDto> getAllOrderPaganationByUser(Integer pageNumber, Integer pageSize, Long userId);
    Page<OrderDto> findOrderByNameAndDate(Integer pageNumber, Integer pageSize, String name, String startDay, String endDay);
    Page<OrderDto> getOrderPaganation(Integer pageNumber, Integer pageSize, Long userId, OderStatus status);
//    Page<OrderDto> getAllHistory(Integer pageNumber, Integer pageSize, Long userId);
//    Page<OrderDto> getAllOrderByStatus(Integer pageNumber, Integer pageSize, OderStatus status, String properties, String sortDir);
//
    void requestCancelOrder(Long orderId);
    void ApproveCancelOrder(Long orderId);

}
