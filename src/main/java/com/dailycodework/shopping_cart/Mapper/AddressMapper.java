package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.AddressDto;
import com.dailycodework.shopping_cart.DTO.Request.AddressRequest;
import com.dailycodework.shopping_cart.Entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequest request);
    @Mapping(target = "userId", source = "user.id")
    AddressDto toAddressDto(Address address);
    List<Address> toAddressList(List<AddressRequest> requests);
    List<AddressDto> toAddressDtoList(List<Address> addresses);
    void updateAddress(@MappingTarget Address address, AddressRequest request);
}
