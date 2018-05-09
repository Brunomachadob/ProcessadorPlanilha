package br.com.app.processador.transformadores;

import br.com.app.processador.TransformadorRegexReplace;

public class RemoverEspacosSobressalentes extends TransformadorRegexReplace {

	public RemoverEspacosSobressalentes() {
		super("\\s+", " ");
	}
	
	@Override
	public String transformar(Object valor) {
		return super.transformar(valor).trim();
	}
}

