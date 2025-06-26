package com.autobots.automanager.modelos.servico;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Servico;

@Component
public class ServicoSelecionador {
	public Servico selecionar(Set<Servico> servicos, Long id) {
		for (Servico servico : servicos) {
			if (servico.getId() == id) {
				Servico selecionado = servico;
				return selecionado;
			}
		}
		return null;
	}
}
