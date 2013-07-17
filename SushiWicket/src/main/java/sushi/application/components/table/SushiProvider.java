package sushi.application.components.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.persistence.Persistable;

public class SushiProvider<E extends Persistable> extends AbstractDataProvider implements ISortableDataProvider<E, String> {

	private static final long serialVersionUID = 1L;
	protected List<E> entities;
	protected List<E> selectedEntities;
	protected ISortState<String> sortState = new SingleSortState<String>();
	
	public SushiProvider(List<E> entities) {
		this.entities = entities;
		this.selectedEntities = new ArrayList<E>();
	}

	public SushiProvider(List<E> entities, List<E> selectedEntities) {
		this.entities = entities;
		this.selectedEntities = selectedEntities;
	}

	
	@Override
	public void detach() {
//		attributes = null;
	}
	
	public void removeItem(E p) {
		this.entities.remove(p);
	}
	
	public void addItem(E p) {
		this.entities.add(p);
	}
	
	@Override
	public Iterator<? extends E> iterator(long first, long count) {
		List<E> data = entities;
		Collections.sort(data, new Comparator<E>() {
			public int compare(E e1, E e2) {
				return (new Integer(e1.getID()).compareTo(e2.getID()));
			}
		});
		return data.subList((int)first, (int)Math.min(first + count, data.size())).iterator();
	}

	@Override
	public IModel<E> model(E entity) {
		return Model.of(entity);
	}

	@Override
	public long size() {
		return entities.size();
	}

	public List<E> getEntities() {
		return entities;
	}
	
	public List<E> getSelectedEntities(){
		return selectedEntities;
	}

	public void setEntities(List<E> entityList) {
		entities = entityList;
	}
	
	@Override
	public ISortState<String> getSortState() {
		return sortState;
	}
	
	@Override
	public void selectEntry(int entryId) {
		for (Iterator<E> iter = entities.iterator(); iter.hasNext(); ) {
			E entity = iter.next();
			if(entity.getID() == entryId) {
				selectEntry(entity);
				return;
			}
		}
	}
	
	public void selectEntry(E entity) {
		selectedEntities.add(entity);
	}
	
	@Override
	public void deselectEntry(int entryId) {
		for (Iterator<E> iter = entities.iterator(); iter.hasNext(); ) {
			E entity = iter.next();
			if (entity.getID() == entryId) {
				deselectEntry(entity);
				return;
			}
		}
	}
	
	public void deselectEntry(E entity) {
		selectedEntities.remove(entity);
	}
	
	public void clearSelectedEntities() {
		selectedEntities.clear();
	}
	
	@Override
	public boolean isEntrySelected(int entryId) {
		for (E entity : selectedEntities) {
			if (entity.getID() == entryId) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isEntrySelected(E notification) {
		if (selectedEntities.contains(notification)) {
			return true;
		}
		return false;
	}

	public void deleteSelectedEntries() {
		for (E entity : selectedEntities) {
			entities.remove(entity);
			entity.remove();
		}
	}

	public void selectAllEntries() {
		for (E entity : entities) {
			selectedEntities.add(entity);
		}
	}

	@Override
	public Object getEntry(int entryId) {
		// TODO Auto-generated method stub
		return null;
	}

}
