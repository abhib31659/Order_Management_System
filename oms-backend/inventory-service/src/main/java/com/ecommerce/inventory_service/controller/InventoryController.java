package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.CreateInventoryRequest;
import com.ecommerce.inventory_service.dto.InventoryResponse;
import com.ecommerce.inventory_service.dto.UpdateInventoryRequest;
import com.ecommerce.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public InventoryResponse createInventory(
            @Valid @RequestBody CreateInventoryRequest request){

        return inventoryService.createInventory(request);
    }

    @GetMapping
    public List<InventoryResponse> getAllInventory(){

        return inventoryService.getAllInventory();
    }

    @GetMapping("/{id}")
    public InventoryResponse getInventoryById(
            @PathVariable Long id){

        return inventoryService.getInventoryById(id);
    }

    @PutMapping("/{id}")
    public InventoryResponse updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInventoryRequest request){

        return inventoryService.updateInventory(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteInventory(
            @PathVariable Long id){

        inventoryService.deleteInventory(id);

        return "Inventory deleted successfully!";
    }

    /// /for Product Service -> Inventory Service Communication with RestTemplate
    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(
            @PathVariable Long productId){

        return ResponseEntity.ok(
                inventoryService
                        .getInventoryByProductId(productId)
        );
    }


}
