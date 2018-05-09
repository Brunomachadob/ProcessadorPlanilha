package br.com.app.test.processador.transformadores;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.app.processador.Transformador;
import br.com.app.processador.transformadores.RemoverEspacosSobressalentes;

public class RemoverEspacosSobressalentesTest {

	Transformador transformador = new RemoverEspacosSobressalentes();

	@Test
	public void valorSimples() {
		validarTransformacao("teste", "teste");
	}

	@Test
	public void valorComUmEspaco() {
		validarTransformacao("Nome de alguem", "Nome de alguem");
	}

	@Test
	public void valorComDoisEspacos() {
		validarTransformacao("Nome  de  alguem", "Nome de alguem");
	}

	@Test
	public void valorComEspacosLaterais() {
		validarTransformacao("  Nome  de  alguem  ", "Nome de alguem");
	}

	private void validarTransformacao(String nro, String esperado) {
		String ajustado = (String) transformador.transformar(nro);
		assertEquals(ajustado, esperado);
	}
}
