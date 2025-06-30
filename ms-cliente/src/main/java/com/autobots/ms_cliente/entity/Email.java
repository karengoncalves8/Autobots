package com.autobots.ms_cliente.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String endereco;
} 