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

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelos.mercadoria.MercadoriaAdicionadorLink;
import com.autobots.automanager.modelos.mercadoria.MercadoriaAtualizador;
import com.autobots.automanager.modelos.mercadoria.MercadoriaSelecionar;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {
    @Autowired
	private MercadoriaRepositorio mer_repo;

    @Autowired
    private MercadoriaSelecionar mer_selecionador;
    
	@Autowired
	private MercadoriaAdicionadorLink adicionadorLink;

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
    @GetMapping("/todos")
	public ResponseEntity<List<Mercadoria>> obterTodasMercadorias() {
		List<Mercadoria> mercadorias = mer_repo.findAll();
		if (mercadorias.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		adicionadorLink.adicionarLinkList(mercadorias);
		return ResponseEntity.status(HttpStatus.OK).body(mercadorias);
	}
	
	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@GetMapping("/{mer_id}")
	public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable Long mer_id) {
		List<Mercadoria> mercadorias = mer_repo.findAll();
		Mercadoria mercadoria = mer_selecionador.selecionar(new HashSet<>(mercadorias), mer_id);

		if (mercadoria == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		adicionadorLink.adicionarLink(mercadoria);

		return ResponseEntity.status(HttpStatus.OK).body(mercadoria);
	}

	@PreAuthorize("hasRole('ROLE_GERENTE')")
    @PostMapping("/cadastrar")
	public ResponseEntity<String> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (mercadoria.getId() == null) {
			mer_repo.save(mercadoria);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasRole('ROLE_GERENTE')")
    @PutMapping("/atualizar")
	public ResponseEntity<?> atualizarMercadoria(@RequestBody Mercadoria atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Mercadoria mercadoria = mer_repo.getReferenceById(atualizacao.getId());
		if (mercadoria != null) {
			MercadoriaAtualizador atualizador = new MercadoriaAtualizador();
			atualizador.atualizar(mercadoria, atualizacao);
			mer_repo.save(mercadoria);
			status = HttpStatus.NO_CONTENT;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasRole('ROLE_GERENTE')")
    @DeleteMapping("/excluir")
	public ResponseEntity<?> excluirUsuario(@RequestBody Mercadoria exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Mercadoria mercadoria = mer_repo.getReferenceById(exclusao.getId());
		if (mercadoria != null) {
			mer_repo.delete(mercadoria);
			status = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<>(status);
	}
}
