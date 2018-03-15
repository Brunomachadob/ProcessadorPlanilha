package br.com.app.view.componentes.dataset;

import java.util.ArrayList;
import java.util.List;

import br.com.app.dao.GenericDAO;

public class Dataset<T> {

	T dirty;
	List<T> records;
	GenericDAO<T> dao;

	int index = -1;

	List<DatasetObserver<T>> observers;

	public Dataset(GenericDAO<T> dao) {
		this.records = new ArrayList<>();
		this.observers = new ArrayList<>();
		this.dao = dao;
	}

	public void addObserver(DatasetObserver<T> obs) {
		this.observers.add(obs);
	}
	
	public void goToInsertion(T entity) {
		this.dirty = entity;
		this.index = -1;
	}
	
	public void save() {
		this.dao.save(dirty);
		this.records.add(dirty);
		this.dirty = null;
		this.index = size() - 1;
		fireLineChange();
	}

	public void load() {
		records = dao.list();

		resetState();

		if (records.size() > 0) {
			setIndex(0);
		}
	}

	public List<T> getRecords() {
		return this.records;
	}

	public T getCurrentEntity() {
		if (dirty != null) {
			return dirty;
		} else if (isValidIndex(index)) {
			return records.get(index);
		} else {
			return null;
		}
	}

	public T getEntityAt(int index) {
		if (isValidIndex(index)) {
			return records.get(index);
		} else {
			return null;
		}
	}
	
	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		if (isValidIndex(index)) {
			this.index = index;
			fireLineChange();
		}
	}
	
	private void fireLineChange() {
		T currentEntity = getCurrentEntity();

		observers.forEach(obs -> {
			obs.currentLineChange(index, currentEntity);
		});
	}
	
	private boolean isValidIndex(int index) {
		return !records.isEmpty() && (index >= 0 && index <= records.size());
	}

	protected void resetState() {
		index = -1;
		dirty = null;
	}

	public int size() {
		return records.size();
	}
}
