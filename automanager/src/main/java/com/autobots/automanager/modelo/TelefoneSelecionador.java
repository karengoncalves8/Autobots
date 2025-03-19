package com.autobots.automanager.modelo;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Telefone;

@Component
public class TelefoneSelecionador {
	public Telefone selecionar(Set<Telefone> telefones, Long id) {
		for (Telefone telefone : telefones) {
			if (telefone.getId() == id) {
				Telefone selecionado = telefone;
				return selecionado;
			}
		}
		return null;
	}
}