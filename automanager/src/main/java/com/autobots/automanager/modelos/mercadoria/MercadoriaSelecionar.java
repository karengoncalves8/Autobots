package com.autobots.automanager.modelos.mercadoria;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Mercadoria;

@Component
public class MercadoriaSelecionar {
	public Mercadoria selecionar(Set<Mercadoria> mercadorias, Long id) {
		for (Mercadoria mercadoria : mercadorias) {
			if (mercadoria.getId() == id) {
				Mercadoria selecionado = mercadoria;
				return selecionado;
			}
		}
		return null;
	}
}
