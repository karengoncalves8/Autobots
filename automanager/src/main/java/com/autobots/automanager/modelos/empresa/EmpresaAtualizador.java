package com.autobots.automanager.modelos.empresa;

import java.util.ArrayList;
import java.util.List;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.StringVerificadorNulo;
import com.autobots.automanager.modelos.endereco.EnderecoAtualizador;
import com.autobots.automanager.modelos.mercadoria.MercadoriaAtualizador;
import com.autobots.automanager.modelos.servico.ServicoAtualizador;
import com.autobots.automanager.modelos.telefone.TelefoneAtualizador;
import com.autobots.automanager.modelos.usuario.UsuarioAtualizador;
import com.autobots.automanager.modelos.venda.VendaAtualizador;

public class EmpresaAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    private TelefoneAtualizador telefoneAtualizador = new TelefoneAtualizador();
    private EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();
    private UsuarioAtualizador usuarioAtualizador = new UsuarioAtualizador();
	private MercadoriaAtualizador mercadoriaAtualizador = new MercadoriaAtualizador();
	private ServicoAtualizador servicoAtualizador = new ServicoAtualizador();
	private VendaAtualizador vendaAtualizador = new VendaAtualizador();

	public void atualizarDados(Empresa empresa, Empresa atualizacao) {
		if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getRazaoSocial())) {
				empresa.setRazaoSocial(atualizacao.getRazaoSocial());
			}
			if (!verificador.verificar(atualizacao.getNomeFantasia())) {
				empresa.setNomeFantasia(atualizacao.getNomeFantasia());
			}
            if (!verificador.verificarGenerico(atualizacao.getCadastro())) {
				empresa.setCadastro(atualizacao.getCadastro());
			}
		}
	}

	public void atualizar(Empresa empresa, Empresa atualizacao) {
		atualizarDados(empresa, atualizacao);
		telefoneAtualizador.atualizar(empresa.getTelefones(), atualizacao.getTelefones());
        enderecoAtualizador.atualizar(empresa.getEndereco(), atualizacao.getEndereco());
        
        List<Usuario> usuarios = new ArrayList<>(empresa.getUsuarios());
        List<Usuario> usuariosAtualizacao = new ArrayList<>(atualizacao.getUsuarios());
        for (int i = 0; i < usuarios.size() && i < usuariosAtualizacao.size(); i++) {
            Usuario usuario = usuarios.get(i);
            Usuario usuarioAtualizacao = usuariosAtualizacao.get(i);
            usuarioAtualizador.atualizar(usuario, usuarioAtualizacao);
        }
		
		mercadoriaAtualizador.atualizar(empresa.getMercadorias(), atualizacao.getMercadorias());
		servicoAtualizador.atualizar(empresa.getServicos(), atualizacao.getServicos());
        vendaAtualizador.atualizar(empresa.getVendas(), atualizacao.getVendas());
	}
}
