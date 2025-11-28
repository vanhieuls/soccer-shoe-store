package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.OrderDto;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.Interface.IOrder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    IOrder orderService;
    @Operation(summary = "Tạo đơn hàng từ giỏ hàng của người dùng")
    @PostMapping("/create/{userId}/{userAddressId}")
    public ApiResponse<OrderDto> createOrder(@PathVariable Long userId, @PathVariable Long userAddressId) {
        try {
            OrderDto order = orderService.placeOrder(userId, userAddressId);
            return ApiResponse.<OrderDto>builder()
                    .code(200)
                    .message("create success")
                    .result(order)
                    .build();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    @Operation(summary = "Lấy đơn hàng theo id")
    @GetMapping("/get-order/{orderId}")
    public ApiResponse<OrderDto> getOrderById(@PathVariable Long orderId) {
        return ApiResponse.<OrderDto>builder()
                .code(200)
                .message("get success")
                .result(orderService.getOrder(orderId))
                .build();
    }
    @Operation(summary = "Lấy lịch sử đơn hàng của người dùng")
    @GetMapping("/history-order/{userId}")
    public ApiResponse<Page<OrderDto>> getUserOrders(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        Page<OrderDto>  orderDtos = orderService.getAllOrderPaganationByUser(page, size, userId);
        if(orderDtos.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return ApiResponse.<Page<OrderDto>>builder()
                .code(200)
                .message("get history order of user" + userId + " success")
                .result(orderDtos)
                .build();
    }
    @Operation(summary = "Áp dụng voucher cho đơn hàng")
    @PostMapping("/applyVoucher/{orderId}/{voucherId}/{userAddressId}")
    public ApiResponse<OrderDto> applyVoucher(@PathVariable Long orderId,@PathVariable Long voucherId, @PathVariable Long userAddressId) {
        return ApiResponse.<OrderDto>builder()
                .code(200)
                .message("apply voucher success")
                .result(orderService.applyVoucher(orderId,voucherId, userAddressId))
                .build();
    }
//    @PostMapping("/applyVoucher/{orderId}")
//    public ApiResponse<OrderDto> testApplyVoucher(@PathVariable Long orderId,@RequestParam Long voucherCode) {
//        return ApiResponse.<OrderDto>builder()
//                .code(200)
//                .message("Test apply voucher success")
//                .result(orderService.testApplyVoucher(orderId,voucherCode))
//                .build();
//    }
    @PreAuthorize("hasRole('STAFF')")
    @Operation(summary = "Cập nhật trạng thái đơn hàng, dành cho trang admin (nhân viên có thể dô cập nhật trạng thái đơn hàng)")
    @PutMapping("/update-order-status/{orderId}")
    public ApiResponse<OrderDto> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return ApiResponse.<OrderDto>builder()
                .code(200)
                .message("update order status success")
                .result(orderService.updateOrderStatus(orderId, OderStatus.valueOf(status)))
                .build();
    }
    @GetMapping("/get-order-status/{userId}")
    public ApiResponse<List<OrderDto>> getOrderStatus(@PathVariable Long userId, @RequestParam String status) {
        List<OrderDto> orderDtos = orderService.getOrderStatus(userId, OderStatus.valueOf(status));
        if(orderDtos.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return ApiResponse.<List<OrderDto>>builder()
                .code(200)
                .message("get order status success")
                .result(orderDtos)
                .build();
    }
    @Operation(summary = "Lấy đơn hàng theo id của người dùng và trạng thái của đơn hàng đó (PENDING, CONFIRMED, PROCESSING, SHIPPING, DELIVERED, CANCELLED, CANCEL_REQUESTED)")
    @GetMapping("/order-user-status/{userId}")
    public ApiResponse<Page<OrderDto>> getOrdersByUserAndStatus(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,     // có thể null
            @RequestParam(defaultValue = "0") Integer page,       // trang hiện tại
            @RequestParam(defaultValue = "10") Integer size     // số item / trang
    ) {
        Page<OrderDto> orderPage = orderService.getOrderPaganation(page, size,userId, OderStatus.valueOf(status));
        return ApiResponse.<Page<OrderDto>>builder()
                .code(200)
                .message("get order pagination success")
                .result(orderPage)
                .build();
    }
    @Operation(summary = "Tìm kiếm đơn hàng theo tên sản phẩm và khoảng thời gian (dành cho trang user)")
    @GetMapping("/find-Order-By-Name-And-Date/{userId}")
    public ApiResponse<Page<OrderDto>> findOrderByNameAndDate(
            @PathVariable Long userId,
            @RequestParam(required = false) OderStatus status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String startDay,
            @RequestParam(required = false) String endDay,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<OrderDto> orderPage = orderService.findOrderByNameAndDate( pageNumber, pageSize, status ,name, startDay, endDay, userId);
        if(orderPage.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return ApiResponse.<Page<OrderDto>>builder()
                .code(200)
                .message("find order by name and date success")
                .result(orderPage)
                .build();
    }
    @Operation(summary = "Yêu cầu hủy đơn hàng")
    @PatchMapping("/request-cancel-order/{orderId}")
    public ApiResponse<OrderDto> requestCancelOrder(@PathVariable Long orderId) {
        return ApiResponse.<OrderDto>builder()
                .code(200)
                .message("request cancel order success")
                .result(orderService.requestCancelOrder(orderId))
                .build();
    }
    @PreAuthorize("hasRole('STAFF')")
    @Operation(summary = "Xác nhận hủy đơn hàng")
    @PatchMapping("/confirm-cancel-order/{orderId}")
    public ApiResponse<OrderDto> confirmCancelOrder(@PathVariable Long orderId) {
        return ApiResponse.<OrderDto>builder()
                .code(200)
                .message("confirm cancel order success")
                .result(orderService.confirmCancelOrder(orderId))
                .build();
    }
    @Operation(summary = "Lấy tổng doanh thu trong khoảng thời gian (gọi cho trang chủ của trang admin)")
    @GetMapping("/total-revenue")
    public ApiResponse<BigDecimal> getTotalRevenue(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        BigDecimal totalRevenue = orderService.getTotalRevenue(startDate, endDate);
        return ApiResponse.<BigDecimal>builder()
                .code(200)
                .message("get total revenue success")
//                .result(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .result(totalRevenue)
                .build();
    }
    @PreAuthorize("hasRole('STAFF')")
    @Operation(summary = "Đếm số đơn hàng theo trạng thái (gọi cho trang chủ của trang admin)")
    @GetMapping("/count-by-status")
    public ApiResponse<Long> countOrdersByStatus(@RequestParam OderStatus status) {
        Long count = orderService.countOrdersByStatus(status);
        return ApiResponse.<Long>builder()
                .code(200)
                .message("count orders by status success")
                .result(count)
                .build();
    }
    @PreAuthorize("hasRole('STAFF')")
    @Operation(summary = "Lấy tất cả đơn hàng với phân trang và sắp xếp (dành cho trang admin)")
    @GetMapping("/get-orders")
    public ApiResponse<Page<OrderDto>> getAll(@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize,
                                              @RequestParam(required = false) String sortBy, @RequestParam(required = false, defaultValue = "desc") String sortDir) {
        Page<OrderDto> orders = orderService.getAllOrder(pageNumber, pageSize, sortBy, sortDir);
        if(orders.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return ApiResponse.<Page<OrderDto>>builder()
                .code(200)
                .message("get orders by status success")
                .result(orders)
                .build();
    }
    @PreAuthorize("hasRole('STAFF')")
    @Operation(summary = "Tìm kiếm đơn hàng theo id, mã đơn hàng, trạng thái Dơn hàng và khoảng thời gian (dành cho trang admin)")
    @GetMapping("/search-orders")
    public ApiResponse<Page<OrderDto>> searchOrders(
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long orderCode,
            @RequestParam(required = false) OderStatus status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDay,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDay,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<OrderDto> orderPage = orderService.filterOders(pageNumber, pageSize, sortDir, sortBy, id, orderCode, status, startDay, endDay);
        if(orderPage.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return ApiResponse.<Page<OrderDto>>builder()
                .code(200)
                .message("search orders success")
                .result(orderPage)
                .build();
    }
}
