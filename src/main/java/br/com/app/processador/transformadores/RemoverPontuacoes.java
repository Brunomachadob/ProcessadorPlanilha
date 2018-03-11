package br.com.app.processador.transformadores;

import java.util.regex.Pattern;

import br.com.app.processador.Transformador;

public class RemoverPontuacoes implements Transformador {

	Pattern p = Pattern.compile("\\D");

	@Override
	public String transformar(Object valor) {
		if (valor == null) {
			return null;
		}

		return p.matcher((String) valor).replaceAll("");
	}

}
