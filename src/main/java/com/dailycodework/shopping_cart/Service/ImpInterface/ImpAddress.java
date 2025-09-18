package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Dto.AddressDto;
import com.dailycodework.shopping_cart.DTO.Request.AddressRequest;
import com.dailycodework.shopping_cart.Entity.Address;
import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Mapper.AddressMapper;
import com.dailycodework.shopping_cart.Repository.AddressRepository;
import com.dailycodework.shopping_cart.Repository.UserRepository;
import com.dailycodework.shopping_cart.Service.Interface.IAddress;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpAddress implements IAddress {
    AddressRepository addressRepository;
    UserRepository userRepository;
    AddressMapper addressMapper;
    @Override
    public List<AddressDto> getAllAddresses() {
        List<AddressDto> addressDtoList = addressRepository.findAll().stream().map(addressMapper::toAddressDto).toList();
        if(addressDtoList.isEmpty()) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        return addressDtoList;
    }

    @Override
    public List<AddressDto> getAddressesByUserId(Long userId) {
        List<AddressDto> addressDtoList = addressRepository.findByUserId(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND))
                .stream()
                .map(addressMapper::toAddressDto)
                .toList();
        if(addressDtoList.isEmpty()) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        return addressDtoList;
    }

    @Override
    public AddressDto getAddressById(Long id) {
        return addressMapper.toAddressDto(addressRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.ADDRESS_NOT_FOUND)));

    }

    @Override
    public AddressDto createAddress(AddressRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Address address = addressMapper.toAddress(request);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        user.getAddress().add(savedAddress);
        userRepository.save(user);

        return addressMapper.toAddressDto(savedAddress);
    }
    @Transactional // Đảm bảo chạy trong transaction
    @Override
    public AddressDto updateAddress(Long id, AddressRequest request) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        addressMapper.updateAddress(address, request);
        return addressMapper.toAddressDto(address);
    }
    @Transactional
    @Override
    public void deleteAddress(Long userId, Long id) {
        if (!addressRepository.existsById(id)) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Address addressToRemove = user.getAddress().stream()
                .filter(address -> address.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        user.getAddress().remove(addressToRemove);
        // Không cần gọi addressRepository.delete(...) hoặc userRepository.save(...)
        // Vì thay đổi sẽ được flush khi kết thúc transaction
    }
}
