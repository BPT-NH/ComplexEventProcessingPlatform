package sushi.application.pages.eventrepository.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.process.SushiProcessInstance;

public class ProcessInstanceProvider extends AbstractDataProvider implements ISortableDataProvider<SushiProcessInstance, String>, IFilterStateLocator {
	
	private static List<SushiProcessInstance> processInstances;
	private ISortState sortState = new SingleSortState();
	private ProcessInstanceFilter processInstanceFilter = new ProcessInstanceFilter();
	private List<SushiProcessInstance> selectedProcessInstances;
	
	public ProcessInstanceProvider(){
		processInstances = filterProcessInstances(SushiProcessInstance.findAll(), processInstanceFilter);
		selectedProcessInstances = new ArrayList<SushiProcessInstance>();
	}
	
	@Override
	public void detach() {
		
	}

	@Override
	public Iterator<? extends SushiProcessInstance> iterator(long first, long count) {
		List<SushiProcessInstance> data = new ArrayList<SushiProcessInstance>(processInstances);
		Collections.sort(data, new Comparator<SushiProcessInstance>() {
			public int compare(SushiProcessInstance e1, SushiProcessInstance e2) {
				return (new Integer(e1.getID()).compareTo(e2.getID()));
			}
		});
		return data.subList((int)first, (int)Math.min(first + count, data.size())).iterator();
	}

	@Override
	public IModel<SushiProcessInstance> model(SushiProcessInstance processInstance) {
		return Model.of(processInstance);
	}
	
	@Override
	public long size() {
		return processInstances.size();
	}

	public static List<SushiProcessInstance> getProcessInstances() {
		return processInstances;
	}

	public static void setProcessInstances(List<SushiProcessInstance> processInstances) {
		ProcessInstanceProvider.processInstances = processInstances;
	}
	
	public ProcessInstanceFilter getProcessInstanceFilter() {
		return processInstanceFilter;
	}

	public void setProcessInstanceFilter(ProcessInstanceFilter processInstanceFilter) {
		this.processInstanceFilter = processInstanceFilter;
		processInstances = filterProcessInstances(SushiProcessInstance.findAll(), processInstanceFilter);
	}
	
	private List<SushiProcessInstance> filterProcessInstances(List<SushiProcessInstance> processInstancesToFilter, ProcessInstanceFilter processInstanceFilter) {
		List<SushiProcessInstance> returnedProcessInstances = new ArrayList<SushiProcessInstance>();
		for(SushiProcessInstance processInstance: processInstancesToFilter){
			if(processInstanceFilter.match(processInstance)){
				returnedProcessInstances.add(processInstance);
			}
		}
		return returnedProcessInstances;
	}
	
	@Override
	public ISortState<String> getSortState() {
		return sortState;
	}
	
	@Override
	public Object getFilterState() {
		return processInstanceFilter;
	}

	@Override
	public void setFilterState(Object state) {
		this.processInstanceFilter = (ProcessInstanceFilter) state;
	}
	
	@Override
	public void selectEntry(int entryId) {
		for (Iterator iter = processInstances.iterator(); iter.hasNext();) {
			SushiProcessInstance processInstance = (SushiProcessInstance) iter.next();
			if(processInstance.getID() == entryId) {
				selectedProcessInstances.add(processInstance);
				return;
			}
		}
	}
	
	@Override
	public void deselectEntry(int entryId) {
		for (Iterator<SushiProcessInstance> iter = processInstances.iterator(); iter.hasNext();) {
			SushiProcessInstance processInstance = (SushiProcessInstance) iter.next();
			if(processInstance.getID() == entryId) {
				selectedProcessInstances.remove(processInstance);
				return;
			}
		}
	}
	
	@Override
	public boolean isEntrySelected(int entryId) {
		for(SushiProcessInstance processInstance : selectedProcessInstances){
			if(processInstance.getID() == entryId) {
				return true;
			}
		}
		return false;
	}

	public void deleteSelectedEntries() {
		for(SushiProcessInstance processInstance : selectedProcessInstances){
			processInstances.remove(processInstance);
			processInstance.remove();
		}
	}
	
	public void selectAllEntries() {
		for(SushiProcessInstance processInstance : processInstances){
			selectedProcessInstances.add(processInstance);
		}
	}
	
	@Override
	public Object getEntry(int entryId) {
		for(SushiProcessInstance processInstance : processInstances){
			if(processInstance.getID() == entryId){
				return processInstance;
			}
		}
		return null;
	}

}
