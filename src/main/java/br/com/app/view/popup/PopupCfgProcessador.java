package br.com.app.view.popup;

import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

import br.com.app.dao.GenericDAO;
import br.com.app.processador.model.ConfiguracaoProcessamento;
import br.com.app.view.componentes.dataset.Dataset;
import br.com.app.view.componentes.db.DBLabel;
import br.com.app.view.componentes.db.DBList;
import br.com.app.view.componentes.db.DBNavigator;
import br.com.app.view.componentes.db.DBList.SimpleTextRenderer;

public class PopupCfgProcessador extends JDialog {
	private static final long serialVersionUID = 1L;

	private Dataset<ConfiguracaoProcessamento> dataset;

	JList<ConfiguracaoProcessamento> list;
	JPanel panel = new JPanel();

	private PopupCfgProcessador(Frame parent) {
		super(parent, "Configuração do Processador");

		setLayout(new FlowLayout());

		dataset = new Dataset<>(new GenericDAO<>(ConfiguracaoProcessamento.class));
		dataset.load();

		DBNavigator<ConfiguracaoProcessamento> navigator = new DBNavigator<>(dataset);
		add(navigator);

		list = new DBList<>(dataset);
		list.setCellRenderer(
				new SimpleTextRenderer<ConfiguracaoProcessamento>(ConfiguracaoProcessamento::getDescricao));
		add(list);

		add(new DBLabel<ConfiguracaoProcessamento>(dataset, value -> "Indice:" + dataset.getIndex()));
	}

	public static void open(Frame parent) {
		PopupCfgProcessador instance = new PopupCfgProcessador(parent);

		instance.setSize(500, 500);
		instance.setLocationRelativeTo(parent);
		instance.setVisible(true);
	}
}
