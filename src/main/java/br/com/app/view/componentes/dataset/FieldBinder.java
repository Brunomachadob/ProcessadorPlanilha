package br.com.app.view.componentes.dataset;

public interface FieldBinder {
	public void setData(Object value);
	public Object getData();
	
	public String getFieldName();
}
