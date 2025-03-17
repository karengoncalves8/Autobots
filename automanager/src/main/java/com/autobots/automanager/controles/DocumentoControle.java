package com.autobots.automanager.controles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	@Autowired
	private ClienteRepositorio cli_repo;

	@Autowired
	private ClienteSelecionador cli_selecionador;

	@Autowired
	private DocumentoRepositorio doc_repo;

	@Autowired
	private DocumentoSelecionador doc_selecionador;

	@GetMapping("/cliente/{cli_id}")
	public ResponseEntity<Set<Documento>> obterTodosDocumentosCliente(@PathVariable Long cli_id) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		Set<Documento> documentos = cliente.getDocumentos();

		return ResponseEntity.status(HttpStatus.OK).body(documentos);
	}

	@GetMapping("/cliente/{cli_id}/{doc_id}")
	public ResponseEntity<?> obterDocumentoCliente(@PathVariable Long cli_id, @PathVariable Long doc_id) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			Map<String, String> response = new HashMap<>();
        	response.put("message", "Cliente não encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		Set<Documento> documentos = cliente.getDocumentos();

		Documento documento = doc_selecionador.selecionar(documentos, doc_id);

		return ResponseEntity.status(HttpStatus.OK).body(documento);
	}

	@PostMapping("/{cli_id}/cadastrar")
	public ResponseEntity<String> cadastrarClienteDocumento(@PathVariable Long cli_id, @RequestBody Documento documento) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
		}

		documento.setCliente(cliente);

		doc_repo.save(documento);
		
		return ResponseEntity.status(HttpStatus.OK).body("Documento cadastrado com sucesso!");
	}
	

	@PutMapping("/atualizar/{cli_id}")
	public ResponseEntity<String> atualizarClienteDocumento(@PathVariable Long cli_id, @RequestBody Documento att_documento) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
		}
		
		Set<Documento> documentos = cliente.getDocumentos();
		Documento documento = doc_selecionador.selecionar(documentos, att_documento.getId());
		
		DocumentoAtualizador doc_atualizador = new DocumentoAtualizador();

		doc_atualizador.atualizar(documento, att_documento);
		doc_repo.save(documento);

		return ResponseEntity.status(HttpStatus.OK).body("Documento atualizado com sucesso!");
	}

	@DeleteMapping("/excluir/{cli_id}")
	public ResponseEntity<String> deletarClienteDocumento(@PathVariable Long cli_id, @RequestBody Documento doc_excluido) {
		List<Cliente> clientes = cli_repo.findAll();
		Cliente cliente = cli_selecionador.selecionar(clientes, cli_id);

		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
		}
		
		Set<Documento> documentos = cliente.getDocumentos();
		Documento documento = doc_selecionador.selecionar(documentos, doc_excluido.getId());

		if(documento == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado");
		}

		cliente.getDocumentos().remove(documento);
    
		cli_repo.save(cliente);
		
		doc_repo.delete(documento);

		return ResponseEntity.status(HttpStatus.OK).body("Documento excluído com sucesso!");
	}
	
}
