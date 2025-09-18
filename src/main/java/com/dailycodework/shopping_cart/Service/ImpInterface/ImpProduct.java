package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Request.ProductRequest;
import com.dailycodework.shopping_cart.DTO.Request.ProductUpdateRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.Entity.Category;
import com.dailycodework.shopping_cart.Entity.Collections;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Helper.ProductSpecification.ProductFilter;
import com.dailycodework.shopping_cart.Helper.ProductSpecification.ProductSpecification;
import com.dailycodework.shopping_cart.Mapper.ProductMapper;
import com.dailycodework.shopping_cart.Repository.CategoryRepository;
import com.dailycodework.shopping_cart.Repository.CollectionRepository;
import com.dailycodework.shopping_cart.Repository.ProductRepository;
import com.dailycodework.shopping_cart.Service.Interface.IProduct;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImpProduct implements IProduct {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    CollectionRepository collectionRepository;
    @Override
    public ApiResponse<List<ProductResponse>> getAllProducts(){
        List<ProductResponse> productResponses = productRepository.findAll().stream().map(productMapper::toProductResponse).toList();
        return ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("success")
                .result(productResponses)
                .build();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return productMapper.toProductResponse(productRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_FOUND)));

    }

    @Override
    public ApiResponse<Void> deleteProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.deleteById(id);
        return ApiResponse.<Void>builder()
                .message("Product was delete")
                .build();
    }

    @Override
    public List<Product> getProductsByCategory(String category){
        return productRepository.findByCategoryName(category);
    }
    @Override
    public List<ProductResponse> getProductsByBrand(String brand){
        return productRepository.findByBrand(brand).stream().map(productMapper::toProductResponse).toList();
    }
    @Override
    public List<ProductResponse> getProductsByName(String name){
        return productRepository.findByName(name).stream().map(productMapper::toProductResponse).toList();
    }
    @Override
    public List<ProductResponse> getProductsByCategoryAndBrand(String category, String brand){
        return productRepository.findByCategoryNameAndBrand(category, brand).stream().map(productMapper::toProductResponse).collect(Collectors.toList());
    }
    @Override
    public List<ProductResponse> getProductsByCategoryAndName(String category, String name){
        return productRepository.findByCategoryNameAndName(category, name).stream().map(productMapper::toProductResponse).collect(Collectors.toList());
    }
    @Override
    public List<ProductResponse> getProductsByBrandAndName(String brand, String name){
        return productRepository.findByBrandAndName(brand, name).stream().map(productMapper::toProductResponse).collect(Collectors.toList());
    }
    @Override
    public Long countProductsByBrandAndName(String brand, String name){
        return productRepository.countByBrandAndName(brand,name);
    }

    @Override
    public Product createProduct(ProductRequest request) {
        Category category = categoryRepository.findByName(request.getCategory().getName()).orElseGet(()->{
            Category newCategory = Category.builder()
                    .name(request.getCategory().getName())
                    .build();
            return categoryRepository.save(newCategory);
        });
        request.setCategory(category);
        Product product = productMapper.toProduct(request);
        Collections collection = collectionRepository.findById(request.getCollectionId())
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));
        product.setCollection(collection);
        return productRepository.save(product);
    }

    @Override
    public Product updateProductExisted(ProductUpdateRequest request, Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productMapper.updateProduct(product,request);
        return productRepository.save(product);
    }


    @Override
    public Page<ProductResponse> getProductByFilter(ProductFilter filter) {

        Specification<Product> specification = Specification.where(null);
//        specification = specification.and(ProductSpecification.findProducts(filter));
        Sort.Direction direction = filter.getSort().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(
                filter.getPage() != null ? filter.getPage() : 0,
                filter.getSize() != null ? filter.getSize() : 10
                , Sort.by(direction, filter.getPropertySort() != null && !filter.getPropertySort().isEmpty() ? filter.getPropertySort() : "name"));
        Page<Product> productPage = productRepository.findAll(specification, pageable);
        return productPage.map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getProducts(Integer pageNumber, Integer pageSize, String category, String brand,
                                             String properties, String sortDir,
                                             BigDecimal minPrice, BigDecimal maxPrice ){
//        Pageable         -> Chỉ định yêu cầu phân trang (số trang, kích thước, sắp xếp)
//PageRequest      -> Tạo cụ thể đối tượng Pageable
//Page<T>          -> Kết quả trả về từ phương thức truy vấn (có dữ liệu + thông tin phân trang)
        Specification<Product> specification = Specification.where(null);
        specification = specification.and(ProductSpecification.findProducts(category, brand, minPrice, maxPrice));
        Pageable pageable = null;
        if (pageNumber == null || pageNumber < 0) pageNumber = 0;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        String sortField = (properties != null) ? properties : "name";
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//        if(properties!=null && sortDir.isEmpty()){
////            PageRequest là class triển khai Pageable
//            pageable = PageRequest.of(pageNumber,pageSize, Sort.Direction.ASC,properties);
//        }
//            pageable = PageRequest.of(pageNumber,pageSize,Sort.Direction.ASC,"name");
        pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField ));
        Page<Product> productPage = productRepository.findAll(specification, pageable);
//        Page<Product> productPage = productRepository.findByPriceBetween(min, max, pageable);
        return productPage.map(productMapper::toProductResponse);
    }
}
