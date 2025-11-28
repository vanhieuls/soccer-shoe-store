package com.dailycodework.shopping_cart.Configuration;

import com.dailycodework.shopping_cart.Entity.Role;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Enum.Roles;
import com.dailycodework.shopping_cart.Repository.RoleRepository;
import com.dailycodework.shopping_cart.Repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@Slf4j
public class ApplicationConfig {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRespository;
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()) {
                Role entity = Role.builder()
                        .name(Roles.valueOf(Roles.ROLE_ADMIN.name()))
                        //                        Role.ROLE_ADMIN.name() → trả về "ROLE_ADMIN" (kiểu String)
                        //                        valueOf là phương thức tĩnh (static method) dùng để chuyển chuỗi
                        // (String) thành một hằng enum tương ứng.
                        .build();

                roleRespository.save(entity);
                Set<Role> roles = new HashSet<>();
                roles.add(entity);
//                var roles = new HashSet<String>();
//                roles.add(Roles.ROLE_ADMIN.name());
                User admin = User.builder()
                        .username("admin")
                        .phone("0123456789")
                        .dateOfBirth("01/01/1990")
                        .email("hieu@gmail.com")
                        .password(passwordEncoder.encode("admin1309**")) // In a real application, ensure to hash passwords!
                        .roles(roles)
                        .nonLocked(true)
                        .checked(true)
                        .build();
                userRepository.save(admin);
                log.info("Admin user created with username: admin and password: admin1309**");

            }
        };
    }
}
