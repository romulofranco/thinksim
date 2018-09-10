package br.unicamp.fe.thinksim.entity;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class EntityDataModel<T extends EntityModel> extends ListDataModel<T>
		implements SelectableDataModel<T>, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public EntityDataModel(List<T> data) {
		super(data);
	}

	@Override
	public T getRowData(String rowKey) {
		List<T> items = (List<T>) getWrappedData();
		Integer integerRowKey = Integer.parseInt(rowKey);

		for (T item : items) {
			if (item.getId().equals(integerRowKey)) {
				return item;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(T item) {
		return item.getId();
	}

}
