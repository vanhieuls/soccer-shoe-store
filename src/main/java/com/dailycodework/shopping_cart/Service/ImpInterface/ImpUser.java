package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.cloudinary.Cloudinary;
import com.dailycodework.shopping_cart.Configuration.JwtTokenProvider;
import com.dailycodework.shopping_cart.DTO.Dto.VoucherDto;
import com.dailycodework.shopping_cart.DTO.Request.ImageDto;
import com.dailycodework.shopping_cart.DTO.Request.UserRequest;
import com.dailycodework.shopping_cart.DTO.Request.UserUpdateRequest;
import com.dailycodework.shopping_cart.DTO.Response.JwtInfo;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Entity.*;
import com.dailycodework.shopping_cart.Enum.Roles;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Helper.OrderSpecification.OrderSpecification;
import com.dailycodework.shopping_cart.Helper.UserSpecification.UserSpecification;
import com.dailycodework.shopping_cart.Mapper.UserMapper;
import com.dailycodework.shopping_cart.Mapper.VoucherMapper;
import com.dailycodework.shopping_cart.Repository.CartRepository;
import com.dailycodework.shopping_cart.Repository.RedisRepository;
import com.dailycodework.shopping_cart.Repository.UserRepository;
import com.dailycodework.shopping_cart.Repository.VoucherRepository;
import com.dailycodework.shopping_cart.Service.Interface.IUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.dailycodework.shopping_cart.Enum.TypeToken.ACCESS_TOKEN;
@Slf4j
@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpUser implements IUser {
    RedisRepository redisRepository;
    JwtTokenProvider jwtTokenProvider;
    UserRepository userRepository;
    UserMapper userMapper;
    CartRepository cartRepository;
    PasswordEncoder passwordEncoder;
    VoucherRepository voucherRepository;
    VoucherMapper voucherMapper;
    Cloudinary cloudinary;
    @Override
    public UserResponse getUser(Long userId) {
        return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setChecked(false);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Cart cart = Cart.builder()
                .user(user)
                .build();
    // Lưu user trước, rồi tới lưu cart, không là lỗi TransientObjectException
        user.setCart(cart);
        User user1 = userRepository.save(user);
//        cartRepository.save(cart);
        return userMapper.toUserResponse(user1);
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        // 1) Lấy tất cả voucher có liên kết tới user này
        Set<Voucher> vouchers = user.getVouchers();
        // 2) Gỡ liên kết trên OWNING SIDE (Voucher.users)
        for (Voucher v : vouchers) {
            v.getUsers().remove(user);
        }
        userRepository.delete(user);
    }

    @Override
    public UserResponse updateAvatar(MultipartFile file, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            user.setAvatar(data.get("secure_url").toString());
            userRepository.save(user);
            return userMapper.toUserResponse(user);
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Page<UserResponse> getAllUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Pageable pageable = null;
        if(pageNumber == null || pageNumber < 0){
            pageNumber = 0;
        }
        if(pageSize == null || pageSize <=0){
            pageSize = 10;
        }
        String sortField = (sortBy != null) ? sortBy : "username";
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));
        Page<User> users = userRepository.findAllWithCart(pageable);
        if(users.isEmpty()){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return users.map(userMapper::toUserResponse);
    }

    @Override
    public Page<UserResponse> searchUsers(Integer pageNumber, Integer pageSize, String keyword) {
        if(keyword == null || keyword.isEmpty()){
            getAllUser(pageNumber,pageSize,"username","asc");
        }
        if(pageNumber == null || pageNumber < 0){
            pageNumber = 0;
        }
        if(pageSize == null || pageSize <=0){
            pageSize = 10;
        }
        Pageable pageable = null;
        pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> users = userRepository.searchByUsernameOrEmail(keyword, pageable).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        return users.map(userMapper::toUserResponse);
    }

    @Override
    public long countNewUsers(String startDate, String endDate) {
        return userRepository.countNewUsersByCreatedAtBetween(startDate, endDate);
    }
    @Override
    public void lockUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setNonLocked(!user.isNonLocked());
        userRepository.save(user);
    }

    @Override
    public long TotalUsersIsActive() {
        return userRepository.countUsersIsActive();
    }

    @Override
    public UserResponse createAdmin(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Set<Role> roleSet = new HashSet<>();
        Role entity =
                Role.builder().name(Roles.valueOf(Roles.ROLE_ADMIN.name())).build();
        roleSet.add(entity);
        User user = userMapper.toUser(request);
        user.setChecked(true);
        user.setNonLocked(true);
        user.setRoles(roleSet);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse createStaff(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Set<Role> roleSet = new HashSet<>();
        Role entity =
                Role.builder().name(Roles.valueOf(Roles.ROLE_STAFF.name())).build();
        roleSet.add(entity);
        User user = userMapper.toUser(request);
        user.setChecked(true);
        user.setNonLocked(true);
        user.setRoles(roleSet);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public Page<UserResponse> filterManagers(Integer pageNumber, Integer pageSize, Roles roleNames, String name, String email) {
        Pageable pageable = null;
        if(pageNumber == null || pageNumber < 0){
            pageNumber = 0;
        }
        if(pageSize == null || pageSize <=0){
            pageSize = 10;
        }
        pageable = PageRequest.of(pageNumber, pageSize);
        Specification<User> specification = UserSpecification.filterManagers(roleNames, name, email);
        Page<User> users = userRepository.findAll(specification, pageable);
        if(users.isEmpty()){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return users.map(userMapper::toUserResponse);
    }

    //    @Override
//    public Page<UserResponse> getAllUserManagers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
//        Specification<User> specification = Specification.where(null);
//        specification = specification.and(UserSpecification.getUserManagers());
//        Pageable pageable = null;
//        if(pageNumber == null || pageNumber < 0){
//            pageNumber = 0;
//        }
//        if(pageSize == null || pageSize <=0){
//            pageSize = 10;
//        }
//        String sortField = (sortBy != null) ? sortBy : "username";
//        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//        pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));
//        Page<User> users = userRepository.findAll(specification, pageable);
//        if(users.isEmpty()){
//            throw new AppException(ErrorCode.USER_NOT_FOUND);
//        }
//        return users.map(userMapper::toUserResponse);
//    }
    @Override
    public Page<UserResponse> getAllUserManagers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        int page = (pageNumber == null || pageNumber < 0) ? 0 : pageNumber;
        int size = (pageSize == null || pageSize <= 0) ? 10 : pageSize;

        String sortField = (sortBy == null || sortBy.isBlank()) ? "username" : sortBy;
        Sort.Direction direction = ("desc".equalsIgnoreCase(sortDir)) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Specification<User> spec = Specification
                .where(UserSpecification.hasAnyRole(Set.of("ROLE_ADMIN", "ROLE_STAFF")));

        Page<User> users = userRepository.findAll(spec, pageable);

        // KHÔNG nên ném lỗi khi rỗng nếu là API list
        return users.map(userMapper::toUserResponse);
    }

}
