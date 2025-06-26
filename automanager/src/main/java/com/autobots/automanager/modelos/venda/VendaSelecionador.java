package com.autobots.automanager.modelos.venda;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Venda;

@Component
public class VendaSelecionador {
    public Venda selecionar(List<Venda> vendas, long id) {
		for (Venda venda : vendas) {
			if (venda.getId() == id) {
				Venda selecionado = venda;
				return selecionado;
			}
		}
		return null;
	}
}
