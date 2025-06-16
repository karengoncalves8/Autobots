package com.autobots.automanager.modelos.endereco;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Endereco;

@Component
public class EnderecoSelecionador {
	public Endereco selecionar(List<Endereco> enderecos, long id) {
		for (Endereco endereco : enderecos) {
			if (endereco.getId() == id) {
				Endereco selecionado = endereco;
				return selecionado;
			}
		}
		return null;
	}
}
