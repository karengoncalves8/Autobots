package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.venda.VendaAtualizador;
import com.autobots.automanager.modelos.venda.VendaAdicionadorLink;
import com.autobots.automanager.modelos.venda.VendaSelecionador;
import com.autobots.automanager.repositorios.VendaRepositorio;

@RestController
@RequestMapping("/venda")
public class VendaControle {
    @Autowired
	private VendaRepositorio repositorio;

    @Autowired
    private VendaSelecionador selecionador;
    
	@Autowired
	private VendaAdicionadorLink adicionadorLink;

    @GetMapping("/todos")
	public ResponseEntity<List<Venda>> obterTodasVendas() {
		List<Venda> vendas = repositorio.findAll();
		if (vendas.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		adicionadorLink.adicionarLinkList(vendas);
		return ResponseEntity.status(HttpStatus.OK).body(vendas);
	}
	
	@GetMapping("/{ven_id}")
	public ResponseEntity<Venda> obterVenda(@PathVariable Long ven_id) {
		List<Venda> vendas = repositorio.findAll();
		Venda venda = selecionador.selecionar(vendas, ven_id);

		if (venda == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		adicionadorLink.adicionarLink(venda);

		return ResponseEntity.status(HttpStatus.OK).body(venda);
	}

    @PostMapping("/cadastrar")
	public ResponseEntity<String> cadastrarVenda(@RequestBody Venda venda) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (venda.getId() == null) {
			repositorio.save(venda);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping("/atualizar")
	public ResponseEntity<?> atualizarMercadoria(@RequestBody Venda atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Venda venda = repositorio.getReferenceById(atualizacao.getId());
		if (venda != null) {
			VendaAtualizador atualizador = new VendaAtualizador();
			atualizador.atualizar(venda, atualizacao);
			repositorio.save(venda);
			status = HttpStatus.NO_CONTENT;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

    @DeleteMapping("/excluir")
	public ResponseEntity<?> excluirUsuario(@RequestBody Venda exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Venda venda = repositorio.getReferenceById(exclusao.getId());
		if (venda != null) {
			repositorio.delete(venda);
			status = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<>(status);
	}
}
