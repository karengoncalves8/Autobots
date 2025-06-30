package com.autobots.ms_veiculos.service;

import com.autobots.ms_veiculos.entity.Veiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeiculoService {
    private final WebClient webClient;

    @Autowired
    public VeiculoService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public List<Veiculo> listarVeiculosPorEmpresa(Long empresaId, String token) {
        List<Veiculo> veiculos = webClient.get()
                .uri("/veiculo/todos")
                .header(HttpHeaders.AUTHORIZATION, token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Veiculo.class)
                .collectList()
                .block();
        if (veiculos == null) return List.of();
        return veiculos.stream()
                .filter(v -> v.getEmpresa() != null && empresaId.equals(v.getEmpresa().getId()))
                .collect(Collectors.toList());
    }
} 