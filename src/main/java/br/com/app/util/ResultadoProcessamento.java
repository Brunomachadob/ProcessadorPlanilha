package br.com.app.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResultadoProcessamento {

	public File planilhaProcessada;
	public final List<ErroImportacao> erros;
	
	public ResultadoProcessamento() {
		this.erros = new ArrayList<>();
	}
	
	public static class ErroImportacao {
		public String identificador;
		public String mensagem;
		
		public ErroImportacao(String identificador, String mensagem) {
			this.identificador = identificador;
			this.mensagem = mensagem;
		}
	}
}
