package com.autobots.automanager.modelos.endereco;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class EnderecoAdicionadorLink implements AdicionadorLink<Endereco>{
    @Override
	public void adicionarLinkList(List<Endereco> lista) {
		for (Endereco endereco : lista) {
			long id = endereco.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(EnderecoControle.class)
							.obterEnderecoPorId(id))
					.withSelfRel();
			endereco.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Endereco objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EnderecoControle.class)
						.obterTodosEnderecos())
				.withRel("Lista de Endereços");
		objeto.add(linkProprio);
	}

	public void adicionarLinkObj(Endereco obj) {
		long id = obj.getId();
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EnderecoControle.class)
						.obterEnderecoPorId(id))
				.withSelfRel();
		obj.add(linkProprio);
	}
}
