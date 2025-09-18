package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Dto.ProductsizeDto;
import com.dailycodework.shopping_cart.DTO.Request.ProductSizeRequest;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Entity.ProductSize;
import com.dailycodework.shopping_cart.Entity.Size;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Mapper.ProductSizeMapper;
import com.dailycodework.shopping_cart.Repository.ProductRepository;
import com.dailycodework.shopping_cart.Repository.ProductSizeRepository;
import com.dailycodework.shopping_cart.Repository.SizeRepository;
import com.dailycodework.shopping_cart.Service.Interface.IProductSize;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Filter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpProductSize implements IProductSize {
    ProductSizeRepository productSizeRepository;
    ProductSizeMapper productSizeMapper;
    ProductRepository productRepository;
    SizeRepository sizeRepository;
    @Override
    public List<ProductsizeDto> findByProductId(Long productId) {
        return productSizeMapper.toListProductSizeDto(productSizeRepository.findByProductId(productId).orElseThrow(()->new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXIST)));
    }

    @Override
    public List<ProductsizeDto> getAll() {
        return productSizeMapper.toListProductSizeDto(productSizeRepository.findAll());
    }

    @Override
    public ProductsizeDto getById(Long id) {
        return productSizeMapper.toProductSizeDto(
                productSizeRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXIST))
        );
    }

    @Override
    public ProductSize getProductSizeById(Long id) {
        return  productSizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXIST))
                ;
    }

    @Override
    public ProductsizeDto createProductSize(ProductSizeRequest productSize) {
        if (productSizeRepository.existsBySizeIdAndProductId(productSize.getSizeId(), productSize.getProductId())) {
            throw new AppException(ErrorCode.PRODUCT_SIZE_EXISTED);
        }
        Product product = productRepository.findById(productSize.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        Size size = sizeRepository.findById(productSize.getSizeId())
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXIST));
        ProductSize productSizeEntity = ProductSize.builder()
                .product(product)
                .size(size)
                .quantity(productSize.getQuantity())
                .build();
//        ProductSize ps = productSizeMapper.toProductSize(productSize);
//        productSizeRepository.save(productSizeEntity);
        return productSizeMapper.toProductSizeDto(productSizeRepository.save(productSizeEntity));
    }

    @Override
    public void deleteProductSize(Long id) {
        if (!productSizeRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXIST);
        }
        productSizeRepository.deleteById(id);
    }

    @Override
    public ProductsizeDto update(Long id, ProductSizeRequest ps) {
        ProductSize productSize = productSizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_EXIST));
        if (productSizeRepository.existsBySizeIdAndProductId(ps.getSizeId(), ps.getProductId())) {
            throw new AppException(ErrorCode.PRODUCT_SIZE_EXISTED);
        }
        productSizeMapper.updateProductSize(productSize, ps);
        return productSizeMapper.toProductSizeDto(productSizeRepository.save(productSize));
    }

    // Implement the methods defined in the ProductSize interface here
    // For example:
    // @Override
    // public SizeDto getSizeById(Long id) {
}
