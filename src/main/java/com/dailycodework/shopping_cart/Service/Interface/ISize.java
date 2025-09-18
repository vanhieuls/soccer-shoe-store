package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.SizeDto;
import com.dailycodework.shopping_cart.DTO.Request.SizeRequest;

import java.util.List;

public interface ISize {
    List<SizeDto> getAllSizes();
    SizeDto getSizeById(Long id);
    SizeDto createSize(SizeRequest request);
    void deleteSize(Long id);
    SizeDto updateSize(Long id, SizeRequest request);
}
