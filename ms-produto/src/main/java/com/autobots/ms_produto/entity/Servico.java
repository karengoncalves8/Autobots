package com.autobots.ms_produto.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Servico {
    private Long id;
    private String nome;
    private String descricao;
    private Double valor;
    private Date cadastro;
    private Empresa empresa;
}