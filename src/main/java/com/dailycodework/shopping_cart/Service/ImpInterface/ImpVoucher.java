package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Dto.VoucherDto;
import com.dailycodework.shopping_cart.DTO.Request.VoucherRequest;
import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Entity.Voucher;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Helper.ProductSpecification.ProductSpecification;
import com.dailycodework.shopping_cart.Mapper.UserMapper;
import com.dailycodework.shopping_cart.Mapper.VoucherMapper;
import com.dailycodework.shopping_cart.Repository.UserRepository;
import com.dailycodework.shopping_cart.Repository.VoucherRepository;
import com.dailycodework.shopping_cart.Service.Interface.IVoucher;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ImpVoucher implements IVoucher {
    ImpOrder orderService;
    VoucherMapper voucherMapper;
    VoucherRepository voucherRepository;
    UserRepository userRepository;
    UserMapper userMapper;
    @Override
    public VoucherDto createVoucher(VoucherRequest request) {
        Voucher voucher = voucherMapper.toVoucher(request);
        voucherRepository.save(voucher);
        return voucherMapper.toVoucherDto(voucher);
    }

    @Override
    public VoucherDto getVoucherById(Long id) {
        return voucherMapper.toVoucherDto(voucherRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.VOUCHER_NOT_EXIST)));
    }

    @Override
    public void deleteVoucher(Long id) {
        voucherRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.VOUCHER_NOT_EXIST));
        voucherRepository.deleteById(id);
    }

    @Override
    public Page<VoucherDto> getAllVoucher(Integer pageNumber, Integer pageSize, String sortDir) {
        Pageable pageable = null;
        if(pageNumber == null || pageNumber < 0) pageNumber = 0;
        if(pageSize == null || pageSize <= 0 ) pageSize = 10;
        String sortField = "id";
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField ));
        Page<Voucher> voucherPage = voucherRepository.findAll(pageable);
        return voucherPage.map(voucherMapper::toVoucherDto);
    }

    @Override
    public VoucherDto updateVoucher(Long id, VoucherRequest request) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.VOUCHER_NOT_EXIST));
        voucherMapper.updateVoucher(voucher,request);
        return voucherMapper.toVoucherDto(voucherRepository.save(voucher));
    }
    @Override
    public void addVoucherToUser(Long userId, Long voucherId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXIST));
        if(user.getVouchers().contains(voucher)){
            throw new AppException(ErrorCode.VOUCHER_ALREADY_EXISTED_IN_USER);
        }
        if(user.getPointVoucher() < voucher.getPointRequired()) {
            throw new AppException(ErrorCode.INSUFFICIENT_POINTS);
        }
        if(!voucher.isActive()){
            throw new AppException(ErrorCode.VOUCHER_NOT_ACTIVE);
        }
        else if (voucher.getUsageLimit()<=voucher.getUsedCount()){
            throw new AppException(ErrorCode.VOUCHER_USAGE_LIMIT_EXCEEDED);
        }
        else if(voucher.getStartDate().isAfter(LocalDateTime.now())||voucher.getEndDate().isBefore(LocalDateTime.now())){
            throw new AppException(ErrorCode.EXPIRED_VOUCHER);
        }
        if (user.getVouchers().stream().anyMatch(v -> v.getId().equals(voucherId))) {
            throw new AppException(ErrorCode.VOUCHER_ALREADY_USED);
        }
        user.getVouchers().add(voucher);
        voucher.getUsers().add(user);
        voucher.setUsedCount(voucher.getUsedCount() + 1);
        user.setPointVoucher(user.getPointVoucher() - voucher.getPointRequired());
//        voucherRepository.save(voucher);
        userRepository.save(user);
    }

    @Override
    public List<VoucherDto> getVouchersByUserId(Long userId) {
        List<VoucherDto> voucherDtoList = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND))
                .getVouchers()
                .stream()
                .map(voucherMapper::toVoucherDto)
                .toList();
        if(voucherDtoList.isEmpty()) {
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        }
        return voucherDtoList;
    }

    @Override
    public VoucherDto findByCode(String code) {
        return voucherRepository.findByCode(code)
                .map(voucherMapper::toVoucherDto)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_EXIST));
    }


}
