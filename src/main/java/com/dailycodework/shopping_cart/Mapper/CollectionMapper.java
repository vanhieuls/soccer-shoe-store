package com.dailycodework.shopping_cart.Mapper;

import com.dailycodework.shopping_cart.DTO.Dto.CollectionDto;
import com.dailycodework.shopping_cart.DTO.Request.CollectionRequest;
import com.dailycodework.shopping_cart.Entity.Collections;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, ImageMapper.class})
public interface CollectionMapper {
    Collections toCollection(CollectionRequest collectionRequest);
    CollectionDto toCollectionDto(Collections collection);
    List<CollectionDto> toCollectionDtos(List<Collections> collections);
    void updateCollectionFromRequest( CollectionRequest collectionRequest,@MappingTarget Collections collection);
}
