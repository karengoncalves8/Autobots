package com.autobots.automanager.controles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.TelefoneSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;


@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	@Autowired
	private ClienteRepositorio cli_repo;

	@Autowired
	private ClienteSelecionador cli_selecionador;

	@Autowired
	private TelefoneRepositorio tel_repo;

	@Autowired
	private TelefoneSelecionador tel_selecionador;

	@GetMapping("/todos")
	public ResponseEntity<List<Telefone>> obterTodosTelefones() {
		List<Telefone> telefones = tel_repo.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(telefones);
	}
	
	@GetMapping("/cliente/{cli_id}")
	public ResponseEntity<Set<Telefone>> obterTodosTelefonesCliente(@PathVariable Long cli_id) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		Set<Telefone> telefones = cliente.getTelefones();

		return ResponseEntity.status(HttpStatus.OK).body(telefones);
	}

	@GetMapping("/{tel_id}")
	public ResponseEntity<Telefone> obterTelefonePorId(@PathVariable Long tel_id) {
		List<Telefone> listaTelefones = tel_repo.findAll();
		Set<Telefone> telefones = new HashSet<>(listaTelefones);
		Telefone telefone = tel_selecionador.selecionar(telefones, tel_id);

		return ResponseEntity.status(HttpStatus.OK).body(telefone);
	}
	

	@PostMapping("/{cli_id}/cadastrar")
	public ResponseEntity<String> cadastrarClienteTelefone(@PathVariable Long cli_id, @RequestBody Telefone telefone) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
		}

		cliente.getTelefones().add(telefone);
		tel_repo.save(telefone);
		
		return ResponseEntity.status(HttpStatus.OK).body("Telefone cadastrado com sucesso!");
	}
	

	@PutMapping("/atualizar/{cli_id}")
	public ResponseEntity<String> atualizarClienteTelefone(@PathVariable Long cli_id, @RequestBody Telefone att_telefone) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
		}
		
		Set<Telefone> telefones = cliente.getTelefones();
		Telefone telefone = tel_selecionador.selecionar(telefones, att_telefone.getId());
		
		TelefoneAtualizador tel_atualizador = new TelefoneAtualizador();

		tel_atualizador.atualizar(telefone, att_telefone);
		tel_repo.save(telefone);

		return ResponseEntity.status(HttpStatus.OK).body("Telefone atualizado com sucesso!");
	}

	@DeleteMapping("/excluir/{cli_id}")
	public ResponseEntity<String> deletarClienteTelefone(@PathVariable Long cli_id, @RequestBody Telefone tel_excluido) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
		}
		
		Set<Telefone> telefones = cliente.getTelefones();
		Telefone telefone = tel_selecionador.selecionar(telefones, tel_excluido.getId());

		if(telefone == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Telefone não encontrado");
		}
		
		cliente.getTelefones().remove(telefone);
		tel_repo.delete(telefone);

		return ResponseEntity.status(HttpStatus.OK).body("Telefone excluído com sucesso!");
	}
	
}
