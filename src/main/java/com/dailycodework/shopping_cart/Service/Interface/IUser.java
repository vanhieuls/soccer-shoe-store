package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.VoucherDto;
import com.dailycodework.shopping_cart.DTO.Request.UserRequest;
import com.dailycodework.shopping_cart.DTO.Request.UserUpdateRequest;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUser {
    UserResponse getUser (Long userId);
    UserResponse createUser (UserRequest request);
    UserResponse updateUser(UserUpdateRequest request, Long userId);
    void deleteUser (Long userId);
    UserResponse updateAvatar(MultipartFile file, Long userId);
    Page<UserResponse> getAllUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    Page<UserResponse> searchUsers(Integer pageNumber, Integer pageSize, String keyword);
}
