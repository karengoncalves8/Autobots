package com.autobots.automanager.modelos.veiculo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class VeiculoAdicionadorLink implements AdicionadorLink<Veiculo> {
    @Override
	public void adicionarLinkList(List<Veiculo> lista) {
		for (Veiculo veiculo : lista) {
			long id = veiculo.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(UsuarioControle.class)
							.obterUsuario(id))
					.withSelfRel();
			veiculo.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Veiculo objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(UsuarioControle.class)
						.obterusuarios())
				.withRel("Lista de Veículos");
		objeto.add(linkProprio);
    }
}
