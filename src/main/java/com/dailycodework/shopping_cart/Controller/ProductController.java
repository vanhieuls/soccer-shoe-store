package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Request.ProductRequest;
import com.dailycodework.shopping_cart.DTO.Request.ProductUpdateRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.DTO.Response.ProductResponse;
import com.dailycodework.shopping_cart.Entity.Product;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Helper.ProductSpecification.ProductFilter;
import com.dailycodework.shopping_cart.Service.Interface.IProduct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    IProduct productService;
    @GetMapping("/getall")
    public ApiResponse<List<ProductResponse>> getAllProducts(){
        return productService.getAllProducts();
    }
    @DeleteMapping("/deleteProduct/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProductById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("delete product success")
                .build();
    }
    @GetMapping("/getproduct/{id}/id")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long id){
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("success")
                .result(productService.getProductById(id))
                .build();
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteProductById(@PathVariable Long id){
        return productService.deleteProductById(id);
    }
//    @GetMapping("/product/{category}/category")
//    public ApiResponse<List<Product>> getProductsByCategory(@PathVariable String category){
//        List<Product> products = productService.getProductsByCategory(category);
//        if(products.isEmpty()){
//            return ApiResponse.<List<Product>>builder()
//                    .code(200)
//                    .message("products is empty")
//                    .build();
//        }
//        return ApiResponse.<List<Product>>builder()
//                .code(200)
//                .message("get product success")
//                .result(products)
//                .build();
//    }
//    @GetMapping("/product/brand")
//    public ApiResponse<List<ProductResponse>> getProductsByBrand(@RequestParam String brand){
//
//        List<ProductResponse> products = productService.getProductsByBrand(brand);
//        if(products.isEmpty()){
//            return ApiResponse.<List<ProductResponse>>builder()
//                    .code(200)
//                    .message("product is empty")
//                    .build();
//        }
//        return ApiResponse.<List<ProductResponse>>builder()
//                .code(200)
//                .message("get product success")
//                .result(products)
//                .build();
//    }
    @GetMapping("/product/name")
    public ApiResponse<List<ProductResponse>> getProductsByName(@RequestParam String name){
        List<ProductResponse> products = productService.getProductsByBrand(name);
        return ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("get product success")
                .result(products)
                .build();
    }
//    @GetMapping("/product/category-and-brand")
//    public ApiResponse<List<ProductResponse>> getProductsByCategoryAndBrand(@RequestParam String category,@RequestParam String brand){
//        List<ProductResponse> products = productService.getProductsByCategoryAndBrand(category,brand);
//        return ApiResponse.<List<ProductResponse>>builder()
//                .code(200)
//                .message("get product success")
//                .result(products)
//                .build();
//    }
//    @GetMapping("/product/category-and-name")
//    public ApiResponse<List<ProductResponse>> getProductsByCategoryAndName(@RequestParam String category,@RequestParam String name){
//        List<ProductResponse> products = productService.getProductsByCategoryAndName(category,name);
//        return ApiResponse.<List<ProductResponse>>builder()
//                .code(200)
//                .message("get product success")
//                .result(products)
//                .build();
//    }
//    @GetMapping("/getproduct/{brand}/{name}/brand-and-name")
//    ApiResponse<List<ProductResponse>> getProductsByBrandAndName(@PathVariable String brand,@PathVariable String name){
//        List<ProductResponse> products = productService.getProductsByBrandAndName(brand,name);
//        return ApiResponse.<List<ProductResponse>>builder()
//                .code(200)
//                .message("get product success")
//                .result(products)
//                .build();
//    }
//    @GetMapping("/count-product/{brand}/{name}/brand-and-name")
//    public ApiResponse<Long> countProductsByBrandAndName(@PathVariable String brand,@PathVariable String name){
//        Long count = productService.countProductsByBrandAndName(brand,name);
//        return ApiResponse.<Long>builder()
//                .code(200)
//                .message("count product success")
//                .result(count)
//                .build();
//    }
    @PostMapping("/create")
    public ApiResponse<Product> createProduct (@RequestBody ProductRequest request){
        Product product = productService.createProduct(request);
        return ApiResponse.<Product>builder()
                .code(200)
                .message("create product success")
                .result(product)
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<Product> updateProductExisted(@RequestBody ProductUpdateRequest request,@PathVariable Long id){
        Product product = productService.updateProductExisted(request,id);
        return ApiResponse.<Product>builder()
                .code(200)
                .message("update product success")
                .result(product)
                .build();
    }
    @GetMapping("/get-products")
    public ApiResponse<Page<ProductResponse>> getProducts(@RequestParam (required = false,defaultValue = "0") Integer pageNumber,@RequestParam (required = false,defaultValue = "10")Integer pageSize,@RequestParam (required = false) String category,@RequestParam (required = false) String brand,@RequestParam (required = false,defaultValue = "name") String properties,@RequestParam (required = false, defaultValue = "asc") String sortDir,
                                                                   @RequestParam (required = false) BigDecimal minPrice,
                                                                   @RequestParam (required = false) BigDecimal maxPrice ){
        return ApiResponse.<Page<ProductResponse>>builder()
                .code(200)
                .message("get product success")
                .result(productService.getProducts(pageNumber,pageSize,category, brand,properties,sortDir,minPrice,maxPrice))
                .build();
    }
    @GetMapping("/get-products/filter")
    public ApiResponse<Page<ProductResponse>> getProductByFilter (@RequestBody ProductFilter filter){
        Page<ProductResponse> products = productService.getProductByFilter(filter);
        return ApiResponse.<Page<ProductResponse>>builder()
                .code(200)
                .message("get product success")
                .result(products)
                .build();
    }

}
