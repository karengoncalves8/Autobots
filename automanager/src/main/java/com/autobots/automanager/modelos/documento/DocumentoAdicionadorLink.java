package com.autobots.automanager.modelos.documento;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.DocumentoControle;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class DocumentoAdicionadorLink implements AdicionadorLink<Documento>{
    @Override
	public void adicionarLinkList(List<Documento> lista) {
		for (Documento documento : lista) {
			long id = documento.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(DocumentoControle.class)
							.obterDocumentoPorId(id))
					.withSelfRel();
			documento.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Documento objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(DocumentoControle.class)
						.obterTodosDocumetnos())
				.withRel("Lista de Documentos");
		objeto.add(linkProprio);
	}
}
