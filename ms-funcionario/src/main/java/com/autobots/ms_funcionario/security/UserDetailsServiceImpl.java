package com.autobots.ms_funcionario.security;

import com.autobots.ms_funcionario.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    // Para simplificação, busca em memória (ajuste conforme sua lógica de autenticação)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Não há persistência local, então sempre lança exceção
        throw new UsernameNotFoundException("Usuário não encontrado");
    }
} 