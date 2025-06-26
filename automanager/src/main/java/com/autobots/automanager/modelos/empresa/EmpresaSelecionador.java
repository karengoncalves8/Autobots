package com.autobots.automanager.modelos.empresa;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Empresa;

@Component
public class EmpresaSelecionador {
	public Empresa selecionar(Set<Empresa> empresas, Long id) {
		for (Empresa empresa : empresas) {
			if (empresa.getId() == id) {
				Empresa selecionado = empresa;
				return selecionado;
			}
		}
		return null;
	}
}
