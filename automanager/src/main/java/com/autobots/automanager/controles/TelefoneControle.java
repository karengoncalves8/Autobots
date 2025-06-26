package com.autobots.automanager.controles;

import java.util.ArrayList;
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

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.telefone.TelefoneAdicionadorLink;
import com.autobots.automanager.modelos.telefone.TelefoneAtualizador;
import com.autobots.automanager.modelos.telefone.TelefoneSelecionador;
import com.autobots.automanager.modelos.usuario.UsuarioSelecionador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	@Autowired
	private UsuarioRepositorio usu_repo;

	@Autowired
	private UsuarioSelecionador usu_selecionador;

	@Autowired
	private TelefoneRepositorio tel_repo;

	@Autowired
	private TelefoneSelecionador tel_selecionador;

	@Autowired
	private TelefoneAdicionadorLink adicionadorLink;

	@GetMapping("/todos")
	public ResponseEntity<List<Telefone>> obterTodosTelefones() {
		List<Telefone> telefones = tel_repo.findAll();
		if (telefones.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		adicionadorLink.adicionarLinkList(telefones);
		return ResponseEntity.status(HttpStatus.OK).body(telefones);
	}
	
	@GetMapping("/usuario/{cli_id}")
	public ResponseEntity<Set<Telefone>> obterTodosTelefonesUsuario(@PathVariable Long cli_id) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, cli_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		Set<Telefone> telefones = usuario.getTelefones();
		List<Telefone> telefones_list = new ArrayList<>(telefones);
		adicionadorLink.adicionarLinkList(telefones_list);

		return ResponseEntity.status(HttpStatus.OK).body(telefones);
	}

	@GetMapping("/{tel_id}")
	public ResponseEntity<Telefone> obterTelefonePorId(@PathVariable Long tel_id) {
		List<Telefone> listaTelefones = tel_repo.findAll();
		Set<Telefone> telefones = new HashSet<>(listaTelefones);
		Telefone telefone = tel_selecionador.selecionar(telefones, tel_id);

		if(telefone == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		adicionadorLink.adicionarLink(telefone);

		return ResponseEntity.status(HttpStatus.OK).body(telefone);
	}
	

	@PostMapping("/{cli_id}/cadastrar")
	public ResponseEntity<String> cadastrarUsuarioTelefone(@PathVariable Long cli_id, @RequestBody Telefone telefone) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, cli_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		}

		usuario.getTelefones().add(telefone);
		tel_repo.save(telefone);
		
		return ResponseEntity.status(HttpStatus.OK).body("Telefone cadastrado com sucesso!");
	}
	

	@PutMapping("/atualizar/{cli_id}")
	public ResponseEntity<String> atualizarUsuarioTelefone(@PathVariable Long cli_id, @RequestBody Telefone att_telefone) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, cli_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		}
		
		Set<Telefone> telefones = usuario.getTelefones();
		Telefone telefone = tel_selecionador.selecionar(telefones, att_telefone.getId());
		
		if(telefone == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Telefone não encontrado!");
		}

		TelefoneAtualizador tel_atualizador = new TelefoneAtualizador();

		tel_atualizador.atualizar(telefone, att_telefone);
		tel_repo.save(telefone);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Telefone atualizado com sucesso!");
	}

	@DeleteMapping("/excluir/{cli_id}")
	public ResponseEntity<String> deletarUsuarioTelefone(@PathVariable Long cli_id, @RequestBody Telefone tel_excluido) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, cli_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		}
		
		Set<Telefone> telefones = usuario.getTelefones();
		Telefone telefone = tel_selecionador.selecionar(telefones, tel_excluido.getId());

		if(telefone == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Telefone não encontrado");
		}
		
		usuario.getTelefones().remove(telefone);
		tel_repo.delete(telefone);

		return ResponseEntity.status(HttpStatus.OK).body("Telefone excluído com sucesso!");
	}
	
}
