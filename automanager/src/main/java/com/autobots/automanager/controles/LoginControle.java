package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.autobots.automanager.dtos.LoginCodigoDTO;
import com.autobots.automanager.dtos.LoginSenhaDTO;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@Controller
public class LoginControle {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginSenhaDTO loginDto) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginDto.getNomeUsuario(), loginDto.getSenha());
            this.authenticationManager.authenticate(authToken);
            return ResponseEntity.status(HttpStatus.OK).body("Login realizado com sucesso!");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    // Login com código de barras
    @PostMapping("/login-codigo")
    public ResponseEntity<?> loginCodigo(@RequestBody LoginCodigoDTO dto) {
        for (Usuario usuario : usuarioRepositorio.findAll()) {
            for (Credencial cred : usuario.getCredenciais()) {
                if (cred instanceof CredencialCodigoBarra) {
                    CredencialCodigoBarra ccb = (CredencialCodigoBarra) cred;
                    if (ccb.getCodigo() == dto.getCodigo() && !ccb.isInativo()) {
                        return ResponseEntity.status(HttpStatus.OK).body("Login realizado com sucesso!");
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código inválido ou inativo");
    }
}
