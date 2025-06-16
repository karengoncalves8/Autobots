package com.autobots.automanager.controles;

import java.util.ArrayList;
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

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelo.AdicionadorLinkCliente;
import com.autobots.automanager.modelo.AdicionadorLinkDocumento;
import com.autobots.automanager.modelo.AdicionadorLinkEndereco;
import com.autobots.automanager.modelo.AdicionadorLinkTelefone;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/cliente")
public class ClienteControle {
	
	@Autowired
	private ClienteRepositorio repositorio;
	@Autowired
	private ClienteSelecionador selecionador;
	@Autowired
	private AdicionadorLinkCliente adicionadorLink;
	@Autowired
	private AdicionadorLinkDocumento adicionadorLinkDoc;
	@Autowired
	private AdicionadorLinkTelefone adicionadorLinkTel;
	@Autowired
	private AdicionadorLinkEndereco adicionadorLinkEnd;

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> obterCliente(@PathVariable long id) {
		List<Cliente> clientes = repositorio.findAll();
		Cliente cliente = selecionador.selecionar(clientes, id);
		if (cliente == null) {
			ResponseEntity<Cliente> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(cliente);
			adicionadorLinkDoc.adicionarLinkList(cliente.getDocumentos());
			adicionadorLinkTel.adicionarLinkList( new ArrayList<>(cliente.getTelefones()));
			adicionadorLinkEnd.adicionarLinkObj(cliente.getEndereco());

			ResponseEntity<Cliente> resposta = new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
			return resposta;
		}
	}

	@GetMapping("/clientes")
	public ResponseEntity<List<Cliente>> obterClientes() {
		List<Cliente> clientes = repositorio.findAll();
		if (clientes.isEmpty()) {
			ResponseEntity<List<Cliente>> resposta = new ResponseEntity<>(HttpStatus.NO_CONTENT);
			return resposta;
		} else {
			adicionadorLink.adicionarLinkList(clientes);
			for(Cliente cliente : clientes){
				adicionadorLinkDoc.adicionarLinkList(cliente.getDocumentos());
				adicionadorLinkTel.adicionarLinkList(new ArrayList<>(cliente.getTelefones()));
				adicionadorLinkEnd.adicionarLinkObj(cliente.getEndereco());
			}
			
			ResponseEntity<List<Cliente>> resposta = new ResponseEntity<>(clientes, HttpStatus.OK);
			return resposta;
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (cliente.getId() == null) {
			repositorio.save(cliente);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarCliente(@RequestBody Cliente atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Cliente cliente = repositorio.getById(atualizacao.getId());
		if (cliente != null) {
			ClienteAtualizador atualizador = new ClienteAtualizador();
			atualizador.atualizar(cliente, atualizacao);
			repositorio.save(cliente);
			status = HttpStatus.NO_CONTENT;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirCliente(@RequestBody Cliente exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Cliente cliente = repositorio.getById(exclusao.getId());
		if (cliente != null) {
			repositorio.delete(cliente);
			status = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<>(status);
	}
}
