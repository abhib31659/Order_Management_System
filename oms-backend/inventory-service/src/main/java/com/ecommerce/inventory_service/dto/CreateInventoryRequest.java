package com.ecommerce.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInventoryRequest {

    @NotNull(message = "Product Id is required!")
    private Long productId;

    @NotNull(message = "Quantity is required!")
    @Min(value = 0, message = "Quantity can't be negative!")
    private Integer quantity;
}
