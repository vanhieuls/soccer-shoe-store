package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.AddressDto;
import com.dailycodework.shopping_cart.DTO.Request.AddressRequest;

import java.util.List;

public interface IAddress {
    List<AddressDto> getAllAddresses();
    List<AddressDto> getAddressesByUserId(Long userId);
    AddressDto getAddressById(Long id);
    AddressDto createAddress(AddressRequest request);
    AddressDto updateAddress(Long id, AddressRequest request);
    void deleteAddress(Long userId, Long id);
}
