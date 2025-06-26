package com.autobots.automanager.modelos.venda;

import java.util.Set;

import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.StringVerificadorNulo;
import com.autobots.automanager.modelos.mercadoria.MercadoriaAtualizador;
import com.autobots.automanager.modelos.servico.ServicoAtualizador;
import com.autobots.automanager.modelos.usuario.UsuarioAtualizador;
import com.autobots.automanager.modelos.veiculo.VeiculoAtualizador;

public class VendaAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();
    private UsuarioAtualizador usuarioAtualizador = new UsuarioAtualizador();
    private MercadoriaAtualizador mercadoriaAtualizador = new MercadoriaAtualizador();
    private ServicoAtualizador servicoAtualizador = new ServicoAtualizador();
    private VeiculoAtualizador veiculoAtualizador = new VeiculoAtualizador();

	public void atualizar(Venda venda, Venda atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificarGenerico(atualizacao.getCadastro())) {
				venda.setCadastro(atualizacao.getCadastro());
			}
			if (!verificador.verificar(atualizacao.getIdentificacao())) {
				venda.setIdentificacao(atualizacao.getIdentificacao());
			}
		}
	}

	public void atualizar(Set<Venda> vendas, Set<Venda> atualizacoes) {
		for (Venda atualizacao : atualizacoes) {
			for (Venda venda : vendas) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == venda.getId()) {
						atualizar(venda, atualizacao);
                        usuarioAtualizador.atualizar(venda.getCliente(), atualizacao.getCliente());
                        usuarioAtualizador.atualizar(venda.getFuncionario(), atualizacao.getFuncionario());
                        mercadoriaAtualizador.atualizar(venda.getMercadorias(), atualizacao.getMercadorias());
                        servicoAtualizador.atualizar(venda.getServicos(), atualizacao.getServicos());
                        veiculoAtualizador.atualizar(venda.getVeiculo(), atualizacao.getVeiculo());
                    }
				}
			}
		}
	}
}
