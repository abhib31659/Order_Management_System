package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.CreateInventoryRequest;
import com.ecommerce.inventory_service.dto.InventoryResponse;
import com.ecommerce.inventory_service.dto.UpdateInventoryRequest;
import com.ecommerce.inventory_service.entity.Inventory;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryResponse createInventory(CreateInventoryRequest request){

        Inventory inventory = Inventory.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();

        Inventory savedInventory = inventoryRepository.save(inventory);

        return mapToResponse(savedInventory);
    }

    public List<InventoryResponse> getAllInventory(){

        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InventoryResponse getInventoryById(Long id){

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow( () ->
                        new RuntimeException("Inventory not found with id: "+id));

        return mapToResponse(inventory);
    }

    public InventoryResponse updateInventory(Long id,
                                             UpdateInventoryRequest request){

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow( () ->
                        new RuntimeException("Inventory not found with id: "+id));

        inventory.setProductId(request.getProductId());
        inventory.setQuantity(request.getQuantity());

        Inventory updatedInventory = inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory);
    }

    public void deleteInventory(Long id){

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow( () ->
                        new RuntimeException("Inventory not found with id: "+id));

        inventoryRepository.delete(inventory);
    }

    private InventoryResponse mapToResponse (Inventory inventory){

        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .build();
    }

    /// /for Product Service -> Inventory Service Communication with RestTemplate
    public InventoryResponse getInventoryByProductId(Long productId){

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow( () ->
                        new RuntimeException(
                                "Inventory not found for product id: "+productId
                        ));

        return mapToResponse(inventory);
    }
}
