package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.AuthenticationDto;
import com.dailycodework.shopping_cart.DTO.Dto.ResetPassword;
import com.dailycodework.shopping_cart.DTO.Dto.VerifyUserDto;
import com.dailycodework.shopping_cart.DTO.Request.ChangePasswordRequest;
import com.dailycodework.shopping_cart.DTO.Request.LoginRequest;
import com.dailycodework.shopping_cart.DTO.Request.LogoutRequest;
import com.dailycodework.shopping_cart.DTO.Request.UserRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Service.Interface.IAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    @Operation(
            summary = "Đăng nhập"
    )
    @PostMapping("/login")
    public ApiResponse<AuthenticationDto> login(@RequestBody LoginRequest request){
//        try{
            AuthenticationDto authenticationDto = authenticationService.login(request);
            return ApiResponse.<AuthenticationDto>builder()
                    .code(200)
                    .message("login success")
                    .result(authenticationDto)
                    .build();
//        }
//        catch (Exception e){
//        return ApiResponse.<AuthenticationDto>builder()
//                .code(401)
//                .message("Invalids credentials")
//                .build();
//        }
    }
    @Operation(
            summary = "Đăng xuất"
    )
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String accessToken){
        log.info("AccessToken: {}", accessToken);
        String token = accessToken.replace("Bearer ","");
        authenticationService.logout(token);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Logout success")
                .build();
    }
    @Operation(
            summary = "Đăng ký"
    )
    @PostMapping("/signup")
    public ApiResponse<UserResponse> signUp(@Valid @RequestBody UserRequest request){
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Check Email and verification code")
                .result(authenticationService.signUp(request))
                .build();
    }
    @Operation(
            summary = "Gửi lại email xác thực"
    )
    @PutMapping("/resend-email")
    public ApiResponse<Void> reSendVerificationEmail(@RequestParam String email){
        authenticationService.reSendVerificationEmail(email);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Verify code was resend")
                .build();
    }
    @Operation(
            summary = "Xác thực người dùng"
    )
    @PostMapping("/verify-code")
    public ApiResponse<Void> verifyUser(@RequestBody VerifyUserDto userDto){
        authenticationService.verifyUser(userDto);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Your account has been successfully created.")
                .build();
    }
    @Operation(
            summary = "Đổi mật khẩu"
    )
    @PatchMapping("/change-password")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        authenticationService.changePassword(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Change password success")
                .build();
    }
    @Operation(
            summary = "Quên mật khẩu"
    )
    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestParam String email) throws MessagingException {
        authenticationService.forgotPassword(email);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Check your email")
                .build();
    }
    @Operation(
            summary = "Làm mới token"
    )
    @PostMapping("/refresh")
    public ApiResponse<AuthenticationDto> refreshToken(HttpServletRequest request){
        AuthenticationDto authenticationDto = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationDto>builder()
                .code(200)
                .message("Refresh token success")
                .result(authenticationDto)
                .build();
    }
    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPassword resetPasswordRequest){
        authenticationService.resetPassword(resetPasswordRequest);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Reset password success")
                .build();
    }
}
