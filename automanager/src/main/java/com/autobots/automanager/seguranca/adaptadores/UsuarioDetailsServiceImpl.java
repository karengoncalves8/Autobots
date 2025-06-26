package com.autobots.automanager.seguranca.adaptadores;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.servicos.UsuarioServico;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsuarioServico usuarioServico;

    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Map<String, Object> map = usuarioServico.getUsuarioByUsername(username);
		Usuario usuario = (Usuario) map.get("usuario");
		Credencial credencial = (Credencial) map.get("credencial");
		if (usuario != null && credencial != null) {
			return new UsuarioDetailsImpl(usuario, credencial);
		} 

		throw new UsernameNotFoundException("Usuário não encontrado");
	}
}
