package br.com.app.view.componentes.db.input;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import br.com.app.view.componentes.dataset.Dataset;
import br.com.app.view.componentes.dataset.FieldBinder;

public class DBTextInput<T> extends JTextField implements FieldBinder {
	private static final long serialVersionUID = 1L;

	private transient Dataset<T> dataset;
	private String fieldName;

	public DBTextInput(String fieldName, Dataset<T> dataset) {
		super();

		this.fieldName = fieldName;

		this.dataset = dataset;
		this.dataset.addFieldBinder(this);
		
		this.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				onTextChange();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				onTextChange();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				onTextChange();
			}
		});
	}
	
	private void onTextChange() {
		dataset.setModified(this);
	}

	@Override
	public String getFieldName() {
		return this.fieldName;
	}

	@Override
	public void setData(Object value) {
		this.setText((String) value);
	}

	@Override
	public Object getData() {
		return this.getText();
	}
}
