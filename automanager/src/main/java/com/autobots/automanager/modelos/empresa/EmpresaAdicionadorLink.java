package com.autobots.automanager.modelos.empresa;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class EmpresaAdicionadorLink implements AdicionadorLink<Empresa>{
    @Override
	public void adicionarLinkList(List<Empresa> lista) {
		for (Empresa empresa : lista) {
			long id = empresa.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(EmpresaControle.class)
							.obterEmpresa(id))
					.withSelfRel();
			empresa.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Empresa objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EmpresaControle.class)
						.obterEmpresas())
				.withRel("Lista de Empresas");
		objeto.add(linkProprio);
	}
}
