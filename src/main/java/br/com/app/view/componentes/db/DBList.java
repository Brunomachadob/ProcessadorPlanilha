package br.com.app.view.componentes.db;

import java.awt.Color;
import java.awt.Component;
import java.util.function.Function;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.app.view.componentes.dataset.Dataset;
import br.com.app.view.componentes.dataset.DatasetObserver;

public class DBList<T> extends JList<T> implements DatasetObserver<T>, ListSelectionListener {
	private static final long serialVersionUID = 1L;

	private Dataset<T> dataset;

	public DBList(Dataset<T> dataset) {
		super();

		this.dataset = dataset;
		this.dataset.addObserver(this);

		setModel(new DBListModel());
		
		addListSelectionListener(this);
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		currentLineChange(dataset.getIndex(), dataset.getCurrentEntity());
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			this.dataset.setIndex(getSelectedIndex());
		}
	}

	@Override
	public void currentLineChange(int index, T entity) {
		setSelectedIndex(index);
	}

	@Override
	public void refreshed() {
		clearSelection();
	}

	private class DBListModel implements ListModel<T> {

		public DBListModel() {
		}

		@Override
		public int getSize() {
			return dataset.size();
		}

		@Override
		public T getElementAt(int index) {
			return dataset.getEntityAt(index);
		}

		@Override
		public void addListDataListener(ListDataListener arg0) {
		}

		@Override
		public void removeListDataListener(ListDataListener arg0) {

		}
	}

	public static class SimpleTextRenderer<T> extends JLabel implements ListCellRenderer<T> {
		private static final long serialVersionUID = 1L;

		private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);
		
		private Function<T, String> entityToString;

		public SimpleTextRenderer(Function<T, String> entityToString) {
			setOpaque(true);
			setIconTextGap(12);
			
			this.entityToString = entityToString;
		}

		public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected,
				boolean cellHasFocus) {

			setText(entityToString.apply(value));

			if (isSelected) {
				setBackground(HIGHLIGHT_COLOR);
				setForeground(Color.white);
			} else {
				setBackground(Color.white);
				setForeground(Color.black);
			}
			
			return this;
		}
	}
}
