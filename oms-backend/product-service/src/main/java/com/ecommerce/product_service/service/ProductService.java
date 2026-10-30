package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.*;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final RestTemplate restTemplate;

    public ProductResponse createProduct(
            CreateProductRequest request
    ){

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(request.getCategory())
                .build();

        Product savedProduct =
                productRepository.save(product);

        /// /REST-TEMPLATE SERVICE Communication start
        InventoryRequest inventoryRequest = InventoryRequest.builder()
                .productId(savedProduct.getId())
                .quantity(request.getStock())
                .build();

        restTemplate.postForObject(
                "http://INVENTORY-SERVICE/inventory",
                inventoryRequest,
                Object.class
        );
        /// /REST-TEMPLATE SERVICE Communication end

        return ProductResponse.builder()
                .id(savedProduct.getId())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .stock(savedProduct.getStock())
                .category(savedProduct.getCategory())
                .build();
    }


    /// /Get all Products
    public List<ProductResponse> getAllProducts() {

        List<Product> products =
                productRepository.findAll();

        return products.stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .category(product.getCategory())
                        .build())
                .toList();
    }

    /// /Get product by ID
    public ProductResponse getProductById(Long id){

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found!"));

        /// /REST-TEMPLATE SERVICE Communication start
        InventoryResponse inventoryResponse =
                restTemplate.getForObject(
                        "http://INVENTORY-SERVICE/inventory/" + id,
                        InventoryResponse.class
                );
        /// /REST-TEMPLATE SERVICE Communication end


        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
//                .stock(product.getStock())
                .stock(inventoryResponse.getQuantity()) /// / in replacement of ".stock(product.getStock())" for fetching the quantity in the form of stock in the product service
                .category(product.getCategory())
                .build();
    }


    /// /Product Update method
    public ProductResponse updateProduct(
            Long id,
            UpdateProductRequest request
    ){

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found!"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());

        Product updatedProduct =
                productRepository.save(product);

        return ProductResponse.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .description(updatedProduct.getDescription())
                .price(updatedProduct.getPrice())
                .stock(updatedProduct.getStock())
                .category(updatedProduct.getCategory())
                .build();
    }


    /// /Delete by Id method
    public void deleteProduct(Long id){

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found!"));

        productRepository.delete(product);
    }


}
