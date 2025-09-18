package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.VoucherDto;
import com.dailycodework.shopping_cart.DTO.Request.VoucherRequest;
import com.dailycodework.shopping_cart.Entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    Voucher toVoucher (VoucherRequest request);
    VoucherDto toVoucherDto (Voucher voucher);
    List<VoucherDto> toListVoucher (List<Voucher> vouchers);
    void updateVoucher(@MappingTarget Voucher voucher, VoucherRequest request);
}
