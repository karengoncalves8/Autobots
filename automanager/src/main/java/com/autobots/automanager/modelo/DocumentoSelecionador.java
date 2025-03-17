package com.autobots.automanager.modelo;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Documento;

@Component
public class DocumentoSelecionador {
	public Documento selecionar(Set<Documento> documentos, long id) {
		for (Documento documento : documentos) {
			if (documento.getId() == id) {
				Documento selecionado = documento;
				return selecionado;
			}
		}
		return null;
	}
}