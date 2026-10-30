package com.ecommerce.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ecom_orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "order_quantity")
    private Integer quantity;

    @Column(name = "order_total_price")
    private Double totalPrice;

    @Column(name = "order_status")
    private String orderStatus;
}
