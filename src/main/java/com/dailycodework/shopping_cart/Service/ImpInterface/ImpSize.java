package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Dto.SizeDto;
import com.dailycodework.shopping_cart.DTO.Request.SizeRequest;
import com.dailycodework.shopping_cart.Entity.Size;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Mapper.SizeMapper;
import com.dailycodework.shopping_cart.Repository.SizeRepository;
import com.dailycodework.shopping_cart.Service.Interface.ISize;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ImpSize implements ISize {
    SizeMapper sizeMapper;
    SizeRepository sizeRepository;
    @Override
    public List<SizeDto> getAllSizes() {
        return sizeMapper.toListSizeDto(sizeRepository.findAll());

    }

    @Override
    public SizeDto getSizeById(Long id) {
        return sizeMapper.toSizeDto(sizeRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.SIZE_NOT_EXIST)));
    }

    @Override
    public SizeDto createSize(SizeRequest request) {
        if(request.getSize() <= 0 || request.getSize() > 100) {
            throw new AppException(ErrorCode.SIZE_INVALID);
        }
        if(sizeRepository.existsBySize(request.getSize())) {
            throw new AppException(ErrorCode.SIZE_EXISTED);
        }
        Size size = sizeMapper.toSize(request);
        sizeRepository.save(size);
        return sizeMapper.toSizeDto(size);
    }

    @Override
    public void deleteSize(Long id) {
        if(!sizeRepository.existsById(id)) {
            throw new AppException(ErrorCode.SIZE_NOT_EXIST);
        }
        sizeRepository.deleteById(id);
    }

    @Override
    public SizeDto updateSize(Long id, SizeRequest request) {
        Size size = sizeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXIST));
        if(sizeRepository.existsBySize(request.getSize())) {
            throw new AppException(ErrorCode.SIZE_EXISTED);
        }
        sizeMapper.updateSize(size, request);
        return sizeMapper.toSizeDto(sizeRepository.save(size));
    }
}
