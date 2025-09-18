package com.dailycodework.shopping_cart.Exception;

import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandleException {
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException (AppException appException){
        ApiResponse apiResponse = ApiResponse.builder()
                .code(appException.getErrorCode().getCode())
                .message(appException.getErrorCode().getMessage())
                .build();
        return ResponseEntity.status(appException.getErrorCode().getHttpStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handlingIllegalArgumentException(IllegalArgumentException illegalArgumentException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                .message(illegalArgumentException.getMessage())
                .build());
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(400)
                .message(methodArgumentNotValidException.getFieldError().getDefaultMessage())
                .build());
    }
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handlingHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException){
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(400)
                .message(httpMessageNotReadableException.getHttpInputMessage().getHeaders().getContentType().toString() + " is not supported")
                .build());
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException accessDeniedException){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }
}
