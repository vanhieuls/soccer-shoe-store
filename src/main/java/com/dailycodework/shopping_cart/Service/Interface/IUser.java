package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Request.UserRequest;
import com.dailycodework.shopping_cart.DTO.Request.UserUpdateRequest;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Enum.Roles;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface IUser {
    UserResponse getUser (Long userId);
    UserResponse createUser (UserRequest request);
    UserResponse updateUser(UserUpdateRequest request, Long userId);
    void deleteUser (Long userId);
    UserResponse updateAvatar(MultipartFile file, Long userId);
    Page<UserResponse> getAllUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    Page<UserResponse> searchUsers(Integer pageNumber, Integer pageSize, String keyword);
    long countNewUsers(String startDate, String endDate);
    void lockUser(Long userId);
    long TotalUsersIsActive();
    UserResponse createAdmin(UserRequest request);
    UserResponse createStaff(UserRequest request);
    Page<UserResponse> filterManagers(Integer pageNumber, Integer pageSize, Roles roleNames, String name, String email);
    Page<UserResponse> getAllUserManagers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
