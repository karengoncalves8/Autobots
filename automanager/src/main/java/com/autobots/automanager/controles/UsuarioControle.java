package com.autobots.automanager.controles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.CredencialSenha;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.enums.PerfilUsuario;
import com.autobots.automanager.modelos.documento.DocumentoAdicionadorLink;
import com.autobots.automanager.modelos.endereco.EnderecoAdicionadorLink;
import com.autobots.automanager.modelos.telefone.TelefoneAdicionadorLink;
import com.autobots.automanager.modelos.usuario.UsuarioAdicionadorLink;
import com.autobots.automanager.modelos.usuario.UsuarioAtualizador;
import com.autobots.automanager.modelos.usuario.UsuarioSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.modelos.VerificadorPerfil;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
	private UsuarioRepositorio repositorio;
	@Autowired
	private UsuarioSelecionador selecionador;
	@Autowired
	private UsuarioAdicionadorLink adicionadorLink;
	@Autowired
	private DocumentoAdicionadorLink adicionadorLinkDoc;
	@Autowired
	private TelefoneAdicionadorLink adicionadorLinkTel;
	@Autowired
	private EnderecoAdicionadorLink adicionadorLinkEnd;
	@Autowired
    private PasswordEncoder passwordEncoder;

	@PreAuthorize("hasRole('ROLE_CLIENTE')")
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
		List<Usuario> usuarios = repositorio.findAll();
		Usuario usuario = selecionador.selecionar(usuarios, id);
		if (usuario == null) {
			ResponseEntity<Usuario> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			// Impedindo que clientes acessem infos que não sejam as suas
			Usuario user_req = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VerificadorPerfil verificadorPerfil = new VerificadorPerfil();
			if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.CLIENTE) && !user_req.getId().equals(id)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			adicionadorLink.adicionarLink(usuario);
			adicionadorLinkDoc.adicionarLinkList(new ArrayList<>(usuario.getDocumentos()));
			adicionadorLinkTel.adicionarLinkList(new ArrayList<>(usuario.getTelefones()));
			adicionadorLinkEnd.adicionarLinkObj(usuario.getEndereco());

			ResponseEntity<Usuario> resposta = new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
			return resposta;
		}
	}

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> obterusuarios() {
		List<Usuario> usuarios_all = repositorio.findAll();
		if (usuarios_all.isEmpty()) {
			ResponseEntity<List<Usuario>> resposta = new ResponseEntity<>(HttpStatus.NO_CONTENT);
			return resposta;
		} else {
			List<Usuario> usuarios_return = new ArrayList<Usuario>();

			// Impedindo que clientes acessem infos que não sejam as suas
			Usuario user_req = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VerificadorPerfil verificadorPerfil = new VerificadorPerfil();
			if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.VENDEDOR)){
				usuarios_return = usuarios_all.stream()
								.filter(usuario -> verificadorPerfil.hasPerfil(usuario, PerfilUsuario.CLIENTE))
								.collect(Collectors.toList());
			}else if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.GERENTE)){
				usuarios_return = usuarios_all.stream()
								.filter(usuario -> !verificadorPerfil.hasPerfil(usuario, PerfilUsuario.ADMIN))
								.collect(Collectors.toList());
			}else{
				usuarios_return = usuarios_all;
			}

			adicionadorLink.adicionarLinkList(usuarios_return);
			for(Usuario usuario : usuarios_return){
				adicionadorLinkDoc.adicionarLinkList(new ArrayList<>(usuario.getDocumentos()));
				adicionadorLinkTel.adicionarLinkList(new ArrayList<>(usuario.getTelefones()));
				adicionadorLinkEnd.adicionarLinkObj(usuario.getEndereco());
			}
			
			ResponseEntity<List<Usuario>> resposta = new ResponseEntity<>(usuarios_return, HttpStatus.OK);
			return resposta;
		}
	}

	@PreAuthorize("hasRole('ROLE_VENDEDOR')")
	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
		try {
			HttpStatus status = HttpStatus.BAD_REQUEST;
			if (usuario.getId() == null) {

				Usuario user_req = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				VerificadorPerfil verificadorPerfil = new VerificadorPerfil();

				if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.VENDEDOR) && !verificadorPerfil.hasPerfil(usuario, PerfilUsuario.CLIENTE)){
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				} 
				else if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.GERENTE) && verificadorPerfil.hasPerfil(usuario, PerfilUsuario.ADMIN)){
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}

				if (usuario.getCredenciais() != null && !usuario.getCredenciais().isEmpty()) {
					usuario.getCredenciais().forEach(credencial -> {
						if (credencial instanceof CredencialSenha) {
							CredencialSenha credencialSenha = (CredencialSenha) credencial;
							// Criptografar a senha com BCrypt
							String senhaCriptografada = passwordEncoder.encode(credencialSenha.getSenha());
							credencialSenha.setSenha(senhaCriptografada);
						}
					});

					// Salvar no banco de dados
					repositorio.save(usuario);
					status = HttpStatus.CREATED;		
				} else {
					return new ResponseEntity<>("Usuário deve ter pelo menos uma credencial", HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<>("ID não deve ser fornecido para cadastro", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(status);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Usuario usuario = repositorio.getReferenceById(atualizacao.getId());
		if (usuario != null) {
			Usuario user_req = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VerificadorPerfil verificadorPerfil = new VerificadorPerfil();

			if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.VENDEDOR) && !verificadorPerfil.hasPerfil(usuario, PerfilUsuario.CLIENTE)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			} 
			else if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.GERENTE) && verificadorPerfil.hasPerfil(usuario, PerfilUsuario.ADMIN)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			UsuarioAtualizador atualizador = new UsuarioAtualizador();
			atualizador.atualizar(usuario, atualizacao);
			repositorio.save(usuario);
			status = HttpStatus.NO_CONTENT;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirUsuario(@RequestBody Usuario exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Usuario usuario = repositorio.getReferenceById(exclusao.getId());
		if (usuario != null) {
			Usuario user_req = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VerificadorPerfil verificadorPerfil = new VerificadorPerfil();

			if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.VENDEDOR) && !verificadorPerfil.hasPerfil(usuario, PerfilUsuario.CLIENTE)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			} 
			else if(verificadorPerfil.hasPerfil(user_req, PerfilUsuario.GERENTE) && verificadorPerfil.hasPerfil(usuario, PerfilUsuario.ADMIN)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			
			repositorio.delete(usuario);
			status = HttpStatus.NO_CONTENT;
		}
		return new ResponseEntity<>(status);
	}
}
