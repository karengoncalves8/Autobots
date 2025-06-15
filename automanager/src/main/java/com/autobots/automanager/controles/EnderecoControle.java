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

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.AdicionadorLinkEndereco;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.modelo.EnderecoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;


@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	@Autowired
	private ClienteRepositorio cli_repo;

	@Autowired
	private ClienteSelecionador cli_selecionador;

	@Autowired
	private EnderecoRepositorio end_repo;

	@Autowired
	private EnderecoSelecionador end_selecionador;

	@Autowired
	private AdicionadorLinkEndereco adicionadorLink;

	@GetMapping("/todos")
	public ResponseEntity<List<Endereco>> obterTodosEnderecos() {
		List<Endereco> enderecos = end_repo.findAll();
		if (enderecos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		adicionadorLink.adicionarLinkList(enderecos);
		return ResponseEntity.status(HttpStatus.OK).body(enderecos);
	}
	
	@GetMapping("/cliente/{cli_id}")
	public ResponseEntity<Endereco> obterClienteEndereco(@PathVariable Long cli_id) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		Endereco endereco = cliente.getEndereco();
		adicionadorLink.adicionarLink(endereco);

		return ResponseEntity.status(HttpStatus.OK).body(endereco);
	}

	@GetMapping("/{end_id}")
	public ResponseEntity<?> obterEnderecoPorId(@PathVariable Long end_id) {
		List<Endereco> enderecos = end_repo.findAll();
		Endereco endereco = end_selecionador.selecionar(enderecos, end_id);

		if (endereco == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");
		}

		adicionadorLink.adicionarLink(endereco);

		return ResponseEntity.status(HttpStatus.OK).body(endereco);
	}

	@PostMapping("/{cli_id}/cadastrar")
	public ResponseEntity<String> cadastrarClienteEndereco(@PathVariable Long cli_id, @RequestBody Endereco endereco) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
		}

		cliente.setEndereco(endereco);
		end_repo.save(endereco);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Endereço cadastrado com sucesso!");
	}
	

	@PutMapping("/atualizar/{cli_id}")
	public ResponseEntity<String> atualizarClienteEndereco(@PathVariable Long cli_id, @RequestBody Endereco att_endereco) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
		}
		
		Endereco endereco = cliente.getEndereco();
		
		EnderecoAtualizador end_atualizador = new EnderecoAtualizador();

		end_atualizador.atualizar(endereco, att_endereco);
		end_repo.save(endereco);

		return ResponseEntity.status(HttpStatus.OK).body("Endereço atualizado com sucesso!");
	}

	@DeleteMapping("/excluir/{cli_id}")
	public ResponseEntity<String> deletarClienteEndereco(@PathVariable Long cli_id) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
		}
		
		Endereco endereco = cliente.getEndereco();
		
		cliente.setEndereco(null);
		end_repo.delete(endereco);

		return ResponseEntity.status(HttpStatus.OK).body("Endereço excluído com sucesso!");
	}
	
}
