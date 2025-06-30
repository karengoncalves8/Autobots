package com.autobots.ms_veiculos.controller;

import com.autobots.ms_veiculos.entity.Veiculo;
import com.autobots.ms_veiculos.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {
    @Autowired
    private VeiculoService veiculoService;

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Veiculo>> listarVeiculosPorEmpresa(@PathVariable Long empresaId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        List<Veiculo> veiculos = veiculoService.listarVeiculosPorEmpresa(empresaId, token);
        return ResponseEntity.ok(veiculos);
    }
} 