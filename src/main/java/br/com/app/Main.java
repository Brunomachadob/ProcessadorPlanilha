package br.com.app;

import br.com.app.util.WeldUtil;

public class Main {

	public static void main(String[] args) {
		Application application = WeldUtil.select(Application.class);
		application.run();
		
//		Dataset<ConfiguracaoProcessamento> dataset = new Dataset<>(new GenericDAO<>(ConfiguracaoProcessamento.class));
//		
//		ConfiguracaoProcessamento cfg = new ConfiguracaoProcessamento();
//		cfg.setDescricao("Teste3");
//		cfg.setId("idTeste3");
//		
//		dataset.goToInsertion(cfg);
//		dataset.save();
		
	}

}
