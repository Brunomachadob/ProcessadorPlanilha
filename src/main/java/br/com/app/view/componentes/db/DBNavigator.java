package br.com.app.view.componentes.db;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import br.com.app.view.componentes.dataset.Dataset;
import br.com.app.view.componentes.dataset.DatasetObserver;

public class DBNavigator<T> extends JPanel implements DatasetObserver<T> {
	private static final long serialVersionUID = 1L;

	private JButton btnFirst;
	private JButton btnPrev;
	private JButton btnNext;
	private JButton btnLast;

	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnRemove;

	private transient Dataset<T> dataset;

	private JButton[] nonDirtyVisibleButtons;
	private JButton[] dirtyVisibleButtons;

	public DBNavigator(Dataset<T> dataset) {
		this.dataset = dataset;

		setLayout(new FlowLayout());

		btnFirst = new JButton("Primeiro");
		btnFirst.addActionListener(this::onClickFirst);
		add(btnFirst);

		btnPrev = new JButton("Anterior");
		btnPrev.addActionListener(this::onClickPrev);
		add(btnPrev);

		btnNext = new JButton("Próximo");
		btnNext.addActionListener(this::onClickNext);
		add(btnNext);

		btnLast = new JButton("Último");
		btnLast.addActionListener(this::onClickLast);
		add(btnLast);

		btnRemove = new JButton("Deletar");
		btnRemove.addActionListener(this::onClickRemove);
		add(btnRemove);

		btnSave = new JButton("Salvar");
		btnSave.addActionListener(this::onClickSave);
		add(btnSave);

		btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(this::onClickCancel);
		add(btnCancel);

		nonDirtyVisibleButtons = new JButton[] { btnFirst, btnPrev, btnNext, btnLast, btnRemove };
		dirtyVisibleButtons = new JButton[] { btnSave, btnCancel };

		dataset.addObserver(this);

		currentLineChange(dataset.getIndex(), dataset.getCurrentEntity());
	}

	private void onClickFirst(ActionEvent e) {
		this.dataset.first();
	}

	private void onClickNext(ActionEvent e) {
		this.dataset.next();
	}

	private void onClickPrev(ActionEvent e) {
		this.dataset.previous();
	}

	private void onClickLast(ActionEvent e) {
		this.dataset.last();
	}

	private void onClickRemove(ActionEvent e) {
		this.dataset.remove();
	}

	private void onClickSave(ActionEvent e) {
		this.dataset.save();
	}

	private void onClickCancel(ActionEvent e) {
	}

	public void currentLineChange(int index, Object entity) {
		for (JButton btn : nonDirtyVisibleButtons) {
			btn.setVisible(!dataset.isDirty());
		}

		for (JButton btn : dirtyVisibleButtons) {
			btn.setVisible(dataset.isDirty());
		}
	}

	public void refreshed() {
		// Não é necessário
	}

}
