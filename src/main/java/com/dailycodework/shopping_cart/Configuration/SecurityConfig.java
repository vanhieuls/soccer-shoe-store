package com.dailycodework.shopping_cart.Configuration;

import com.dailycodework.shopping_cart.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//Định nghĩa các đối tượng (beans) mà Spring sẽ quản lý trong IoC Container.
// Thiết lập các thành phần như Security, Database, REST API, v.v.
//Thay vì dùng applicationContext.xml, bạn có thể cấu hình bằng Java code.
@Configuration
//Bật tính năng bảo mật HTTP cho ứng dụng
@EnableWebSecurity
//	Cho phép sử dụng @PreAuthorize, @PostAuthorize
@EnableMethodSecurity
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SecurityConfig {
    String [] PUBLIC_ENDPOINT ={"/oauth2/authorization/google","/auth/**","/auth/signup","/auth/login","/auth/logout","/auth/refresh","/products/**","/user/create","/login","/login/**","/product-size/**","/size/**","/cartItem/**","/cartItem/remove-cartItem/**",
            "/category/getall","/user/getUser","/products/get-products/**","/products/get-products/filter/**",
            "/collection/get-collection-by-id/**","/collection/get-all","/collection/get-all-products-by-collection-id/**","/payment/confirm-webhook","/payment/payos_transfer_handler",
            "/review/getList/**","/payment/create-payment-link","/payment/**","/payments/**","payment/paypal/execute","payment/paypal/create"};
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    JwtAuthenticationFilter jwtAuthenticationFilter;
    UserDetailsService userDetailsService;
    CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        return new ThreadPoolTaskScheduler();
    }
    @Bean
    static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("STAFF")
                .role("STAFF").implies("USER")
                .build();
    }

    // and, if using pre-post method security also add
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
//    @Bean
//    public CustomOAuth2SuccessHandler customOAuth2SuccessHandler(JwtTokenProvider jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return new CustomOAuth2SuccessHandler(jwtService,userRepository,passwordEncoder);
//    }
    @Bean
//    Xác thực người dùng dựa trên UserDetailsService
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(o->o.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINT).permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINT).permitAll()
                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()
                .requestMatchers("/swagger-ui/**",
                        "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, PUBLIC_ENDPOINT).permitAll()
//                .requestMatchers("/api/auth/**", "/api/login").permitAll()
                .requestMatchers(HttpMethod.PUT, "/auth/resend-email").permitAll()
                .anyRequest().authenticated());
//        httpSecurity.oauth2ResourceServer(oauth2->oauth2
//                .jwt(Customizer.withDefaults())
//                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        httpSecurity.oauth2Client(Customizer.withDefaults());
//        httpSecurity.oauth2Login(Customizer.withDefaults());
        httpSecurity.oauth2Login(o -> o.successHandler(customOAuth2SuccessHandler));
//        httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.cors(Customizer.withDefaults()); //Quan trong (Bật cors)
        httpSecurity.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        return httpSecurity.build();
    }
}
