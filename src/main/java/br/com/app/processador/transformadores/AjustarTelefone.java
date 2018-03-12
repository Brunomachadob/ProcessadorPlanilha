package br.com.app.processador.transformadores;

import br.com.app.processador.ProcessadorPlanilha;
import br.com.app.processador.Transformador;

public class AjustarTelefone implements Transformador {

	private static Transformador removerPontuacoes = ProcessadorPlanilha.getTransformador("RemoverPontuacoes");
	private static Transformador removerEspacos = ProcessadorPlanilha.getTransformador("RemoverEspacos");

	@Override
	public String transformar(Object valor) {
		if (valor == null) {
			return null;
		}

		String telefone = (String) valor;

		telefone = telefone.replaceAll("\\+55", "").replaceAll("-", "");

		telefone = (String) removerPontuacoes.transformar(telefone);
		telefone = (String) removerEspacos.transformar(telefone);

		if (telefone.startsWith("0")) {
			telefone = telefone.substring(1);
		}

		if (telefone.length() < 8 || telefone.length() > 11) {
			throw new IllegalStateException("Telefone com valor inválido: " + telefone);
		}
		
		if (telefone.length() == 8) {
			boolean isCelular = telefone.startsWith("8") || telefone.startsWith("9");
			
			telefone = "34" + (isCelular ? "9" : "") + telefone;
		} else if (telefone.length() == 9) {
			telefone = "34" + telefone;
		} else if (telefone.length() == 10 && isCelular(telefone.substring(2))) {
			telefone = telefone.substring(0, 2) + "9" + telefone.substring(2);
		}
		
		return telefone;
	}
	
	public static boolean isCelular(String telefone) {
		return telefone.startsWith("8") || telefone.startsWith("9");
	}

}
