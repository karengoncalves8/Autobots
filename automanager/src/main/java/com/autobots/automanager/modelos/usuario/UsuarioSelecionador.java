package com.autobots.automanager.modelos.usuario;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Usuario;

@Component
public class UsuarioSelecionador {
    public Usuario selecionar(List<Usuario> usuarios, long id) {
		for (Usuario usuario : usuarios) {
			if (usuario.getId() == id) {
				Usuario selecionado = usuario;
				return selecionado;
			}
		}
		return null;
	}
}
