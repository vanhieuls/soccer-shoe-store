package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Dto.CollectionDto;
import com.dailycodework.shopping_cart.DTO.Request.CollectionRequest;
import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.Entity.Collections;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface ICollection {
    List<ProductResponse> getAllProductsInCollection(Long collectionId);
    void deleteCollection(Long collectionId);
    CollectionDto createCollection(String name, String description, MultipartFile imageFile);
    List<CollectionDto> getAllCollections();
    CollectionDto getCollectionById(Long collectionId);
//    Collections getCollectionById(Long collectionId);
//    List<Collections> getAllCollection();
    CollectionDto updateCollection(String name, String description, MultipartFile imageFile, Long collectionId);
    List<CollectionDto> getCollectionByName(String name);
}
