package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.dto.CreateProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.dto.UpdateProductRequest;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponse createProduct(
            @Valid @RequestBody CreateProductRequest request
    ){
        return productService.createProduct(request);
    }


    @GetMapping
    public List<ProductResponse> getAllProducts(){

        return productService.getAllProducts();
    }


    @GetMapping("/{id}")
    public ProductResponse getProductById(
            @PathVariable Long id
    ){

        return productService.getProductById(id);
    }


    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
            ){

        return productService.updateProduct(id, request);
    }


    @DeleteMapping("/{id}")
    public String deleteProduct(
            @PathVariable Long id
    ){
        productService.deleteProduct(id);

        return "Product deleted successfully!";
    }
}
