package br.com.app.processador.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConfiguracaoProcessamento {
	
	@Id
	String id;
	
	@Column
	String descricao;
}
