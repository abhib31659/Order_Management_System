package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.CreateOrderRequest;
import com.ecommerce.order_service.dto.InventoryResponse;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.dto.ProductResponse;
import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.exception.InsufficientStockException;
import com.ecommerce.order_service.exception.InventoryNotFoundException;
import com.ecommerce.order_service.exception.ProductNotFoundException;
import com.ecommerce.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final RestTemplate restTemplate;

    public OrderResponse createOrder(CreateOrderRequest request){


        ProductResponse productResponse =
                restTemplate.getForObject(
                        "http://PRODUCT-SERVICE/products/" + request.getProductId(),
                        ProductResponse.class
                );

        InventoryResponse inventoryResponse =
                restTemplate.getForObject(
                        "http://INVENTORY-SERVICE/inventory/" + request.getProductId(),
                        InventoryResponse.class
                );

        if(productResponse == null){
            throw new ProductNotFoundException("Product not found!");
        }

        if(inventoryResponse == null){
            throw new InventoryNotFoundException("Inventory not found!");
        }


        if(inventoryResponse.getQuantity() < request.getQuantity()){
            throw new InsufficientStockException("Insufficient stock available!");
        }

        Double totalPrice =
                productResponse.getPrice() * request.getQuantity();


        Order order = Order.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .totalPrice(totalPrice)
                .orderStatus("PLACED")
                .build();

        Order savedOrder = orderRepository.save(order);

        Integer remainingStock =
                inventoryResponse.getQuantity() - request.getQuantity();

        restTemplate.put(
                "http://INVENTORY-SERVICE/inventory/" + inventoryResponse.getId(),
                InventoryResponse.builder()
                        .id(inventoryResponse.getId())
                        .productId(inventoryResponse.getProductId())
                        .quantity(remainingStock)
                        .build()
        );

        return OrderResponse.builder()
                .id(savedOrder.getId())
                .productId(savedOrder.getProductId())
                .quantity(savedOrder.getQuantity())
                .totalPrice(savedOrder.getTotalPrice())
                .orderStatus(savedOrder.getOrderStatus())
                .build();
    }
}
