package com.autobots.ms_funcionario.entity;

import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
public class Usuario {
    private Long id;
    private String nome;
    private String nomeSocial;
    private Set<String> perfis = new HashSet<>();
    private Set<Telefone> telefones = new HashSet<>();
    private Endereco endereco;
    private Set<Documento> documentos = new HashSet<>();
    private Set<Email> emails = new HashSet<>();
    private Empresa empresa;
} 