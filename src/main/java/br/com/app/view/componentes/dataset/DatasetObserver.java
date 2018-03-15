package br.com.app.view.componentes.dataset;

public interface DatasetObserver<T> {
	public void currentLineChange(int index, T entity);
	public void refreshed();
}
