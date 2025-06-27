package com.autobots.automanager.controles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.documento.DocumentoAdicionadorLink;
import com.autobots.automanager.modelos.documento.DocumentoAtualizador;
import com.autobots.automanager.modelos.documento.DocumentoSelecionador;
import com.autobots.automanager.modelos.usuario.UsuarioSelecionador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	@Autowired
	private UsuarioRepositorio usu_repo;

	@Autowired
	private UsuarioSelecionador usu_selecionador;

	@Autowired
	private DocumentoRepositorio doc_repo;

	@Autowired
	private DocumentoSelecionador doc_selecionador;

	@Autowired
	private DocumentoAdicionadorLink adicionadorLink;

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@GetMapping("/todos")
	public ResponseEntity<List<Documento>> obterTodosDocumetnos() {
		List<Documento> documentos = doc_repo.findAll();
		if (documentos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		adicionadorLink.adicionarLinkList(documentos);
		return ResponseEntity.status(HttpStatus.OK).body(documentos);
	}						
	
	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@GetMapping("/usuario/{usu_id}")
	public ResponseEntity<Set<Documento>> obterTodosDocumentosUsuario(@PathVariable Long usu_id) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, usu_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		Set<Documento> documentos = usuario.getDocumentos();
		adicionadorLink.adicionarLinkList(new ArrayList<>(documentos));

		return ResponseEntity.status(HttpStatus.OK).body(documentos);
	}

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@GetMapping("/{doc_id}")
	public ResponseEntity<Documento> obterDocumentoPorId(@PathVariable Long doc_id) {
		List<Documento> documentos = doc_repo.findAll();
		Documento documento = doc_selecionador.selecionar(documentos, doc_id);

		if(documento == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		adicionadorLink.adicionarLink(documento);

		return ResponseEntity.status(HttpStatus.OK).body(documento);
	}

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@PostMapping("/{usu_id}/cadastrar")
	public ResponseEntity<String> cadastrarUsuarioDocumento(@PathVariable Long usu_id, @RequestBody Documento documento) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, usu_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
		}

		usuario.getDocumentos().add(documento);
		doc_repo.save(documento);
		
		return ResponseEntity.status(HttpStatus.OK).body("Documento cadastrado com sucesso!");
	}
	
	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@PutMapping("/atualizar/{usu_id}")
	public ResponseEntity<String> atualizarUsuarioDocumento(@PathVariable Long usu_id, @RequestBody Documento att_documento) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, usu_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		}
		
		Set<Documento> documentos = usuario.getDocumentos();
		Documento documento = doc_selecionador.selecionar(new ArrayList<>(documentos), att_documento.getId());

		if(documento == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
		}
		
		DocumentoAtualizador doc_atualizador = new DocumentoAtualizador();

		doc_atualizador.atualizar(documento, att_documento);
		doc_repo.save(documento);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Documento atualizado com sucesso!");
	}

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@DeleteMapping("/excluir/{usu_id}")
	public ResponseEntity<String> deletarUsuarioDocumento(@PathVariable Long usu_id, @RequestBody Documento doc_excluido) {
		List<Usuario> usuarios = usu_repo.findAll();
		Usuario usuario = usu_selecionador.selecionar(usuarios, usu_id);

		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
		}
		
		Set<Documento> documentos = usuario.getDocumentos();
		Documento documento = doc_selecionador.selecionar(new ArrayList<>(documentos), doc_excluido.getId());

		if(documento == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado");
		}
		
		usuario.getDocumentos().remove(documento);
		doc_repo.delete(documento);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Documento excluído com sucesso!");
	}
	
}
