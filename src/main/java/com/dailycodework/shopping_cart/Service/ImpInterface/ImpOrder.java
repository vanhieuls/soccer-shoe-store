package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Dto.OrderDto;
import com.dailycodework.shopping_cart.Entity.*;
import com.dailycodework.shopping_cart.Enum.OderStatus;
import com.dailycodework.shopping_cart.Enum.PaymentMethod;
import com.dailycodework.shopping_cart.Enum.PaymentStatus;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Helper.OrderSpecification.OrderSpecification;
import com.dailycodework.shopping_cart.Helper.ProductSpecification.ProductSpecification;
import com.dailycodework.shopping_cart.Mapper.OrderMapper;
import com.dailycodework.shopping_cart.Repository.*;
import com.dailycodework.shopping_cart.Service.Interface.ICart;
import com.dailycodework.shopping_cart.Service.Interface.IOrder;
import com.dailycodework.shopping_cart.Service.Interface.IPayment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dailycodework.shopping_cart.Enum.OderStatus.PENDING;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ImpOrder implements IOrder {
//    PaymentRepository paymentRepository;
//    IPayment PaymentService;
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
    public Page<OrderDto> getOrdersByUserId(Integer pageNumber, Integer pageSize, Long userId) {
        return null;
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
        return null;
    }
//    @Override
//    public OrderDto testApplyVoucher(Long orderId, Long voucherId) {
//        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
//        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(()->new AppException(ErrorCode.VOUCHER_NOT_EXIST));
//        if(!voucher.isActive()||voucher.getUsageLimit()<=voucher.getUsedCount()||voucher.getStartDate().isAfter(LocalDateTime.now())||voucher.getEndDate().isBefore(LocalDateTime.now())){
//            throw new AppException(ErrorCode.VOUCHER_INVALID);
//        }
//        if (voucher.getMinOrderAmount() !=null && order.getTotalAmount().compareTo(voucher.getMinOrderAmount())<0) {
//            throw new RuntimeException("Order amount too low for this voucher");
//        }
//        BigDecimal discount = voucher.isPercentTage()?order.getTotalAmount().multiply(voucher.getDiscountAmount()).divide(BigDecimal.valueOf(100)): voucher.getDiscountAmount();
//        if(voucher.getMaxDiscountAmount()==null){
//            discount = discount.min(order.getTotalAmount());
//        }
//        else discount = discount.min(voucher.getMaxDiscountAmount());
//        // Gán voucher vào order
////        order.setVoucher(voucher);
//        order.setDiscountApplied(discount);
//        order.setTotalAmount(order.getTotalAmount().subtract(discount));
//        return orderMapper.toOrderDto(order);
//    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, OderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getOderStatus() == OderStatus.CANCELLED) {
            throw new AppException(ErrorCode.ORDER_CANCELLED);
        }
        if((order.getOderStatus() == OderStatus.SHIPPING || order.getOderStatus() == OderStatus.DELIVERED)&& status == OderStatus.CANCELLED) {
            throw new AppException(ErrorCode.CAN_NOT_CANCELLED_AFTER_SHIPPED);
        }
        // Nếu hủy đơn hàng thì hoàn lại số lượng sản phẩm
        if(status == OderStatus.CANCELLED) {
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
    public Page<OrderDto> findOrderByNameAndDate(Integer pageNumber, Integer pageSize, String name, String startDay, String endDay) {
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
        specification = specification.and(OrderSpecification.OrderSpecification(name, start, end));

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
    public void requestCancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getOderStatus() == OderStatus.PENDING) {
            order.setOderStatus(OderStatus.CANCELLED);
            order.setUpdatedAt(LocalDateTime.now());
            // Hoàn lại số lượng sản phẩm
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
            orderRepository.save(order);
            return;
        }
        if (order.getOderStatus() == OderStatus.CANCELLED) {
            throw new AppException(ErrorCode.ORDER_CANCELLED);
        }
        if((order.getOderStatus() == OderStatus.SHIPPING || order.getOderStatus() == OderStatus.DELIVERED)) {
            throw new AppException(ErrorCode.CAN_NOT_CANCELLED_AFTER_SHIPPED);
        }
        order.setOderStatus(OderStatus.CANCEL_REQUESTED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public void ApproveCancelOrder(Long orderId) {
        //nếu mún thêm từ chối yêu càua hủy đơn hàng thì nên , boolean isApproved để true thì cho hủy và ngược lại
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getOderStatus() != OderStatus.CANCEL_REQUESTED) {
            throw new AppException(ErrorCode.ORDER_NOT_IN_CANCEL_REQUESTED_STATUS);
        }
        // Hoàn lại số lượng sản phẩm
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
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderDto placeOrderWithOptionalVoucher(Long userId, Long userAddressId, Long voucherId) {
        // 1. Lấy địa chỉ người dùng
        Address userAddress = addressRepository.findById(userAddressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        String address = userAddress.getAddressLine() + ", "
                + userAddress.getWardCommune() + ", "
                + userAddress.getState() + ", "
                + userAddress.getCountry();

        // 2. Lấy giỏ hàng user
        Cart cart = cartService.getCartByUserId(userId);
        if(cart.getCartItems().stream().noneMatch(CartItem::isSelected)) {
            throw new AppException(ErrorCode.DO_NOT_HAVE_SELECTED_CART_ITEM_TO_CREATE_ORDER);
        }
//        if(cart.getCartItems().isEmpty()) {
//            throw new AppException(ErrorCode.DO_NOT_HAVE_CART_ITEM_TO_CREATE_ORDER);
//        }
        Set<CartItem> selectedItems = new HashSet<>(
                cart.getCartItems().stream().filter(CartItem::isSelected).toList()
        );

        // 3. Tạo Order
        Order order = Order.builder()
                .oderStatus(PENDING)
                .user(cart.getUser())
                .shippingAddress(address)
                .build();

        // 4. Map cartItems -> orderItems
        Set<OrderItem> orderItemSet = mapCartItemsToOrderItems(selectedItems, order);
        order.setOrderItems(orderItemSet);

        // 5. Tính tổng tiền trước discount
        BigDecimal itemsTotal = calculateTotalAmount(orderItemSet);
        BigDecimal shippingFee = BigDecimal.valueOf(20000);
        BigDecimal totalBeforeDiscount = itemsTotal.add(shippingFee);

        order.setShippingFee(shippingFee);
        order.setTotalAmount(totalBeforeDiscount);

        // 6. Nếu có voucherId thì kiểm tra và áp dụng
        if (voucherId != null) {
            Voucher voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXIST));

            User user = order.getUser();

            // Validate voucher
            if (!voucher.getUsers().contains(user)) {
                throw new AppException(ErrorCode.VOUCHER_NOT_OWNED);
            }
            if (!voucher.isActive()) {
                throw new AppException(ErrorCode.VOUCHER_NOT_ACTIVE);
            }
            if (voucher.getUsageLimit() <= voucher.getUsedCount()) {
                throw new AppException(ErrorCode.VOUCHER_USAGE_LIMIT_EXCEEDED);
            }
            if (voucher.getStartDate().isAfter(LocalDateTime.now())
                    || voucher.getEndDate().isBefore(LocalDateTime.now())) {
                throw new AppException(ErrorCode.EXPIRED_VOUCHER);
            }
            if (voucher.getMinOrderAmount() != null
                    && totalBeforeDiscount.compareTo(voucher.getMinOrderAmount()) < 0) {
                throw new AppException(ErrorCode.ORDER_AMOUNT_TOO_LOW);
            }

            // Tính discount
            BigDecimal discount = voucher.isPercentTage()
                    ? totalBeforeDiscount.multiply(voucher.getDiscountAmount())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                    : voucher.getDiscountAmount();

            if (voucher.getMaxDiscountAmount() != null) {
                discount = discount.min(voucher.getMaxDiscountAmount());
            } else {
                discount = discount.min(totalBeforeDiscount);
            }

            // Áp dụng discount
            order.setDiscountApplied(discount);
            order.setTotalAmount(totalBeforeDiscount.subtract(discount));

            // Cập nhật voucher
            voucher.setUsedCount(voucher.getUsedCount() + 1);
            voucher.getUsers().remove(user); // remove voucher khỏi user đã dùng
            voucherRepository.save(voucher);

            // Cập nhật điểm voucher user
            user.setPointVoucher(user.getPointVoucher() - voucher.getPointRequired());
            userRepository.save(user);
        }

        // 7. Clear cart đã chọn
        cartService.clearSelectedItems(cart.getId());
        BigDecimal remainingAmount = cart.getCartItems().stream()
                .filter(item -> !item.isSelected())
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(remainingAmount);
        cartRepository.save(cart);

        // 8. Cộng điểm voucher cho user vì đã đặt hàng
        User user = cart.getUser();
        user.setPointVoucher(user.getPointVoucher() + 1);
        userRepository.save(user);

//        // 9. Tạo Payment cho order
//        Payment payment = Payment.builder()
//                .order(order)
//                .amount(order.getTotalAmount())
//                .amountPaid(BigDecimal.ZERO)
//                .amountRemaining(order.getTotalAmount())
//                .method(PaymentMethod.COD)
//                .status(PaymentStatus.UNPAID)
//                .build();
//        order.setPayment(payment); // set 2 chiều

        // 10. Save order cuối cùng
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderDto(savedOrder);
    }
//    private Order createOrUpdatePaymentForOrder(Order order, String method) {
//
//        if(method == "COD") {
//
//            Payment payment = Payment.builder()
//                    .order(order)
//                    .amount(order.getTotalAmount())
//                    .amountPaid(BigDecimal.ZERO)
//                    .amountRemaining(order.getTotalAmount())
//                    .method(PaymentMethod.valueOf(method))  // set theo tham số
//                    .status(PaymentStatus.UNPAID)
//                    .build();
//            order.setPayment(payment);
//            paymentRepository.save(payment);
//            return orderRepository.save(order);
//        }
//        else{
//
//            return orderRepository.save(order);
//        }
//
//    }

}
