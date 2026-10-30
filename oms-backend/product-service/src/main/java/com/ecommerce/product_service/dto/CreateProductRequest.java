package com.ecommerce.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {

    @NotBlank(message = "Product name is required!")
    private String name;

    @NotBlank(message = "Product description is required!")
    private String description;

    @NotNull(message = "Price is required!")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Stock is required!")
    @Min(value = 0 , message = "Stock can't be negative!")
    private Integer stock;

    @NotBlank(message = "Category is required!")
    private String category;
}
