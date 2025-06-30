package com.autobots.ms_funcionario.entity;

import lombok.Data;

@Data
public class Endereco {
    private Long id;
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String codigoPostal;
    private String informacoesAdicionais;
} 