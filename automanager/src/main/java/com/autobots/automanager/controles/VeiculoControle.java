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

import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.veiculo.VeiculoAdicionadorLink;
import com.autobots.automanager.modelos.veiculo.VeiculoAtualizador;
import com.autobots.automanager.modelos.veiculo.VeiculoSelecionador;
import com.autobots.automanager.repositorios.VeiculoRepositorio;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {
@Autowired
	private VeiculoRepositorio repositorio;

    @Autowired
    private VeiculoSelecionador selecionador;
    
	@Autowired
	private VeiculoAdicionadorLink adicionadorLink;

    @GetMapping("/todos")
	public ResponseEntity<List<Veiculo>> obterTodasVendas() {
		List<Veiculo> veiculos = repositorio.findAll();
		if (veiculos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		adicionadorLink.adicionarLinkList(veiculos);
		return ResponseEntity.status(HttpStatus.OK).body(veiculos);
	}
	
	@GetMapping("/{ven_id}")
	public ResponseEntity<Veiculo> obterVenda(@PathVariable Long ven_id) {
		List<Veiculo> veiculos = repositorio.findAll();
		Veiculo venda = selecionador.selecionar(veiculos, ven_id);

		if (venda == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		adicionadorLink.adicionarLink(venda);

		return ResponseEntity.status(HttpStatus.OK).body(venda);
	}

    @PostMapping("/cadastrar")
	public ResponseEntity<String> cadastrarVenda(@RequestBody Veiculo venda) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (venda.getId() == null) {
			repositorio.save(venda);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

    @PutMapping("/atualizar")
	public ResponseEntity<?> atualizarVeiuclo(@RequestBody Veiculo atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Veiculo venda = repositorio.getReferenceById(atualizacao.getId());
		if (venda != null) {
			VeiculoAtualizador atualizador = new VeiculoAtualizador();
			atualizador.atualizar(venda, atualizacao);
			repositorio.save(venda);
			status = HttpStatus.NO_CONTENT;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

    @DeleteMapping("/excluir")
	public ResponseEntity<?> excluirVeiculo(@RequestBody Veiculo exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Veiculo venda = repositorio.getReferenceById(exclusao.getId());
		if (venda != null) {
			repositorio.delete(venda);
			status = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<>(status);
	}
}
