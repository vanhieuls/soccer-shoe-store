package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.AuthenticationDto;
import com.dailycodework.shopping_cart.DTO.Dto.ResetPassword;
import com.dailycodework.shopping_cart.DTO.Dto.VerifyUserDto;
import com.dailycodework.shopping_cart.DTO.Request.ChangePasswordRequest;
import com.dailycodework.shopping_cart.DTO.Request.LoginRequest;
import com.dailycodework.shopping_cart.DTO.Request.LogoutRequest;
import com.dailycodework.shopping_cart.DTO.Request.UserRequest;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Entity.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthentication {
    AuthenticationDto login (LoginRequest request);
    void logout (String request);
    UserResponse signUp(UserRequest request);
    void verifyUser(VerifyUserDto userDto);
    void reSendVerificationEmail(String email);
    void changePassword(ChangePasswordRequest request);
    void forgotPassword(String email) throws MessagingException;
    void resetPassword (ResetPassword resetPassword);
    AuthenticationDto refreshToken(HttpServletRequest request);
}
