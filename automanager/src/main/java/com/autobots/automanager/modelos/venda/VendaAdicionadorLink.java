package com.autobots.automanager.modelos.venda;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class VendaAdicionadorLink implements AdicionadorLink<Venda>{
    @Override
	public void adicionarLinkList(List<Venda> lista) {
		for (Venda venda : lista) {
			long id = venda.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(UsuarioControle.class)
							.obterUsuario(id))
					.withSelfRel();
			venda.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Venda objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(UsuarioControle.class)
						.obterusuarios())
				.withRel("Lista de Vendas");
		objeto.add(linkProprio);
    }
}
