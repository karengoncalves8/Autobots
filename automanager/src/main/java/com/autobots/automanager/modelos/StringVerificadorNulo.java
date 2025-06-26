package com.autobots.automanager.modelos;

import java.util.Date;

public class StringVerificadorNulo {

	public boolean verificar(String dado) {
		boolean nulo = true;
		if (!(dado == null)) {
			if (!dado.isBlank()) {
				nulo = false;
			}
		}
		return nulo;
	}

	public <T> boolean verificarGenerico(T dado) {
		return dado == null;
	}
}
