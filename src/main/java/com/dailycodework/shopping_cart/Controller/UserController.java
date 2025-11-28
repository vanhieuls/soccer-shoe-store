package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Dto.AddressDto;
import com.dailycodework.shopping_cart.DTO.Dto.VoucherDto;
import com.dailycodework.shopping_cart.DTO.Request.UserRequest;
import com.dailycodework.shopping_cart.DTO.Request.UserUpdateRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Entity.Cart;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Enum.Roles;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Repository.UserRepository;
import com.dailycodework.shopping_cart.Service.Interface.IUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "User")
public class UserController {
    UserRepository userRepository;
    IUser userService;
    @Operation(summary = "Lấy tất cả người dùng với phân trang và sắp xếp")
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/getAll")
    public ApiResponse<Page<UserResponse>> getAllUser(@RequestParam(defaultValue = "0") Integer pageNumber,
                                         @RequestParam(defaultValue = "10") Integer pageSize,@RequestParam(defaultValue = "username") String sortBy,@RequestParam(defaultValue = "asc") String sortDir) {
        return ApiResponse.<Page<UserResponse>>builder()
                .code(200)
                .message("get all success")
                .result(userService.getAllUser(pageNumber,pageSize,sortBy,sortDir))
                .build();
    }
    @Operation(summary = "Lấy người dùng theo id")
    @GetMapping("/getUser/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long userId) {
        return ApiResponse.<UserResponse>builder()
                    .code(200)
                    .message("get success")
                    .result(userService.getUser(userId))
                    .build();
    }
    @Operation(summary = "Lấy người dùng hiện tại")
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
    @Operation(summary = "Tạo người dùng mới")
    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("create success")
                .result(userService.createUser(request))
                .build();
    }
    @Operation(summary = "Cập nhật người dùng")
    @PutMapping("/update/{userId}")
    public ApiResponse<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request, @PathVariable Long userId) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Update success")
                .result(userService.updateUser(request,userId))
                .build();
    }
    @PreAuthorize("hasRole('STAFF')")
    @Operation(summary = "Xóa người dùng theo id")
    @DeleteMapping("/delete/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("delete success")
                .build();
    }
    @Operation(summary = "Cập nhật avatar người dùng")
    @PutMapping("/updateAvatar/{userId}")
    public ApiResponse<UserResponse> updateAvatar(@RequestParam("file") MultipartFile file, @PathVariable Long userId) {
        UserResponse userResponse = userService.updateAvatar(file, userId);
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Update avatar success")
                .result(userResponse)
                .build();
    }
    @PreAuthorize("hasRole('STAFF')")
    @Operation(summary = "Tìm kiếm người dùng theo từ khóa với phân trang")
    @GetMapping("/search")
    public ApiResponse<Page<UserResponse>> searchUsers(@RequestParam(defaultValue = "0") Integer pageNumber,@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam String keyword) {
        return ApiResponse.<Page<UserResponse>>builder()
                .code(200)
                .message("search success")
                .result(userService.searchUsers(pageNumber,pageSize,keyword))
                .build();
    }
    @Operation(summary = "Đếm số người dùng mới trong khoảng thời gian")
    @GetMapping("/count-new-users")
    public ApiResponse<Long> countNewUsers(@RequestParam String startDate, @RequestParam String endDate) {
        long count = userService.countNewUsers(startDate, endDate);
        return ApiResponse.<Long>builder()
                .code(200)
                .message("Count new users success")
                .result(count)
                .build();
    }
    @Operation(summary = "Khóa hoặc mở khóa người dùng (dành cho admin)")
    @PutMapping("/lock/{userId}")
    public ApiResponse<Void> lockUser(@PathVariable Long userId) {
        userService.lockUser(userId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Lock user success")
                .build();
    }
    @Operation(summary = "Đếm tổng số người dùng đang hoạt động")
    @GetMapping("/total-active-users")
    public ApiResponse<Long> totalUsersIsActive() {
        long totalActiveUsers = userService.TotalUsersIsActive();
        return ApiResponse.<Long>builder()
                .code(200)
                .message("Total active users success")
                .result(totalActiveUsers)
                .build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tạo tài khoản Admin / chỉ có role admin mới có quyền tạo")
    @PostMapping("/create-admin")
    public ApiResponse<UserResponse> createAdmin(@RequestBody @Valid UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("create admin success")
                .result(userService.createAdmin(request))
                .build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tạo tài khoản Staff / chỉ có role admin mới có quyền tạo")
    @PostMapping("/create-staff")
    public ApiResponse<UserResponse> createStaff(@RequestBody @Valid UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("create staff success")
                .result(userService.createStaff(request))
                .build();
    }
    @PreAuthorize("hasRole('STAFF')")
    @Operation(summary = "Lấy tất cả người dùng quản lý với phân trang và sắp xếp")
    @GetMapping("/get-managers")
    public ApiResponse<Page<UserResponse>> getAllUserManagers(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,@RequestParam(defaultValue = "username") String sortBy,@RequestParam(defaultValue = "asc") String sortDir) {
        return ApiResponse.<Page<UserResponse>>builder()
                .code(200)
                .message("get all managers success")
                .result(userService.getAllUserManagers(pageNumber,pageSize,sortBy,sortDir))
                .build();
    }
    @PreAuthorize("hasRole('STAFF')")
    @Operation(summary = "Lọc người dùng quản lý theo vai trò, tên và email với phân trang")
    @GetMapping("/filter-managers")
    public ApiResponse<Page<UserResponse>> filterManagers(@RequestParam(defaultValue = "0") Integer pageNumber,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam(required = false) String roleNames,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String email) {
        if(roleNames == null){
            return ApiResponse.<Page<UserResponse>>builder()
                    .code(200)
                    .message("filter managers success")
                    .result(userService.getAllUserManagers(pageNumber, pageSize, "username", "asc"))
                    .build();
        }
        else {
            return ApiResponse.<Page<UserResponse>>builder()
                    .code(200)
                    .message("filter managers success")
                    .result(userService.filterManagers(pageNumber, pageSize, roleNames != null ? Roles.valueOf(roleNames) : null, name, email))
                    .build();
        }
    }

}
