package com.autobots.automanager;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialSenha;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.enums.PerfilUsuario;
import com.autobots.automanager.enums.TipoDocumento;
import com.autobots.automanager.enums.TipoVeiculo;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@SpringBootApplication
public class AutomanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerApplication.class, args);
	}

	@Component
	public static class Runner implements ApplicationRunner {
		@Autowired
		public UsuarioRepositorio repositorio;

		@Autowired
		private EmpresaRepositorio empresaRepositorio;

		@Autowired
		private UsuarioRepositorio usuarioRepositorio;

		@Autowired
		private MercadoriaRepositorio mercadoriaRepositorio;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

			// Empresa
			Empresa empresa1 = new Empresa();
			empresa1.setRazaoSocial("Auto Center FATEC");
			empresa1.setNomeFantasia("FatecCar");
			empresa1.setCadastro(new Date());

			Endereco enderecoEmpresa = new Endereco();
			enderecoEmpresa.setEstado("SP");
			enderecoEmpresa.setCidade("São Paulo");
			enderecoEmpresa.setBairro("Centro");
			enderecoEmpresa.setRua("Rua das Oficinas");
			enderecoEmpresa.setNumero("100");
			enderecoEmpresa.setCodigoPostal("01000-000");
			enderecoEmpresa.setInformacoesAdicionais("Próximo ao metrô");
			empresa1.setEndereco(enderecoEmpresa);

			Telefone telEmpresa = new Telefone();
			telEmpresa.setDdd("11");
			telEmpresa.setNumero("40028922");
			HashSet<Telefone> telsEmpresa = new HashSet<>();
			telsEmpresa.add(telEmpresa);
			empresa1.setTelefones(telsEmpresa);

			// Cliente
			Usuario cliente = new Usuario();
			cliente.setNome("João Cliente");
			cliente.setNomeSocial("Joãozinho");
			cliente.getPerfis().add(PerfilUsuario.CLIENTE);

			Telefone telCliente = new Telefone();
			telCliente.setDdd("11");
			telCliente.setNumero("999999999");
			cliente.getTelefones().add(telCliente);

			Endereco endCliente = new Endereco();
			endCliente.setEstado("SP");
			endCliente.setCidade("São Paulo");
			endCliente.setBairro("Centro");
			endCliente.setRua("Rua do Cliente");
			endCliente.setNumero("123");
			endCliente.setCodigoPostal("01001-000");
			endCliente.setInformacoesAdicionais("Apto 1");
			cliente.setEndereco(endCliente);

			Documento docCliente = new Documento();
			docCliente.setTipo(com.autobots.automanager.enums.TipoDocumento.CPF);
			docCliente.setNumero("123.456.789-00");
			docCliente.setDataEmissao(new Date());
			cliente.getDocumentos().add(docCliente);

			Email emailCliente = new Email();
			emailCliente.setEndereco("cliente@fatec.com");
			cliente.getEmails().add(emailCliente);

			CredencialSenha credencialCliente = new CredencialSenha();
			credencialCliente.setNomeUsuario("cliente");
			credencialCliente.setSenha(codificador.encode("1234"));
			credencialCliente.setCriacao(new Date());
			credencialCliente.setInativo(false);
			cliente.getCredenciais().add(credencialCliente);

			// Funcionário
			Usuario funcionario = new Usuario();
			funcionario.setNome("Maria Funcionária");
			funcionario.setNomeSocial("Mariazinha");
			funcionario.getPerfis().add(PerfilUsuario.VENDEDOR);

			Telefone telFunc = new Telefone();
			telFunc.setDdd("11");
			telFunc.setNumero("988888888");
			funcionario.getTelefones().add(telFunc);

			Endereco endFunc = new Endereco();
			endFunc.setEstado("SP");
			endFunc.setCidade("São Paulo");
			endFunc.setBairro("Centro");
			endFunc.setRua("Rua do Func");
			endFunc.setNumero("321");
			endFunc.setCodigoPostal("01002-000");
			endFunc.setInformacoesAdicionais("Apto 2");
			funcionario.setEndereco(endFunc);

			Documento docFunc = new Documento();
			docFunc.setTipo(com.autobots.automanager.enums.TipoDocumento.RG);
			docFunc.setNumero("12.345.678-9");
			docFunc.setDataEmissao(new Date());
			funcionario.getDocumentos().add(docFunc);

			Email emailFunc = new Email();
			emailFunc.setEndereco("funcionario@fatec.com");
			funcionario.getEmails().add(emailFunc);

			CredencialSenha credencialFunc = new CredencialSenha();
			credencialFunc.setNomeUsuario("funcionario");
			credencialFunc.setSenha(codificador.encode("1234"));
			credencialFunc.setCriacao(new Date());
			credencialFunc.setInativo(false);
			funcionario.getCredenciais().add(credencialFunc);

			// Veículo
			Veiculo veiculo = new Veiculo();
			veiculo.setTipo(com.autobots.automanager.enums.TipoVeiculo.SEDA);
			veiculo.setModelo("Gol");
			veiculo.setPlaca("ABC-1234");
			veiculo.setProprietario(cliente);
			cliente.getVeiculos().add(veiculo);

			// Mercadoria
			Mercadoria mercadoria = new Mercadoria();
			mercadoria.setNome("Óleo de Motor");
			mercadoria.setDescricao("Óleo sintético 5W30");
			mercadoria.setValor(50.0);
			mercadoria.setQuantidade(10);
			mercadoria.setCadastro(new Date());
			mercadoria.setFabricao(new Date());
			mercadoria.setValidade(new Date(System.currentTimeMillis() + 1000L*60*60*24*365));
			empresa1.getMercadorias().add(mercadoria);

			// Serviço
			Servico servico = new Servico();
			servico.setNome("Troca de Óleo");
			servico.setDescricao("Troca completa de óleo de motor");
			servico.setValor(120.0);
			empresa1.getServicos().add(servico);

			// Venda
			Venda venda = new Venda();
			venda.setCadastro(new Date());
			venda.setIdentificacao("VENDA-001");
			venda.setCliente(cliente);
			venda.setFuncionario(funcionario);
			venda.getMercadorias().add(mercadoria);
			venda.getServicos().add(servico);
			venda.setVeiculo(veiculo);
			veiculo.getVendas().add(venda);
			empresa1.getVendas().add(venda);

			// Relacionar usuários à empresa
			empresa1.getUsuarios().add(cliente);
			empresa1.getUsuarios().add(funcionario);

			// Salvar tudo
			empresaRepositorio.save(empresa1);
			usuarioRepositorio.save(cliente);
			usuarioRepositorio.save(funcionario);
			mercadoriaRepositorio.save(mercadoria);
		}
	}
}
