package br.com.app.processador.transformadores;

import br.com.app.processador.Transformador;

public class ConverterTexto implements Transformador {

	@Override
	public String transformar(Object valor) {
		if (valor == null) {
			return null;
		} else if (valor instanceof String) {
			return (String) valor;
		} else if (valor instanceof Number) {
			return ((Number) valor).toString();
		} else {
			return null;
		}
	}
}
