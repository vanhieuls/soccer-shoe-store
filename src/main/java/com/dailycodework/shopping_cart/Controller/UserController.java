package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.AddressDto;
import com.dailycodework.shopping_cart.DTO.Dto.VoucherDto;
import com.dailycodework.shopping_cart.DTO.Request.UserRequest;
import com.dailycodework.shopping_cart.DTO.Request.UserUpdateRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Repository.UserRepository;
import com.dailycodework.shopping_cart.Service.Interface.IUser;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserRepository userRepository;
    IUser userService;
    @GetMapping("/getAll")
    public ApiResponse<Page<UserResponse>> getAllUser(@RequestParam(defaultValue = "0") Integer pageNumber,
                                         @RequestParam(defaultValue = "10") Integer pageSize,@RequestParam(defaultValue = "username") String sortBy,@RequestParam(defaultValue = "asc") String sortDir) {
        return ApiResponse.<Page<UserResponse>>builder()
                .code(200)
                .message("get all success")
                .result(userService.getAllUser(pageNumber,pageSize,sortBy,sortDir))
                .build();
    }
    @GetMapping("/getUser/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long userId) {
        return ApiResponse.<UserResponse>builder()
                    .code(200)
                    .message("get success")
                    .result(userService.getUser(userId))
                    .build();
    }
    @GetMapping("/getUser")
    public ApiResponse<User> getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));

        return ApiResponse.<User>builder()
                .code(200)
                .message("get success")
                .result(user)
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("create success")
                .result(userService.createUser(request))
                .build();
    }

    @PutMapping("/update/{userId}")
    public ApiResponse<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request, @PathVariable Long userId) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Update success")
                .result(userService.updateUser(request,userId))
                .build();
    }

    @DeleteMapping("/delete/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("delete success")
                .build();
    }
    @PutMapping("/updateAvatar/{userId}")
    public ApiResponse<UserResponse> updateAvatar(@RequestParam("file") MultipartFile file, @PathVariable Long userId) {
        UserResponse userResponse = userService.updateAvatar(file, userId);
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Update avatar success")
                .result(userResponse)
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<Page<UserResponse>> searchUsers(@RequestParam(defaultValue = "0") Integer pageNumber,@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam String keyword) {
        return ApiResponse.<Page<UserResponse>>builder()
                .code(200)
                .message("search success")
                .result(userService.searchUsers(pageNumber,pageSize,keyword))
                .build();
    }
}
