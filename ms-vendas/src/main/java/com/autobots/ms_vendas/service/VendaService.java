package com.autobots.ms_vendas.service;

import com.autobots.ms_vendas.entity.Venda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaService {
    private final WebClient webClient;

    @Autowired
    public VendaService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public List<Venda> listarVendasPorEmpresaEPeriodo(Long empresaId, LocalDate inicio, LocalDate fim, String token) {
        List<Venda> vendas = webClient.get()
                .uri("/venda/todos")
                .header(HttpHeaders.AUTHORIZATION, token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Venda.class)
                .collectList()
                .block();
        if (vendas == null) return List.of();
        return vendas.stream()
                .filter(v -> v.getEmpresa() != null && empresaId.equals(v.getEmpresa().getId()))
                .filter(v -> {
                    if (v.getCadastro() == null) return false;
                    LocalDate dataVenda = v.getCadastro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return (dataVenda.isEqual(inicio) || dataVenda.isAfter(inicio)) && (dataVenda.isEqual(fim) || dataVenda.isBefore(fim));
                })
                .collect(Collectors.toList());
    }
} 