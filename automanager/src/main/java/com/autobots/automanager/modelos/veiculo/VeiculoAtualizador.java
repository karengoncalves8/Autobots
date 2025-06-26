package com.autobots.automanager.modelos.veiculo;

import java.util.Set;

import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.StringVerificadorNulo;
import com.autobots.automanager.modelos.usuario.UsuarioAtualizador;

public class VeiculoAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();
    private UsuarioAtualizador usuarioAtualizador = new UsuarioAtualizador();

	public void atualizar(Veiculo veiculo, Veiculo atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getTipo().name())) {
				veiculo.setTipo(atualizacao.getTipo());
			}
			if (!verificador.verificar(atualizacao.getModelo())) {
				veiculo.setModelo(atualizacao.getModelo());
			}
            if (!verificador.verificar(atualizacao.getPlaca())) {
				veiculo.setPlaca(atualizacao.getPlaca());
			}
		}
	}

	public void atualizar(Set<Veiculo> veiculos, Set<Veiculo> atualizacoes) {
		for (Veiculo atualizacao : atualizacoes) {
			for (Veiculo veiculo : veiculos) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == veiculo.getId()) {
						atualizar(veiculo, atualizacao);
                        usuarioAtualizador.atualizar(veiculo.getProprietario(), atualizacao.getProprietario());
					}
				}
			}
		}
	}
}
