package com.autobots.ms_funcionario.controller;

import com.autobots.ms_funcionario.entity.Usuario;
import com.autobots.ms_funcionario.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {
    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Usuario>> listarFuncionariosPorEmpresa(@PathVariable Long empresaId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        List<Usuario> funcionarios = funcionarioService.listarFuncionariosPorEmpresa(empresaId, token);
        return ResponseEntity.ok(funcionarios);
    }
} 