package com.autobots.automanager.entidades;

import jakarta.persistence.Column;

public class CredencialSenha extends Credencial {
    
	@Column(nullable = false, unique = true)
	private String nomeUsuario;

	@Column(nullable = false)
	private String senha;
	
}
