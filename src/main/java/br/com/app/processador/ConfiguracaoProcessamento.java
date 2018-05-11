package br.com.app.processador;

import java.util.List;

public class ConfiguracaoProcessamento {
	public boolean temCabecalho;
	public List<ConfiguracaoColuna> colunas;
	
	public void setTemCabecalho(boolean temCabecalho) {
		this.temCabecalho = temCabecalho;
	}
	
	public void setColunas(List<ConfiguracaoColuna> colunas) {
		this.colunas = colunas;
	}
	
	public static class ConfiguracaoColuna {
		public String nome;
		public String descricao;
		public List<String> processadores;
	
		public void setNome(String nome) {
			this.nome = nome;
		}
		
		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		
		public void setProcessadores(List<String> processadores) {
			this.processadores = processadores;
		}
	}
}
