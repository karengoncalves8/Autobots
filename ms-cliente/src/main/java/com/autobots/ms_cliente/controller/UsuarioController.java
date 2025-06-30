package com.autobots.ms_cliente.controller;

import com.autobots.ms_cliente.entity.Usuario;
import com.autobots.ms_cliente.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/clientes")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Usuario>> listarClientesPorEmpresa(@PathVariable Long empresaId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        List<Usuario> clientes = usuarioService.listarClientesPorEmpresa(empresaId, token);
        return ResponseEntity.ok(clientes);
    }
} 