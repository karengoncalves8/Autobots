package com.autobots.automanager.controles;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.enums.PerfilUsuario;
import com.autobots.automanager.modelos.venda.VendaAtualizador;
import com.autobots.automanager.modelos.VerificadorPerfil;
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

	@PreAuthorize("hasRole('ROLE_CLIENTE')")
    @GetMapping("/todos")
	public ResponseEntity<List<Venda>> obterTodasVendas() {
		List<Venda> vendas_all = repositorio.findAll();
		if (vendas_all.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		List<Venda> vendas_return = new ArrayList<Venda>();

		// Impedindo que clientes acessem infos que não sejam as suas
		Usuario user_req = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VerificadorPerfil verificadorPerfil = new VerificadorPerfil();
		if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.CLIENTE)){
			for(Venda venda : vendas_all){
				if(user_req.getId().equals(venda.getCliente().getId())){
					vendas_return.add(venda);
				}
			} 
		}else if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.VENDEDOR)){
			for(Venda venda : vendas_all){
				if(user_req.getId().equals(venda.getFuncionario().getId())){
					vendas_return.add(venda);
				}
			} 
		}
		else{
			vendas_return = vendas_all;
		}

		adicionadorLink.adicionarLinkList(vendas_return);
		return ResponseEntity.status(HttpStatus.OK).body(vendas_return);
	}
	
	@PreAuthorize("hasRole('ROLE_CLIENTE')")
	@GetMapping("/{ven_id}")
	public ResponseEntity<Venda> obterVenda(@PathVariable Long ven_id) {
		List<Venda> vendas = repositorio.findAll();
		Venda venda = selecionador.selecionar(vendas, ven_id);

		if (venda == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		Usuario user_req = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VerificadorPerfil verificadorPerfil = new VerificadorPerfil();
		if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.CLIENTE) && !user_req.getId().equals(venda.getCliente().getId())){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}else if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.VENDEDOR) && !venda.getFuncionario().getId().equals(user_req.getId())){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		adicionadorLink.adicionarLink(venda);

		return ResponseEntity.status(HttpStatus.OK).body(venda);
	}

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
    @PostMapping("/cadastrar")
	public ResponseEntity<String> cadastrarVenda(@RequestBody Venda venda) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (venda.getId() == null) {
			Usuario user_req = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VerificadorPerfil verificadorPerfil = new VerificadorPerfil();
			if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.VENDEDOR) && !venda.getFuncionario().getId().equals(user_req.getId())){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			repositorio.save(venda);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
    @PutMapping("/atualizar")
	public ResponseEntity<?> atualizarMercadoria(@RequestBody Venda atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Venda venda = repositorio.getReferenceById(atualizacao.getId());
		if (venda != null) {
			Usuario user_req = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VerificadorPerfil verificadorPerfil = new VerificadorPerfil();
			if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.VENDEDOR) && !venda.getFuncionario().getId().equals(user_req.getId())){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			VendaAtualizador atualizador = new VendaAtualizador();
			atualizador.atualizar(venda, atualizacao);
			repositorio.save(venda);
			status = HttpStatus.NO_CONTENT;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
    @DeleteMapping("/excluir")
	public ResponseEntity<?> excluirUsuario(@RequestBody Venda exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Venda venda = repositorio.getReferenceById(exclusao.getId());
		if (venda != null) {
			Usuario user_req = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VerificadorPerfil verificadorPerfil = new VerificadorPerfil();
			if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.VENDEDOR) && !venda.getFuncionario().getId().equals(user_req.getId())){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			repositorio.delete(venda);
			status = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<>(status);
	}
}
