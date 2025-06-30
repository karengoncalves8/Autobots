package com.autobots.ms_vendas.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Venda {
    private Long id;
    private Date cadastro;
    private Empresa empresa;
    // Adicione outros campos relevantes se necessário
} 