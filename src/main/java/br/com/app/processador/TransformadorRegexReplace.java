package br.com.app.processador;

import java.util.regex.Pattern;

import br.com.app.processador.Transformador;

public class TransformadorRegexReplace implements Transformador {

	protected Pattern pattern;
	protected String replace;

	public TransformadorRegexReplace(String regex, String replace) {
		this.pattern = Pattern.compile(regex);
		this.replace = replace;
	}

	@Override
	public String transformar(Object valor) {
		if (valor == null) {
			return null;
		}

		return pattern.matcher((String) valor).replaceAll(this.replace);
	}

}
