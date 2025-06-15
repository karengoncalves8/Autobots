package com.autobots.automanager.modelo;

import java.util.List;

public interface AdicionadorLink<T> {
	public void adicionarLinkList(List<T> lista);
	public void adicionarLink(T objeto);
}
