package com.autobots.automanager.modelos.veiculo;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Veiculo;

@Component
public class VeiculoSelecionador {
    public Veiculo selecionar(List<Veiculo> veiculos, long id) {
		for (Veiculo veiculo : veiculos) {
			if (veiculo.getId() == id) {
				Veiculo selecionado = veiculo;
				return selecionado;
			}
		}
		return null;
	}
}
