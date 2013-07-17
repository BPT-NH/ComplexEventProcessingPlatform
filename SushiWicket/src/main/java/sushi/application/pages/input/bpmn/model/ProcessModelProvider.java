package sushi.application.pages.input.bpmn.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNProcess;

/**
 * This class is the provider for {@link BPMNProcess}es.
 * @author micha
 */
public class ProcessModelProvider extends AbstractDataProvider implements IDataProvider<AbstractBPMNElement>, ISortableDataProvider<AbstractBPMNElement, String> {
	
	private static final long serialVersionUID = 1L;
	private BPMNProcess process;
	private List<AbstractBPMNElement> elements;
	private List<AbstractBPMNElement> selectedElements;
	
	/**
	 * Constructor for providing {@link BPMNProcess}es.
	 */
	public ProcessModelProvider() {
		elements = new ArrayList<AbstractBPMNElement>();
		selectedElements = new ArrayList<AbstractBPMNElement>();
	}
	
	public ProcessModelProvider(BPMNProcess process) {
		this.process = process;
		elements = process.getBPMNElements();
		selectedElements = new ArrayList<AbstractBPMNElement>();
	}
	
	@Override
	public void detach() {
//		events = null;
	}
	
	@Override
	public Iterator<AbstractBPMNElement> iterator(long first, long count) {
		List<AbstractBPMNElement> data = elements;
		Collections.sort(data, new Comparator<AbstractBPMNElement>() {
			public int compare(AbstractBPMNElement e1, AbstractBPMNElement e2) {
				return (new Integer(e1.getID()).compareTo(e2.getID()));
			}
		});
		return data.subList((int)first, (int)Math.min(first + count, data.size())).iterator();
	}

	@Override
	public IModel<AbstractBPMNElement> model(AbstractBPMNElement element) {
		return Model.of(element);
	}

	@Override
	public long size() {
		return elements.size();
	}

	public List<AbstractBPMNElement> getElements() {
		return elements;
	}
	
	public List<AbstractBPMNElement> getSelectedElements(){
		return selectedElements;
	}

	public void setElements(List<AbstractBPMNElement> elementList) {
		elements = elementList;
	}


	@Override
	public void selectEntry(int entryId) {
		for (AbstractBPMNElement element : elements) {
			if(element.getID() == entryId) {
				selectedElements.add(element);
				return;
			}
		}
	}
	
	@Override
	public void deselectEntry(int entryId) {
		for (AbstractBPMNElement element : elements) {
			if(element.getID() == entryId) {
				selectedElements.remove(element);
				return;
			}
		}
	}
	
	@Override
	public boolean isEntrySelected(int entryId) {
		for (AbstractBPMNElement element : selectedElements) {
			if(element.getID() == entryId) {
				return true;
			}
		}
		return false;
	}

	public void deleteSelectedEntries() {
		for(AbstractBPMNElement element : selectedElements){
			elements.remove(element);
			element.remove();
		}
	}

	public void selectAllEntries() {
		for(AbstractBPMNElement element : elements){
			if(!selectedElements.contains(element)){
				selectedElements.add(element);
			}
		}
	}

	public void setProcessModel(BPMNProcess processModel) {
		this.process = processModel;
		if(this.process != null){
			this.elements = this.process.getBPMNElementsWithOutSequenceFlows();
		} else {
			this.elements = new ArrayList<AbstractBPMNElement>();
		}
		this.selectedElements = new ArrayList<AbstractBPMNElement>();
	}

	@Override
	public ISortState<String> getSortState() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object getEntry(int entryId) {
		for(AbstractBPMNElement element : elements){
			if(element.getID() == entryId){
				return element;
			}
		}
		return null;
	}

}
