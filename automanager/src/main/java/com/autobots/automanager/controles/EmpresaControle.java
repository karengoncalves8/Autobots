package com.autobots.automanager.controles;

import java.util.Date;
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

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.modelos.empresa.EmpresaAdicionadorLink;
import com.autobots.automanager.modelos.empresa.EmpresaAtualizador;
import com.autobots.automanager.modelos.empresa.EmpresaSelecionador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {

	@Autowired
	private EmpresaRepositorio repositorio;

	@Autowired
	private EmpresaSelecionador selecionador;

	@Autowired
	private EmpresaAdicionadorLink adicionadorLink;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
		List<Empresa> empresas = repositorio.findAll();
		Empresa empresa = selecionador.selecionar(new HashSet<>(empresas), id);
		if (empresa == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(empresa);
			return new ResponseEntity<Empresa>(empresa, HttpStatus.OK);
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/todos")
	public ResponseEntity<List<Empresa>> obterEmpresas() {
		List<Empresa> empresas = repositorio.findAll();
		if (empresas.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			adicionadorLink.adicionarLinkList(empresas);
			return new ResponseEntity<>(empresas, HttpStatus.OK);
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/cadastrar")
	public ResponseEntity<?> cadastrarEmpresa(@RequestBody Empresa empresa) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (empresa.getId() == null) {
			empresa.setCadastro(new Date());
			repositorio.save(empresa);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/atualizar/{emp_id}")
	public ResponseEntity<?> atualizarEmpresa(@PathVariable Long emp_id, @RequestBody Empresa atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Empresa empresa = repositorio.findById(atualizacao.getId()).orElse(null);
		if (empresa != null) {
			EmpresaAtualizador atualizador = new EmpresaAtualizador();
			atualizador.atualizar(empresa, atualizacao);
			repositorio.save(empresa);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirEmpresa(@RequestBody Empresa exclusao) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		Empresa empresa = repositorio.findById(exclusao.getId()).orElse(null);
		if (empresa != null) {
			repositorio.delete(empresa);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}
