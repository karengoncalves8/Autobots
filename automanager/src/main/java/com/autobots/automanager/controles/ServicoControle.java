package com.autobots.automanager.controles;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelos.servico.ServicoAtualizador;
import com.autobots.automanager.modelos.servico.ServicoAdicionadorLink;
import com.autobots.automanager.modelos.servico.ServicoSelecionador;
import com.autobots.automanager.repositorios.ServicoRepositorio;

@RestController
@RequestMapping("/servico")
public class ServicoControle {
    
    @Autowired
	private ServicoRepositorio repositorio;

    @Autowired
    private ServicoSelecionador selecionador;
    
	@Autowired
	private ServicoAdicionadorLink adicionadorLink;

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
    @GetMapping("/todos")
	public ResponseEntity<List<Servico>> obterTodosServicos() {
		List<Servico> servicos = repositorio.findAll();
		if (servicos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		adicionadorLink.adicionarLinkList(servicos);
		return ResponseEntity.status(HttpStatus.OK).body(servicos);
	}
	
	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@GetMapping("/{ser_id}")
	public ResponseEntity<Servico> obterMercadoria(@PathVariable Long ser_id) {
		List<Servico> servicos = repositorio.findAll();
		Servico servico = selecionador.selecionar(new HashSet<>(servicos), ser_id);

		if (servico == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		adicionadorLink.adicionarLink(servico);

		return ResponseEntity.status(HttpStatus.OK).body(servico);
	}

	@PreAuthorize("hasRole('ROLE_GERENTE')")
    @PostMapping("/cadastrar")
	public ResponseEntity<String> cadastrarMercadoria(@RequestBody Servico servico) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (servico.getId() == null) {
			repositorio.save(servico);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasRole('ROLE_GERENTE')")
    @PutMapping("/atualizar")
	public ResponseEntity<?> atualizarMercadoria(@RequestBody Servico atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Servico servico = repositorio.getReferenceById(atualizacao.getId());
		if (servico != null) {
			ServicoAtualizador atualizador = new ServicoAtualizador();
			atualizador.atualizar(servico, atualizacao);
			repositorio.save(servico);
			status = HttpStatus.NO_CONTENT;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasRole('ROLE_GERENTE')")
    @DeleteMapping("/excluir")
	public ResponseEntity<?> excluirUsuario(@RequestBody Servico exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Servico servico = repositorio.getReferenceById(exclusao.getId());
		if (servico != null) {
			repositorio.delete(servico);
			status = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<>(status);
	}
}
