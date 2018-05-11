package br.com.app.interseccao;

import java.util.List;

public class ConfiguracaoInterseccao {
	boolean temCabecalho;
	List<String> colunas;
	
	public void setTemCabecalho(boolean temCabecalho) {
		this.temCabecalho = temCabecalho;
	}

	public void setColunas(List<String> colunas) {
		this.colunas = colunas;
	}
}
