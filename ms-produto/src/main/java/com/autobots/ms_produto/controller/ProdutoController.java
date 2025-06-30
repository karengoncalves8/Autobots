package com.autobots.ms_produto.controller;

import com.autobots.ms_produto.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Object>> listarProdutosPorEmpresa(@PathVariable Long empresaId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        List<Object> produtos = produtoService.listarProdutosPorEmpresa(empresaId, token);
        return ResponseEntity.ok(produtos);
    }
}
