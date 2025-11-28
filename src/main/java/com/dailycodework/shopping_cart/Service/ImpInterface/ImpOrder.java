package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Dto.OrderDto;
import com.dailycodework.shopping_cart.Entity.*;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Helper.OrderSpecification.OrderSpecification;
import com.dailycodework.shopping_cart.Mapper.OrderMapper;
import com.dailycodework.shopping_cart.Repository.*;
import com.dailycodework.shopping_cart.Service.Interface.ICart;
import com.dailycodework.shopping_cart.Service.Interface.IOrder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dailycodework.shopping_cart.Enum.OderStatus.CONFIRMED;
import static com.dailycodework.shopping_cart.Enum.OderStatus.PENDING;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ImpOrder implements IOrder {
    OrderRepository orderRepository;
    ProductRepository productRepository;
    CartRepository cartRepository;
    ICart cartService;
    OrderMapper orderMapper;
    VoucherRepository voucherRepository;
    ProductSizeRepository productSizeRepository;
    UserRepository userRepository;
    AddressRepository addressRepository;
    @Override
//    public OrderDto placeOrder(Long userId) {
//        Cart cart = cartService.getCartByUserId(userId);
////        User user = cartService.getUserByUserId(userId);
//        Set<CartItem> selectedItems = new HashSet<>(cart.getCartItems().stream().filter(CartItem::isSelected).toList());
//        // Tính tổng tiền các item đã chọn
//        BigDecimal selectedTotalAmount = selectedItems.stream()
//                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        Order order = Order.builder()
//                .oderStatus(PENDING)
//                .user(cart.getUser())
//                .build();
//        Set<OrderItem> orderItemSet =  mapCartItemsToOrderItems(selectedItems,order);
//        List<OrderItem> orderItemList = createOrderItems(order,cart);
//        // chuyển list sang set dùng new hashset<>(...)
////        order.setOrderItems(new HashSet<>(orderItemList));
//        order.setOrderItems(orderItemSet);
//        order.setShippingFee(BigDecimal.valueOf(20000));
//        order.setTotalAmount(calculateTotalAmount(orderItemSet).add(order.getShippingFee()));
//        Order saveOder = orderRepository.save(order);
//        cartService.clearSelectedItems(cart.getId());
////        cart.setTotalAmount(BigDecimal.ZERO);
//        // Tính lại tổng tiền giỏ còn lại từ các item chưa chọn
//        BigDecimal remainingAmount = cart.getCartItems().stream()
//                .filter(item -> !item.isSelected()) // chỉ lấy item còn lại
//                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        cart.setTotalAmount(remainingAmount);
////        cart.setTotalAmount(cart.getTotalAmount().subtract(selectedTotalAmount));
//        cartRepository.save(cart);
//        return orderMapper.toOrderDto(saveOder);
//    }
    public OrderDto placeOrder(Long userId, Long userAddressId) {
        Address userAddress = addressRepository.findById(userAddressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        String address = userAddress.getAddressLine() + ", " + userAddress.getWardCommune() + ", " + userAddress.getState() + ", " + userAddress.getCountry();
        Cart cart = cartService.getCartByUserId(userId);
//        User user = cartService.getUserByUserId(userId);
        Set<CartItem> selectedItems = new HashSet<>(cart.getCartItems().stream().filter(CartItem::isSelected).toList());
        // Tính tổng tiền các item đã chọn
        BigDecimal selectedTotalAmount = selectedItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .oderStatus(PENDING)
                .user(cart.getUser())
                .build();
        Set<OrderItem> orderItemSet =  mapCartItemsToOrderItems(selectedItems,order);
        List<OrderItem> orderItemList = createOrderItems(order,cart);
        // chuyển list sang set dùng new hashset<>(...)
//        order.setOrderItems(new HashSet<>(orderItemList));
        order.setOrderItems(orderItemSet);
        order.setShippingFee(BigDecimal.valueOf(20000));
        order.setTotalAmount(calculateTotalAmount(orderItemSet).add(order.getShippingFee()));
        order.setShippingAddress(address);
        Order saveOder = orderRepository.save(order);
        cartService.clearSelectedItems(cart.getId());
//        cart.setTotalAmount(BigDecimal.ZERO);
        // Tính lại tổng tiền giỏ còn lại từ các item chưa chọn
        BigDecimal remainingAmount = cart.getCartItems().stream()
                .filter(item -> !item.isSelected()) // chỉ lấy item còn lại
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(remainingAmount);
//        cart.setTotalAmount(cart.getTotalAmount().subtract(selectedTotalAmount));
        User user = cart.getUser();
        user.setPointVoucher(user.getPointVoucher() + 1);// Giả sử 1000đ = 1 điểm
        userRepository.save(user);
        cartRepository.save(cart);
        return orderMapper.toOrderDto(saveOder);
    }
    private Set<OrderItem> mapCartItemsToOrderItems(Set<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem ->{
                    Product product = cartItem.getProduct();
                    ProductSize productSize = cartItem.getProductSize();
                    if (productSize == null) {
                        throw new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND);
                    }
                    productSize.setQuantity(productSize.getQuantity() - cartItem.getQuantity());
                    product.setInventory(product.getInventory()-cartItem.getQuantity());
                    return OrderItem.builder()
                        .order(order) // liên kết Order hiện tại
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getUnitPrice()).productSize(productSize)
                        .build();
                })
                .collect(Collectors.toSet());
    }
    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getCartItems().stream().map(cartItem -> {
            ProductSize productSize = cartItem.getProductSize();
            productSize.setQuantity(productSize.getQuantity() - cartItem.getQuantity());
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            productSizeRepository.save(productSize);
            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productSize(productSize)
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getUnitPrice())
                    .build();
        }).toList();


    }

    private List<OrderItem> createOrderItem(Order order, Cart cart){
        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);

            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getUnitPrice())
                    .productSize(cartItem.getProductSize())
                    .build();
        }).toList();

    }

    private BigDecimal calculateTotalAmount (Set<OrderItem> orderItemList){
        if (orderItemList == null || orderItemList.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return orderItemList.stream().map(orderItem -> orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
    @Override
    public OrderDto getOrder(Long orderId) {
        return orderMapper.toOrderDto(orderRepository.findById(orderId).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND)));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        return orderMapper.toListOrderDto(orderRepository.findByUserId(userId));
    }

    @Override
//    public OrderDto applyVoucher(Long orderId, Long voucherId) {
//        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
//        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(()->new AppException(ErrorCode.VOUCHER_NOT_EXIST));
//        User user = order.getUser();
//        if (!voucher.getUsers().contains(user)) {
//            throw new AppException(ErrorCode.VOUCHER_NOT_OWNED);
//        }
//        if(!voucher.isActive()){
//            throw new AppException(ErrorCode.VOUCHER_NOT_ACTIVE);
//        }
//        else if(voucher.getUsageLimit()<=voucher.getUsedCount()){
//            throw new AppException(ErrorCode.VOUCHER_USAGE_LIMIT_EXCEEDED);
//        }
//        else if(voucher.getStartDate().isAfter(LocalDateTime.now())||voucher.getEndDate().isBefore(LocalDateTime.now())){
//            throw new AppException(ErrorCode.EXPIRED_VOUCHER);
//        }
//        if (voucher.getMinOrderAmount() !=null && order.getTotalAmount().compareTo(voucher.getMinOrderAmount())<0) {
//            throw new RuntimeException("Order amount too low for this voucher");
//        }
//        BigDecimal discount = voucher.isPercentTage()?order.getTotalAmount().multiply(voucher.getDiscountAmount()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP): voucher.getDiscountAmount();
//        if(voucher.getMaxDiscountAmount()==null){
//            discount = discount.min(order.getTotalAmount());
//        }
//        else discount = discount.min(voucher.getMaxDiscountAmount());
//        // Gán voucher vào order
////        order.setVoucher(voucher);
//        order.setDiscountApplied(discount);
//        // Cập nhật phí vận chuyển
////        BigDecimal shippingFee = order.getShippingFee();
//        order.setShippingFee(BigDecimal.valueOf(20000));
//        // Cập nhật tổng tiền đơn hàng
//        // Trừ đi discount từ tổng tiền đơn hàng
////        if(order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
////            throw new AppException(ErrorCode.ORDER_AMOUNT_ZERO);
////        }
//        order.setTotalAmount(order.getTotalAmount().subtract(discount));
//        voucher.setUsedCount(voucher.getUsedCount()+1);
//        voucherRepository.save(voucher);
//        return orderMapper.toOrderDto(orderRepository.save(order));
//    }
    public OrderDto applyVoucher(Long orderId, Long voucherId, Long userAddressId) {
        Address userAddress = addressRepository.findById(userAddressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(()->new AppException(ErrorCode.VOUCHER_NOT_EXIST));
        User user = order.getUser();
        String address = userAddress.getAddressLine() + ", " + userAddress.getWardCommune() + ", " + userAddress.getState() + ", " + userAddress.getCountry();
        if (!voucher.getUsers().contains(user)) {
            throw new AppException(ErrorCode.VOUCHER_NOT_OWNED);
        }
        if(!voucher.isActive()){
            throw new AppException(ErrorCode.VOUCHER_NOT_ACTIVE);
        }
        else if(voucher.getUsageLimit()<=voucher.getUsedCount()){
            throw new AppException(ErrorCode.VOUCHER_USAGE_LIMIT_EXCEEDED);
        }
        else if(voucher.getStartDate().isAfter(LocalDateTime.now())||voucher.getEndDate().isBefore(LocalDateTime.now())){
            throw new AppException(ErrorCode.EXPIRED_VOUCHER);
        }
        if (voucher.getMinOrderAmount() !=null && order.getTotalAmount().compareTo(voucher.getMinOrderAmount())<0) {
            throw new RuntimeException("Order amount too low for this voucher");
        }
        BigDecimal discount = voucher.isPercentTage()?order.getTotalAmount().multiply(voucher.getDiscountAmount()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP): voucher.getDiscountAmount();
        if(voucher.getMaxDiscountAmount()==null){
            discount = discount.min(order.getTotalAmount());
        }
        else discount = discount.min(voucher.getMaxDiscountAmount());
        // Gán voucher vào order
//        order.setVoucher(voucher);
        order.setDiscountApplied(discount);
        // Cập nhật phí vận chuyển
//        BigDecimal shippingFee = order.getShippingFee();
        order.setShippingFee(BigDecimal.valueOf(20000));
        // Cập nhật tổng tiền đơn hàng
        // Trừ đi discount từ tổng tiền đơn hàng
//        if(order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
//            throw new AppException(ErrorCode.ORDER_AMOUNT_ZERO);
//        }
        order.setShippingAddress(address);
        order.setTotalAmount(order.getTotalAmount().subtract(discount));
        voucher.setUsedCount(voucher.getUsedCount()+1);
        voucher.getUsers().remove(user); // Xóa voucher khỏi danh sách voucher của user
        voucherRepository.save(voucher);
        //cap nhat laij diem voucher usser
        user.setPointVoucher(user.getPointVoucher() - voucher.getPointRequired());
        userRepository.save(user);
        return orderMapper.toOrderDto(orderRepository.save(order));
    }
    @Override
    public OrderDto testApplyVoucher(Long orderId, Long voucherId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(()->new AppException(ErrorCode.VOUCHER_NOT_EXIST));
        if(!voucher.isActive()||voucher.getUsageLimit()<=voucher.getUsedCount()||voucher.getStartDate().isAfter(LocalDateTime.now())||voucher.getEndDate().isBefore(LocalDateTime.now())){
            throw new AppException(ErrorCode.VOUCHER_INVALID);
        }
        if (voucher.getMinOrderAmount() !=null && order.getTotalAmount().compareTo(voucher.getMinOrderAmount())<0) {
            throw new RuntimeException("Order amount too low for this voucher");
        }
        BigDecimal discount = voucher.isPercentTage()?order.getTotalAmount().multiply(voucher.getDiscountAmount()).divide(BigDecimal.valueOf(100)): voucher.getDiscountAmount();
        if(voucher.getMaxDiscountAmount()==null){
            discount = discount.min(order.getTotalAmount());
        }
        else discount = discount.min(voucher.getMaxDiscountAmount());
        // Gán voucher vào order
//        order.setVoucher(voucher);
        order.setDiscountApplied(discount);
        order.setTotalAmount(order.getTotalAmount().subtract(discount));
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, OderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getOderStatus() == OderStatus.CANCELLED) {
            throw new AppException(ErrorCode.ORDER_CANCELLED);
        }
        if((order.getOderStatus() == OderStatus.SHIPPING || order.getOderStatus() == OderStatus.DELIVERED)&& status == OderStatus.CANCELLED) {
            throw new AppException(ErrorCode.CAN_NOT_CANCELLED_AFTER_SHIPPED);
        }
        for (OrderItem orderItem : order.getOrderItems()) {
            ProductSize productSize = orderItem.getProductSize();
            if (productSize == null) {
                throw new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND);
            }
            productSize.setQuantity(productSize.getQuantity() + orderItem.getQuantity());
            productSizeRepository.save(productSize);
            Product product = orderItem.getProduct();
            product.setInventory(product.getInventory() + orderItem.getQuantity());
            productRepository.save(product);
        }
        order.setOderStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getOrderStatus(Long userId, OderStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Order> orders = orderRepository.findByUserAndOderStatus(user, status).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toListOrderDto(orders);
    }

    @Override
    public Page<OrderDto> getAllOrderPaganationByUser(Integer pageNumber, Integer pageSize, Long userId) {
        Pageable pageable = null;
        if (pageNumber == null || pageNumber < 0) pageNumber = 0;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        // Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        pageable = PageRequest.of(pageNumber, pageSize);
        // Query theo user + status + phân trang
        Page<Order> orderPage = orderRepository.findAllStatusByUser(user, pageable);
        return orderPage.map(orderMapper::toOrderDto);
    }

    @Override
    public Page<OrderDto> findOrderByNameAndDate(Integer pageNumber, Integer pageSize, OderStatus orderStatus, String name, String startDay, String endDay, Long userId) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(startDay != null && !startDay.isEmpty()){
            start = LocalDateTime.parse(startDay + " 00:00:00", formatter);
        }
        if(endDay != null && !endDay.isEmpty()){
            end = LocalDateTime.parse(endDay + " 23:59:59", formatter);
        }
        Specification<Order> specification = Specification.where(null);
        specification = specification.and(OrderSpecification.OrderSpecification(userId, name, start, end));
        specification = specification.and(OrderSpecification.hasStatus(orderStatus));
        Pageable pageable = null;
        if (pageNumber == null || pageNumber < 0) pageNumber = 0;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findAll(specification, pageable);
        return orderPage.map(orderMapper::toOrderDto);
    }

    @Override
    public Page<OrderDto> getOrderPaganation(Integer pageNumber, Integer pageSize, Long userId, OderStatus status) {
        //        Pageable         -> Chỉ định yêu cầu phân trang (số trang, kích thước, sắp xếp)
//PageRequest      -> Tạo cụ thể đối tượng Pageable
//Page<T>          -> Kết quả trả về từ phương thức truy vấn (có dữ liệu + thông tin phân trang)
        Pageable pageable = null;
        if (pageNumber == null || pageNumber < 0) pageNumber = 0;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        // Lấy user

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        pageable = PageRequest.of(pageNumber, pageSize);
        // Query theo user + status + phân trang
        Page<Order> orderPage = orderRepository.findByUserAndOderStatus(user, status, pageable);
        return orderPage.map(orderMapper::toOrderDto);
    }

    @Override
    public OrderDto requestCancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getOderStatus() == OderStatus.CANCELLED) {
            throw new AppException(ErrorCode.ORDER_CANCELLED);
        }
        else if(order.getOderStatus() == OderStatus.SHIPPING || order.getOderStatus() == OderStatus.DELIVERED) {
            throw new AppException(ErrorCode.CAN_NOT_CANCELLED_AFTER_SHIPPED);
        }
        order.setOderStatus(OderStatus.CANCEL_REQUESTED);
        order.setUpdatedAt(LocalDateTime.now());
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto confirmCancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getOderStatus() != OderStatus.CANCEL_REQUESTED) {
            throw new AppException(ErrorCode.ORDER_NOT_IN_CANCEL_REQUESTED_STATUS);
        }
        for (OrderItem orderItem : order.getOrderItems()) {
            ProductSize productSize = orderItem.getProductSize();
            if (productSize == null) {
                throw new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND);
            }
            productSize.setQuantity(productSize.getQuantity() + orderItem.getQuantity());
            productSizeRepository.save(productSize);
            Product product = orderItem.getProduct();
            product.setInventory(product.getInventory() + orderItem.getQuantity());
            productRepository.save(product);
        }
        order.setOderStatus(OderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    @Override
    public BigDecimal getTotalRevenue(LocalDate startDate,LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime endExclusive = endDate.plusDays(1).atStartOfDay(); // < end+1d (tránh lệch 23:59:59)
        return orderRepository.getTotalRevenue(OderStatus.DELIVERED, start, endExclusive);
    }

    @Override
    public Long countOrdersByStatus(OderStatus status) {
        if(status == null) {
            throw new RuntimeException("Status must not be null");
        }
        if(status != OderStatus.PENDING && status != CONFIRMED && status != OderStatus.SHIPPING &&
                status != OderStatus.DELIVERED && status != OderStatus.CANCEL_REQUESTED && status != OderStatus.CANCELLED){
            throw new RuntimeException("Invalid status value");
        }
        return orderRepository.countByOderStatus(status);
    }

    @Override
    public void processPendingOrders() {
        System.out.println("Processing Orders");
        List<Order> pendingOrders = orderRepository.findByOderStatus(PENDING);
        pendingOrders.forEach(
                order->{
                    order.setOderStatus(CONFIRMED);
                    System.out.println("Order ID " + order.getId() + " status updated to CONFIRMED");
                    orderRepository.save(order);
                }
        );

        System.out.println("Processed pending orders: " + pendingOrders.size());
    }
    @Override
    public Page<OrderDto> getAllOrder(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Pageable pageable = null;
        if(pageNumber == null || pageNumber < 0){
            pageNumber = 0;
        }
        if(pageSize == null || pageSize <=0){
            pageSize = 10;
        }
        String sortField = (sortBy != null) ? sortBy : "id";
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));
//        Page<Order> orders = orderRepository.findAll(pageable);
        Page<Order> orders = orderRepository.pageOrders(pageable);
        if(orders.isEmpty()){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return orders.map(orderMapper::toOrderDto);
    }

    @Override
    public Page<OrderDto> filterOders(Integer pageNumber, Integer pageSize, String sortDir, String sortBy, Long id, Long orderCode, OderStatus status, LocalDate startDay, LocalDate endDay) {
        Specification<Order> specification = Specification.where(null);
        specification = specification.and(OrderSpecification.filerOrders(id, orderCode, status,  startDay, endDay));
        Pageable pageable = null;
        if (pageNumber == null || pageNumber < 0) pageNumber = 0;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        String sortField = (sortBy != null) ? sortBy : "id";
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));
        Page<Order> orderPage = orderRepository.findAll(specification, pageable);
        return orderPage.map(orderMapper::toOrderDto);
    }

}
