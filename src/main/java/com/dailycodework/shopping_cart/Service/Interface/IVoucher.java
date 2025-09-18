package com.dailycodework.shopping_cart.Service.Interface;
import com.dailycodework.shopping_cart.DTO.Dto.VoucherDto;
import com.dailycodework.shopping_cart.DTO.Request.VoucherRequest;
import com.dailycodework.shopping_cart.DTO.Response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;
public interface IVoucher {
    VoucherDto createVoucher (VoucherRequest request);
    VoucherDto getVoucherById (Long id);
    void deleteVoucher(Long id);
    Page<VoucherDto> getAllVoucher(Integer pageNumber, Integer pageSize, String sortDir);
    VoucherDto updateVoucher (Long id, VoucherRequest request);
    void addVoucherToUser(Long userId, Long voucherId);
    List<VoucherDto> getVouchersByUserId(Long userId);
    VoucherDto findByCode(String code);
}
