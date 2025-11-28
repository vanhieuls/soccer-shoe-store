package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.OrderDto;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IOrder {
//    OrderDto placeOrder (Long userId);
    OrderDto placeOrder (Long userId, Long userAddressId);
    OrderDto getOrder (Long orderId);
    List<OrderDto> getUserOrders (Long userId);
//    OrderDto applyVoucher(Long orderId, Long voucherId);
    OrderDto applyVoucher(Long orderId, Long voucherId, Long userAddressId);
    OrderDto testApplyVoucher(Long orderId, Long voucherId);
    OrderDto updateOrderStatus(Long orderId, OderStatus status);
    List<OrderDto> getOrderStatus(Long userId, OderStatus status);

    Page<OrderDto> getAllOrderPaganationByUser(Integer pageNumber, Integer pageSize, Long userId);
    Page<OrderDto> findOrderByNameAndDate(Integer pageNumber, Integer pageSize,OderStatus orderStatus, String name, String startDay, String endDay, Long userId);
    Page<OrderDto> getOrderPaganation(Integer pageNumber, Integer pageSize, Long userId, OderStatus status);
//    Page<OrderDto> getAllHistory(Integer pageNumber, Integer pageSize, Long userId);
//    Page<OrderDto> getAllOrderByStatus(Integer pageNumber, Integer pageSize, OderStatus status, String properties, String sortDir);
    OrderDto requestCancelOrder(Long orderId);
    OrderDto confirmCancelOrder(Long orderId);
    BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate);
    Long countOrdersByStatus(OderStatus status);
    void processPendingOrders();
    Page<OrderDto> getAllOrder(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    Page<OrderDto> filterOders(Integer pageNumber, Integer pageSize,String sortDir, String sortBy, Long id, Long orderCode, OderStatus status, LocalDate startDay, LocalDate endDay);
}
