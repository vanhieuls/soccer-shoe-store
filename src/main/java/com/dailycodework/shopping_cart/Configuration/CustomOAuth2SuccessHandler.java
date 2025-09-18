package com.dailycodework.shopping_cart.Configuration;

import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
//Nếu sử dụng passwordencoder thì sẽ tạo ra sự phụ thuộc
//SecurityConfig --> CustomOAuth2SuccessHandler --> PasswordEncoder --> SecurityConfig
//Cách tốt nhất là tách thằng passwordencoder ra làm class riêng rồi gọi mà lười quá nên note vậy thui nhé
//tạm thời mk khi đăng nhập bằng google sẽ chưa được mã hóa.
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    JwtTokenProvider jwtTokenProvider;
    UserRepository userRepository;
//    PasswordEncoder passwordEncoder;
    UserDetailsService userDetailsService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email"); // hoặc "sub" hay "name"

        // In log để xem Google trả về gì
        System.out.println("OAuth2 attributes: " + oAuth2User.getAttributes());

        // Tìm user trong DB theo email (hoặc tạo mới nếu chưa có)
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user;
        if (userOpt.isEmpty()) {
            user = new User();
            user.setEmail(email);
            user.setUsername(oAuth2User.getAttribute("given_name")); // hoặc dùng oAuth2User.getAttribute("name")
            user.setChecked(true);
//            user.setUsername(email.split("@")[0]); // hoặc dùng oAuth2User.getAttribute("name")
            user.setPassword("123456"); // có thể để rỗng nếu dùng login Google
//            user.setRole(Role.USER); // hoặc ROLE_GOOGLE
            userRepository.save(user);
        } else {
            user = userOpt.get();
        }
        System.out.println("ccccccccccccccccccccccccccccccccccccccccccc");
        // Chuyển User sang UserDetails (hoặc dùng User trực tiếp nếu bạn implements UserDetails)
        UserDetails userDetails = user;

        String token = jwtTokenProvider.generateToken(userDetails);
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
    }


}