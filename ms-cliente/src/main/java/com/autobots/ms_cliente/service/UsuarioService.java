package com.autobots.ms_cliente.service;

import com.autobots.ms_cliente.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final WebClient webClient;

    @Autowired
    public UsuarioService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public List<Usuario> listarClientesPorEmpresa(Long empresaId, String token) {
        List<Usuario> usuarios = webClient.get()
                .uri("/usuario/usuarios")
                .header(HttpHeaders.AUTHORIZATION, token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Usuario.class)
                .collectList()
                .block();
        if (usuarios == null) return List.of();
        return usuarios.stream()
                .filter(u -> u.getEmpresa() != null && empresaId.equals(u.getEmpresa().getId()))
                .filter(u -> u.getPerfis() != null && u.getPerfis().contains("CLIENTE"))
                .collect(Collectors.toList());
    }
} 