package com.autobots.ms_funcionario.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Documento {
    private Long id;
    private String tipo;
    private Date dataEmissao;
    private String numero;
} 