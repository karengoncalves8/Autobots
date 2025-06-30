package com.autobots.ms_veiculos.entity;

import lombok.Data;

@Data
public class Veiculo {
    private Long id;
    private String placa;
    private String modelo;
    private String marca;
    private String cor;
    private Empresa empresa;
    // Adicione outros campos relevantes se necessário
} 