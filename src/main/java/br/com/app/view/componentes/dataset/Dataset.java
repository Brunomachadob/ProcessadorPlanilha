package br.com.app.view.componentes.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import br.com.app.dao.GenericDAO;

public class Dataset<T> {

	T dirty;
	List<T> records;
	GenericDAO<T> dao;

	int index = -1;

	Map<String, List<FieldBinder>> fieldBinders;
	List<DatasetObserver<T>> observers;

	Map<String, DatasetFieldBridge<T>> fieldBridges;

	public Dataset(GenericDAO<T> dao) {
		this.records = new ArrayList<>();
		this.observers = new ArrayList<>();
		this.fieldBinders = new HashMap<>();
		this.fieldBridges = new HashMap<>();
		
		this.dao = dao;
	}

	public void addObserver(DatasetObserver<T> obs) {
		this.observers.add(obs);
	}

	public void addFieldBinder(FieldBinder fieldBinder) {
		String fieldName = fieldBinder.getFieldName();

		fieldBinders.compute(fieldName, (key, value) -> {
			if (value == null) {
				value = new ArrayList<>();
			}

			value.add(fieldBinder);

			return value;
		});

		refreshFieldBinder(fieldBinder);
	}

	public void goToInsertion(T entity) {
		this.dirty = entity;
		this.index = -1;
	}

	public void load() {
		this.index = -1;
		records = dao.list();

		resetState();

		if (!empty()) {
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

	public boolean isDirty() {
		return this.dirty != null;
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

			refreshFieldBinders();

			T currentEntity = getCurrentEntity();
			observers.forEach(obs -> obs.currentLineChange(index, currentEntity));
		}
	}
	
	public void setModified(FieldBinder binder) {
		DatasetFieldBridge<T> brigde = fieldBridges.get(binder.getFieldName());
	}

	private void refreshFieldBinders() {
		fieldBinders.values().forEach(list -> list.forEach(this::refreshFieldBinder));
	}

	private void refreshFieldBinder(FieldBinder fieldBinder) {
		DatasetFieldBridge<T> brigde = fieldBridges.get(fieldBinder.getFieldName());

		if (brigde != null) {
			T entity = getCurrentEntity();
			Object value = null;

			if (entity != null) {
				value = brigde.entityToData.apply(getCurrentEntity());
			}

			fieldBinder.setData(value);
		}
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

	public boolean empty() {
		return records.isEmpty();
	}

	public void first() {
		if (!empty()) {
			setIndex(0);
		}
	}

	public void next() {
		if (index + 1 < records.size()) {
			setIndex(index + 1);
		}
	}

	public void previous() {
		if (!empty() && (index - 1 >= 0)) {
			setIndex(index - 1);
		}
	}

	public void last() {
		if (records.size() - 1 >= 0) {
			setIndex(records.size() - 1);
		}
	}

	public void save() {
		this.dao.save(dirty);
		this.records.add(dirty);
		this.dirty = null;
		setIndex(size() - 1);
	}

	public void remove() {
		if (!isDirty()) {
			T entity = getCurrentEntity();
			this.dao.remove(entity);
			this.records.remove(entity);

			if (empty()) {
				setIndex(-1);
			} else if (index > 0) {
				setIndex(index - 1);
			}
		}
	}

	public void addEntityField(String field, EntityToData<T> entityToData) {
		DatasetFieldBridge<T> bridge = new DatasetFieldBridge<>();
		bridge.entityToData = entityToData;

		this.fieldBridges.put(field, bridge);
	}

	public static class DatasetFieldBridge<T> {
		EntityToData<T> entityToData;
	}

	public static interface EntityToData<T> extends Function<T, Object> {
	}
}
