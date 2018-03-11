package br.com.app.processador;

import java.util.ArrayList;

public class ConfiguracaoProcessamento {
	boolean temCabecalho;
	ArrayList<ConfiguracaoColuna> colunas;
	
	public static class ConfiguracaoColuna {
		String nome;
		String descricao;
		ArrayList<String> processadores;
	}
}
