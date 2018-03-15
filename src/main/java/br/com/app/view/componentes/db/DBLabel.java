package br.com.app.view.componentes.db;

import java.util.function.Function;

import javax.swing.JLabel;

import br.com.app.view.componentes.dataset.Dataset;
import br.com.app.view.componentes.dataset.DatasetObserver;

public class DBLabel<T> extends JLabel implements DatasetObserver<T> {
	private static final long serialVersionUID = 1L;
	
	private transient Dataset<T> dataset;
	private transient Function<T, String> entityToString;

	public DBLabel(Dataset<T> dataset, Function<T, String> entityToString) {
		super();

		this.dataset = dataset;
		this.dataset.addObserver(this);
		this.entityToString = entityToString;
		
		currentLineChange(dataset.getIndex(), dataset.getCurrentEntity());
	}

	@Override
	public void currentLineChange(int index, T entity) {
		if (index > -1) {
			setText(entityToString.apply(entity));
		} else {
			setText("");
		}
	}

	@Override
	public void refreshed() {
	}
}
