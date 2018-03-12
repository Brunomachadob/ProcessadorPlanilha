package br.com.app.processador.transformadores;

import br.com.app.processador.TransformadorRegexReplace;

public class RemoverPontuacoes extends TransformadorRegexReplace {

	public RemoverPontuacoes() {
		super("([^a-zA-Z0-9])", "");
	}
}
