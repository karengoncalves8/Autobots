package com.autobots.automanager.modelos.mercadoria;

import java.util.Set;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelos.StringVerificadorNulo;

public class MercadoriaAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Mercadoria mercadoria, Mercadoria atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificarGenerico(atualizacao.getFabricao())) {
				mercadoria.setFabricao(atualizacao.getFabricao());
			}
			if (!verificador.verificarGenerico(atualizacao.getCadastro())) {
				mercadoria.setCadastro(atualizacao.getCadastro());
			}
            if (!verificador.verificar(atualizacao.getNome())) {
				mercadoria.setNome(atualizacao.getNome());
			}
            if (!verificador.verificarGenerico(atualizacao.getQuantidade())) {
				mercadoria.setQuantidade(atualizacao.getQuantidade());
			}
            if (!verificador.verificarGenerico(atualizacao.getValor())) {
				mercadoria.setValor(atualizacao.getValor());
			}
            if (!verificador.verificar(atualizacao.getDescricao())) {
				mercadoria.setDescricao(atualizacao.getDescricao());
			}
		}
	}

	public void atualizar(Set<Mercadoria> mercadorias, Set<Mercadoria> atualizacoes) {
		for (Mercadoria atualizacao : atualizacoes) {
			for (Mercadoria mercadoria : mercadorias) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == mercadoria.getId()) {
						atualizar(mercadoria, atualizacao);
					}
				}
			}
		}
	}
}
