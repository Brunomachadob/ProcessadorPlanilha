package br.com.app.processador.transformadores;

import br.com.app.processador.TransformadorRegexReplace;

public class RemoverEspacos extends TransformadorRegexReplace {

	public RemoverEspacos() {
		super("\\s", "");
	}
}

