package com.ecommerce.product_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ecom_products")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_description")
    private String description;

    @Column(name = "product_price")
    private Double price;

    @Column(name = "product_stock")
    private Integer stock;

    @Column(name = "product_category")
    private String category;
}
