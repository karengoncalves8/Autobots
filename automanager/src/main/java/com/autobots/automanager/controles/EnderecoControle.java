package com.autobots.automanager.controles;

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

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.endereco.EnderecoAdicionadorLink;
import com.autobots.automanager.modelos.endereco.EnderecoAtualizador;
import com.autobots.automanager.modelos.endereco.EnderecoSelecionador;
import com.autobots.automanager.modelos.usuario.UsuarioSelecionador;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	@Autowired
	private UsuarioRepositorio usu_repo;

	@Autowired
	private UsuarioSelecionador usu_selecionador;

	@Autowired
	private EnderecoRepositorio end_repo;

	@Autowired
	private EnderecoSelecionador end_selecionador;

	@Autowired
	private EnderecoAdicionadorLink adicionadorLink;

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@GetMapping("/todos")
	public ResponseEntity<List<Endereco>> obterTodosEnderecos() {
		List<Endereco> enderecos = end_repo.findAll();
		if (enderecos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		adicionadorLink.adicionarLinkList(enderecos);
		return ResponseEntity.status(HttpStatus.OK).body(enderecos);
	}
	
	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@GetMapping("/usuario/{usu_id}")
	public ResponseEntity<Endereco> obterUsuarioEndereco(@PathVariable Long usu_id) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, usu_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		Endereco endereco = usuario.getEndereco();
		adicionadorLink.adicionarLink(endereco);

		return ResponseEntity.status(HttpStatus.OK).body(endereco);
	}

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
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

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@PostMapping("/{usu_id}/cadastrar")
	public ResponseEntity<String> cadastrarUsuarioEndereco(@PathVariable Long usu_id, @RequestBody Endereco endereco) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, usu_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		}

		usuario.setEndereco(endereco);
		end_repo.save(endereco);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Endereço cadastrado com sucesso!");
	}
	
	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@PutMapping("/atualizar/{usu_id}")
	public ResponseEntity<String> atualizarUsuarioEndereco(@PathVariable Long usu_id, @RequestBody Endereco att_endereco) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, usu_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		}
		
		Endereco endereco = usuario.getEndereco();
		
		EnderecoAtualizador end_atualizador = new EnderecoAtualizador();

		end_atualizador.atualizar(endereco, att_endereco);
		end_repo.save(endereco);

		return ResponseEntity.status(HttpStatus.OK).body("Endereço atualizado com sucesso!");
	}

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@DeleteMapping("/excluir/{usu_id}")
	public ResponseEntity<String> deletarUsuarioEndereco(@PathVariable Long usu_id) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, usu_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		}
		
		Endereco endereco = usuario.getEndereco();
		
		usuario.setEndereco(null);
		end_repo.delete(endereco);

		return ResponseEntity.status(HttpStatus.OK).body("Endereço excluído com sucesso!");
	}
	
}