package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.Configuration.JwtTokenProvider;
import com.dailycodework.shopping_cart.DTO.Dto.AuthenticationDto;
import com.dailycodework.shopping_cart.DTO.Dto.ResetPassword;
import com.dailycodework.shopping_cart.DTO.Dto.VerifyUserDto;
import com.dailycodework.shopping_cart.DTO.Request.ChangePasswordRequest;
import com.dailycodework.shopping_cart.DTO.Request.LoginRequest;
import com.dailycodework.shopping_cart.DTO.Request.LogoutRequest;
import com.dailycodework.shopping_cart.DTO.Request.UserRequest;
import com.dailycodework.shopping_cart.DTO.Response.JwtInfo;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.RedisToken;
import com.dailycodework.shopping_cart.Entity.Role;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Enum.Roles;
import com.dailycodework.shopping_cart.Enum.TypeToken;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Mapper.UserMapper;
import com.dailycodework.shopping_cart.Repository.CartRepository;
import com.dailycodework.shopping_cart.Repository.RedisRepository;
import com.dailycodework.shopping_cart.Repository.RoleRepository;
import com.dailycodework.shopping_cart.Repository.UserRepository;
import com.dailycodework.shopping_cart.Service.Interface.IAuthentication;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.dailycodework.shopping_cart.Enum.TypeToken.ACCESS_TOKEN;
import static com.dailycodework.shopping_cart.Enum.TypeToken.REFRESH_TOKEN;

@Slf4j
@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpAuthentication implements IAuthentication {
    UserMapper userMapper;
    RoleRepository roleRepository;
    ImpEmail emailService;
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtTokenProvider;
    PasswordEncoder passwordEncoder;
    CartRepository cartRepository;
    ImpCart cartService;
    RedisRepository redisRepository;
    @Override
    public AuthenticationDto login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        if(!user.isChecked()){
            throw new AppException(ErrorCode.ACCOUNT_NOT_VERIFY);
        }
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//            if (authentication.isAuthenticated()) {
                log.info("Login success, generate tokennnnnnnnnnnnn");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                user.setActive(true);
                userRepository.save(user);
                String accessToken = jwtTokenProvider.generateToken(userDetails);
                String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
                log.info("Login success, generate token");
                return AuthenticationDto.builder()
                        .authentication(true)
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .build();
//            } else {
//                throw new UsernameNotFoundException("Invalids credentials");
//            }
        }
        catch (LockedException e) {
            // tài khoản bị khóa
            throw new AppException(ErrorCode.ACCOUNT_LOCKED);
        }
        catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    @Override
        public void logout(String token) {
            if(!jwtTokenProvider.validateToken(token,ACCESS_TOKEN)){
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }
            String username = jwtTokenProvider.extractUsername(token, ACCESS_TOKEN);
            User user = userRepository.findByUsername(username).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
            user.setActive(false);
            userRepository.save(user);
            JwtInfo jwtInfo = jwtTokenProvider.parseToken(token, ACCESS_TOKEN);
            long ttlMs = jwtInfo.getExpiredTime().getTime() - System.currentTimeMillis();
            if(ttlMs <=0){
                throw new RuntimeException("Token already expired");
            }
            RedisToken redisToken = RedisToken.builder()
                    .jwtId(jwtInfo.getJwtId())
    //                TTL trong redis nên bằng thời gian hết hạn trừ cho thời gian hiện tại
                    .expiredTime(ttlMs)
                    .build();
            redisRepository.save(redisToken);
            log.info("Logout success, save token to redis");
        }

    @Override
    public UserResponse signUp(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Set<Role> roles = new HashSet<>();
        Role entity =
                Role.builder().name(Roles.valueOf(Roles.ROLE_USER.name())).build();
        roles.add(entity);
        User user = userMapper.toUser(request);
        user.setVerificationCode(generateVerifyCode());
        user.setVerificationExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setChecked(false);
        user.setNonLocked(true);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Cart cart = Cart.builder()
                .user(user)
                .build();
        // Lưu user trước, rồi tới lưu cart, không là lỗi TransientObjectException
        user.setCart(cart);
        User user1 = userRepository.save(user);
        cartRepository.save(cart);
        sendVerificationEmail(user);
        return userMapper.toUserResponse(user1);
    }

    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                "        .header { color: #4a90e2; text-align: center; }\n" +
                "        .code { font-size: 24px; font-weight: bold; text-align: center; margin: 20px 0; padding: 10px; background: #f5f5f5; border-radius: 5px; }\n" +
                "        .button { display: inline-block; padding: 10px 20px; background-color: #4a90e2; color: white; text-decoration: none; border-radius: 5px; margin-top: 20px; }\n" +
                "        .footer { margin-top: 30px; font-size: 12px; color: #999; text-align: center; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <h1>Verify Your Account</h1>\n" +
                "    </div>\n" +
                "    <p>Hello " + user.getUsername() + ",</p>\n" +
                "    <p>Thank you for registering with us. Please use the following verification code to complete your registration:</p>\n" +
                "    \n" +
                "    <div class=\"code\">" + verificationCode + "</div>\n" +
                "    \n" +
                "    <p>If you didn't request this, please ignore this email.</p>\n" +
                "    \n" +
                "    <div class=\"footer\">\n" +
                "        <p>© 2023 Your Company. All rights reserved.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        try{
            emailService.sendverificationEmail(user.getEmail(), subject, htmlMessage);
        }
        catch (MessagingException e){
            e.printStackTrace();
            //in Dòng lỗi (error message).
            //Stack trace (toàn bộ luồng code dẫn đến lỗi).
            // Giúp bạn nhanh chóng phát hiện nguyên nhân lỗi khi phát triển ứng dụng.
        }
    }
    @Override
    public void reSendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        if(user.isChecked()){
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_VERIFIED);
        }
        user.setVerificationCode(generateVerifyCode());
        user.setVerificationExpiresAt(LocalDateTime.now().plusMinutes(15));
        sendVerificationEmail(user);
        userRepository.save(user);
    }
    private String generateVerifyCode() {
        Random random = new Random();
        int code = random.nextInt(900000)+100000;
        return String.valueOf(code);
    }

    @Override
    public void verifyUser(VerifyUserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        if(!user.getVerificationCode().equals(userDto.getVerificationCode())){
            throw new AppException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
        if(user.getVerificationExpiresAt().isBefore(LocalDateTime.now())){
            throw new AppException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        user.setChecked(true);
        user.setVerificationExpiresAt(null);
        user.setVerificationCode(null);
        userRepository.save(user);
    }
    @Override
    public void changePassword(ChangePasswordRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
//        passwordEncoder.matches(rawPassword, encodedPassword);
//        rawPassword: mật khẩu dạng thô, chưa mã hóa (ví dụ: "123456")
//encodedPassword: mật khẩu đã được mã hóa bằng passwordEncoder.encode(...) và lưu trong DB (ví dụ: "$2a$10$J1i7...k")
        if(!passwordEncoder.matches(request.oldPassword(),user.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
        if(!request.newPassword().equals(request.confirmPassword())){
            throw new AppException(ErrorCode.INVALID_CONFIRM_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(String email) throws MessagingException {
        User user = userRepository.findByEmail(email).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        String resetToken = generateToken();
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
        // Gửi email với link đặt lại mật khẩu
        String resetLink = "http://localhost:5173/reset-password/?token=" + resetToken;
        String htmlMessage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <style>\n" +
                "        body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                "        .header { color: #4F46E5; text-align: center; margin-bottom: 25px; }\n" +
                "        .logo { font-size: 24px; font-weight: bold; margin-bottom: 10px; }\n" +
                "        .content { background-color: #F9FAFB; padding: 25px; border-radius: 8px; }\n" +
                "        .button { display: inline-block; padding: 12px 24px; background-color: #4F46E5; color: white !important; text-decoration: none; border-radius: 6px; font-weight: bold; margin: 20px 0; }\n" +
                "        .footer { margin-top: 30px; font-size: 12px; color: #9CA3AF; text-align: center; }\n" +
                "        .code { font-family: monospace; background-color: #E5E7EB; padding: 8px 12px; border-radius: 4px; }\n" +
                "        .divider { height: 1px; background-color: #E5E7EB; margin: 20px 0; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <div class=\"logo\">YourApp</div>\n" +
                "        <h2>Reset Your Password</h2>\n" +
                "    </div>\n" +
                "    \n" +
                "    <div class=\"content\">\n" +
                "        <p>Hello " + user.getUsername() + ",</p>\n" +
                "        <p>We received a request to reset your password. Click the button below to proceed:</p>\n" +
                "        \n" +
                "        <div style=\"text-align: center;\">\n" +
                "            <a href=\"" + resetLink + "\" class=\"button\">Reset Password</a>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"divider\"></div>\n" +
                "        \n" +
                "        <p>If you didn't request this, you can safely ignore this email. This link will expire in <strong>15 minutes</strong>.</p>\n" +
                "        \n" +
                "        <p>Or copy this link manually:</p>\n" +
                "        <p class=\"code\">" + resetLink + "</p>\n" +
                "    </div>\n" +
                "    \n" +
                "    <div class=\"footer\">\n" +
                "        <p>© 2023 YourApp. All rights reserved.</p>\n" +
                "        <p>If you have any questions, contact us at support@yourapp.com</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        try{
            emailService.sendverificationEmail(email, "Reset Your Password", htmlMessage);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetPassword(ResetPassword resetPassword) {
        User user = userRepository.findByResetPasswordToken(resetPassword.token()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        if(user.getResetPasswordExpiry().isBefore(LocalDateTime.now())){
            throw new AppException(ErrorCode.TOKEN_RESET_PASSWORD_EXPIRED);
        }
        user.setPassword(passwordEncoder.encode(resetPassword.newPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);
        userRepository.save(user);
    }

    @Override
    public AuthenticationDto refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        log.info("RefreshToken: {}", refreshToken);
        if(refreshToken == null || refreshToken.isBlank()){
            throw new RuntimeException("Refresh token is missing");
        }
        if(!jwtTokenProvider.validateToken(refreshToken, REFRESH_TOKEN)){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        String username = jwtTokenProvider.extractUsername(refreshToken, REFRESH_TOKEN);
        UserDetails userDetails = userRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        String newAccessToken = jwtTokenProvider.generateToken(userDetails);
        log.info("New AccessToken: {}", newAccessToken);
        return AuthenticationDto.builder()
                .authentication(true)
                .token(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

}
