package com.autobots.ms_vendas.controller;

import com.autobots.ms_vendas.entity.Venda;
import com.autobots.ms_vendas.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {
    @Autowired
    private VendaService vendaService;

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Venda>> listarVendasPorEmpresaEPeriodo(
            @PathVariable Long empresaId,
            @RequestParam("inicio") String inicio,
            @RequestParam("fim") String fim,
            HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);
        List<Venda> vendas = vendaService.listarVendasPorEmpresaEPeriodo(empresaId, dataInicio, dataFim, token);
        return ResponseEntity.ok(vendas);
    }
} 