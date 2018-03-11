package br.com.app.processador.transformadores;

import br.com.app.processador.Transformador;

public class CaixaBaixa implements Transformador {

	@Override
	public String transformar(Object valor) {
		if (valor == null) {
			return null;
		}

		return ((String) valor).toLowerCase();
	}

}
