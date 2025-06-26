package com.autobots.automanager.modelos.telefone;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class TelefoneAdicionadorLink implements AdicionadorLink<Telefone>{
    @Override
	public void adicionarLinkList(List<Telefone> lista) {
		for (Telefone telefone : lista) {
			long id = telefone.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(TelefoneControle.class)
							.obterTelefonePorId(id))
					.withSelfRel();
			telefone.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Telefone objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(TelefoneControle.class)
						.obterTodosTelefones())
				.withRel("Lista de Telefones");
		objeto.add(linkProprio);
	}
}
