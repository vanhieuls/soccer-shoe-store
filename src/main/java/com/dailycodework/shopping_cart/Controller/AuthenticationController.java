package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.AuthenticationDto;
import com.dailycodework.shopping_cart.DTO.Dto.VerifyUserDto;
import com.dailycodework.shopping_cart.DTO.Request.ChangePasswordRequest;
import com.dailycodework.shopping_cart.DTO.Request.LoginRequest;
import com.dailycodework.shopping_cart.DTO.Request.UserRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Service.Interface.IAuthentication;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    IAuthentication authenticationService;
    @PostMapping("/login")
    public ApiResponse<AuthenticationDto> login(@RequestBody LoginRequest request){
        try{
            AuthenticationDto authenticationDto = authenticationService.login(request);
            return ApiResponse.<AuthenticationDto>builder()
                    .code(200)
                    .message("login success")
                    .result(authenticationDto)
                    .build();
        }
        catch (Exception e){
        return ApiResponse.<AuthenticationDto>builder()
                .code(401)
                .message("Invalids credentials")
                .build();
        }
    }
    @PostMapping("/signup")
    public ApiResponse<UserResponse> signUp(@RequestBody UserRequest request){
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Check Email and verification code")
                .result(authenticationService.signUp(request))
                .build();
    }
    @PutMapping("/resend-email")
    public ApiResponse<Void> reSendVerificationEmail(@RequestParam String email){
        authenticationService.reSendVerificationEmail(email);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Verify code was resend")
                .build();
    }
    @PostMapping("/verify-code")
    public ApiResponse<Void> verifyUser(@RequestBody VerifyUserDto userDto){
        authenticationService.verifyUser(userDto);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Your account has been successfully created.")
                .build();
    }
    @PatchMapping("/change-password")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        authenticationService.changePassword(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Change password success")
                .build();
    }
    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestParam String email) throws MessagingException {
        authenticationService.forgotPassword(email);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Check your email")
                .build();
    }
}
