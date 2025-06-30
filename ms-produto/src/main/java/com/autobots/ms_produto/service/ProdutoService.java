package com.autobots.ms_produto.service;

import com.autobots.ms_produto.entity.Servico;
import com.autobots.ms_produto.entity.Mercadoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {
    private final WebClient webClient;

    @Autowired
    public ProdutoService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public List<Object> listarProdutosPorEmpresa(Long empresaId, String token) {
        List<Servico> servicos = webClient.get()
                .uri("/servico/todos")
                .header(HttpHeaders.AUTHORIZATION, token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Servico.class)
                .collectList()
                .block();
        List<Mercadoria> mercadorias = webClient.get()
                .uri("/mercadoria/todos")
                .header(HttpHeaders.AUTHORIZATION, token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Mercadoria.class)
                .collectList()
                .block();
        List<Object> produtos = new ArrayList<>();
        if (servicos != null) {
            produtos.addAll(servicos.stream().filter(s -> s.getEmpresa() != null && empresaId.equals(s.getEmpresa().getId())).collect(Collectors.toList()));
        }
        if (mercadorias != null) {
            produtos.addAll(mercadorias.stream().filter(m -> m.getEmpresa() != null && empresaId.equals(m.getEmpresa().getId())).collect(Collectors.toList()));
        }
        return produtos;
    }
}
