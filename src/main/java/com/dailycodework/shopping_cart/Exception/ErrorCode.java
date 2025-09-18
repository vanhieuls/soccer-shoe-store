package com.dailycodework.shopping_cart.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    PRODUCT_NOT_EXIST (100,"Product not exist", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED (100,"Product existed", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND (100,"Product not found", HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED (102,"Category existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND (101,"Category not found", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND (101,"Cart not found", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND (101,"Order not found", HttpStatus.NOT_FOUND),
    CART_NOT_EXIST (101,"Cart not exist", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_EXIST (101,"Cart item not exist", HttpStatus.NOT_FOUND),
    USER_NOT_EXIST (101,"User not exist", HttpStatus.NOT_FOUND),
    EMAIL_EXISTED (101,"Email existed", HttpStatus.CREATED),
    USER_NOT_FOUND (101,"User not found", HttpStatus.NOT_FOUND),
    IMAGE_NOT_FOUND (101,"Image not found", HttpStatus.NOT_FOUND),
    STATUS_ORDER_NOT_SUCCESS (104,"Just rate product when it delivered", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_EXIST(105,"voucher not found",HttpStatus.NOT_FOUND),
    VOUCHER_INVALID(106,"voucher invalid",HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_VERIFY(107,"Account not verified. Plaese verify your account", HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_VERIFIED(110,"Account already verified", HttpStatus.BAD_REQUEST),
    INVALID_VERIFICATION_CODE(108,"Invalid verification code", HttpStatus.BAD_REQUEST),
    VERIFICATION_CODE_EXPIRED(109,"Verification code has expired", HttpStatus.BAD_REQUEST),
    USER_EXISTED(111,"User existed", HttpStatus.BAD_REQUEST),
    SIZE_EXISTED(109,"Size existed", HttpStatus.BAD_REQUEST),
    SIZE_NOT_EXIST(110,"Size not exist", HttpStatus.BAD_REQUEST),
    SIZE_EMPTY(117,"Size empty", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(112,"Old passwords do not correct", HttpStatus.BAD_REQUEST),
    INVALID_CONFIRM_PASSWORD(113,"New and confirm passwords do not match", HttpStatus.BAD_REQUEST),
    TOKEN_RESET_PASSWORD_INVALID(114,"Token reset password expired", HttpStatus.BAD_REQUEST),
    PRODUCT_SIZE_NOT_EXIST(115,"Product size not exist", HttpStatus.BAD_REQUEST),
    PRODUCT_SIZE_EXISTED(116,"Product size existed", HttpStatus.BAD_REQUEST),
    PRODUCT_SIZE_NOT_FOUND(118,"Product size not found", HttpStatus.NOT_FOUND),
    COLLECTION_NOT_FOUND(120,"Collection not found", HttpStatus.NOT_FOUND),
    SIZE_INVALID(119,"Size invalid", HttpStatus.BAD_REQUEST),
    COLLECTION_EXISTED(121,"Collection existed", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND(122,"Address not found", HttpStatus.NOT_FOUND),
    ORDER_CANCELLED(123,"Order has been cancelled", HttpStatus.BAD_REQUEST),
    ORDER_AMOUNT_ZERO(123,"Order amount is zero", HttpStatus.BAD_REQUEST),
    CAN_NOT_CANCELLED_AFTER_SHIPPED(124,"Can not cancelled after shipped", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_USED(125,"Voucher already used", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_FOUND(126,"Voucher not found", HttpStatus.NOT_FOUND),
    VOUCHER_NOT_ACTIVE(127,"Voucher not active", HttpStatus.BAD_REQUEST),
    VOUCHER_USAGE_LIMIT_EXCEEDED(128,"Voucher usage limit exceeded", HttpStatus.BAD_REQUEST),
    EXPIRED_VOUCHER(129,"Voucher expired", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_OWNED(130,"Voucher not owned by user", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_POINTS(131,"Insufficient points to redeem voucher", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_EXISTED_IN_USER(132,"Voucher already existed in user", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(132,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(133,"You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    INAPPROPRIATE_CONTENT(134,"Inappropriate content in comment", HttpStatus.BAD_REQUEST),
    LINK_IN_COMMENT(135,"Link is not allowed in comment", HttpStatus.BAD_REQUEST),
    ORDER_AMOUNT_TOO_LOW(136,"Order amount is too low to apply voucher", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND(137,"Payment not found", HttpStatus.NOT_FOUND),
    PAYMENT_METHOD_NOT_SUPPORTED(138,"Payment method not supported", HttpStatus.BAD_REQUEST),
    PERMISSION_EXISTED(139,"Permission existed", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXIST(140,"Permission not exist", HttpStatus.NOT_FOUND),
    ADDRESS_NOT_SAVE(141,"Address not save", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(142,"Review not found", HttpStatus.NOT_FOUND),
    DO_NOT_HAVE_CART_ITEM_TO_CREATE_ORDER(143,"Do not have cart item to create order", HttpStatus.BAD_REQUEST),
    DO_NOT_HAVE_SELECTED_CART_ITEM_TO_CREATE_ORDER(144,"Do not have selected cart item to create order", HttpStatus.BAD_REQUEST),
    ORDER_NOT_IN_CANCEL_REQUESTED_STATUS(145,"Order not in cancel requested status", HttpStatus.BAD_REQUEST),;
    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;
    ErrorCode(int code, String message, HttpStatusCode httpStatusCode){
        this.code=code;
        this.message=message;
        this.httpStatusCode= httpStatusCode;
    }
}
