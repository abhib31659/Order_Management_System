package com.ecommerce.user_service.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "ecom_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name" , nullable = false)
    private String name;

    @Column(name = "user_email" , nullable = false , unique = true)
    private String email;

    @Column(name = "user_pass" , nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
