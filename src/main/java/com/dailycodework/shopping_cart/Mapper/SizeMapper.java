package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.SizeDto;
import com.dailycodework.shopping_cart.DTO.Request.SizeRequest;
import com.dailycodework.shopping_cart.DTO.Request.UserRequest;
import com.dailycodework.shopping_cart.Entity.Size;
import com.dailycodework.shopping_cart.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductSizeMapper.class})
public interface SizeMapper {
    Size toSize(SizeRequest request);
    SizeDto toSizeDto(Size size);
    List<SizeDto> toListSizeDto(List<Size> sizes);
    void updateSize(@MappingTarget Size size, SizeRequest request);
}
