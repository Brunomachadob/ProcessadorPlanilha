package br.com.app.processador.transformadores;

import br.com.app.processador.ProcessadorPlanilha;
import br.com.app.processador.Transformador;

public class AjustarCPF implements Transformador {

	private static Transformador converterTexto = ProcessadorPlanilha.getTransformador("ConverterTexto");
	private static Transformador removerPontuacoes = ProcessadorPlanilha.getTransformador("RemoverPontuacoes");
	private static Transformador removerEspacos = ProcessadorPlanilha.getTransformador("RemoverEspacos");

	@Override
	public String transformar(Object valor) {
		if (valor == null) {
			return null;
		}

		String cpf = (String) converterTexto.transformar(valor);

		cpf = (String) removerPontuacoes.transformar(cpf);
		cpf = (String) removerEspacos.transformar(cpf);

		if (cpf.length() == 10) {
			cpf = "0" + cpf;
		}

		if (cpf.length() != 11) {
			throw new IllegalStateException("CPF com valor inválido: " + cpf);
		}

		return cpf;
	}
}
