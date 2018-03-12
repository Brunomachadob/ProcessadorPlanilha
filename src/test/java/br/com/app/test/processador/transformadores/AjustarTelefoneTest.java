package br.com.app.test.processador.transformadores;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import br.com.app.processador.Transformador;
import br.com.app.processador.transformadores.AjustarTelefone;

public class AjustarTelefoneTest {

	Transformador transformadorTelefone = new AjustarTelefone();

	@Test
	public void testNroResidencial() {
		validarTransformacao("3298-3965", "3432983965");
	}

	@Test
	public void testNroResidencialComDDD() {
		validarTransformacao("(34)3298-3965", "3432983965");
	}

	@Test
	public void testNroResidencialComDDDZero() {
		validarTransformacao("(034)3298-3965", "3432983965");
	}

	@Test
	public void testNroCelularSemDDDENove() {
		validarTransformacao("+558865-2760", "34988652760");
	}

	@Test
	public void testNroCelularComDDDSemNove() {
		validarTransformacao("348865-2760", "34988652760");
	}

	@Test
	public void testNroCelularSemDDDComNove() {
		validarTransformacao("98865-2760", "34988652760");
	}

	private void validarTransformacao(String nro, String esperado) {
		String ajustado = (String) transformadorTelefone.transformar(nro);
		assertEquals(ajustado, esperado);
	}
}
