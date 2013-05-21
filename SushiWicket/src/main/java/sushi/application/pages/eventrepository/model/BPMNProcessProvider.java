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
import sushi.bpmn.element.BPMNProcess;

@SuppressWarnings("serial")
public class BPMNProcessProvider extends AbstractDataProvider implements ISortableDataProvider<BPMNProcess, String>, IFilterStateLocator {
	
	private static List<BPMNProcess> processes;
	private ISortState sortState = new SingleSortState();
	private BPMNProcessFilter processFilter = new BPMNProcessFilter();
	private List<BPMNProcess> selectedProcesses;
	
	public BPMNProcessProvider() {
		processes = filterBPMNProcesses(BPMNProcess.findAll(), processFilter);
		selectedProcesses = new ArrayList<BPMNProcess>();
	}
	
	@Override
	public void detach() {
//		Processs = null;
	}
	
	@Override
	public Iterator<? extends BPMNProcess> iterator(long first, long count) {
		List<BPMNProcess> data = processes;
		Collections.sort(data, new Comparator<BPMNProcess>() {
			public int compare(BPMNProcess e1, BPMNProcess e2) {
				return (new Integer(e1.getID()).compareTo(e2.getID()));
			}
		});
		return data.subList((int)first, (int)Math.min(first + count, data.size())).iterator();
	}

	private List<BPMNProcess> filterBPMNProcesses(List<BPMNProcess> processesToFilter, BPMNProcessFilter processFilter) {
		List<BPMNProcess> returnedProcesses = new ArrayList<BPMNProcess>();
		for(BPMNProcess process: processesToFilter){
			if(processFilter.match(process)){
				returnedProcesses.add(process);
			}
		}
		return returnedProcesses;
	}

	@Override
	public IModel<BPMNProcess> model(BPMNProcess BPMNProcess) {
		return Model.of(BPMNProcess);
	}

	@Override
	public long size() {
		return processes.size();
	}

	public static List<BPMNProcess> getBPMNProcesses() {
		return processes;
	}

	public static void setBPMNProcesses(List<BPMNProcess> processList) {
		processes = processList;
	}

	@Override
	public ISortState<String> getSortState() {
		return sortState;
	}

	@Override
	public Object getFilterState() {
		return processFilter;
	}

	@Override
	public void setFilterState(Object state) {
		this.processFilter = (BPMNProcessFilter) state;
	}

	public BPMNProcessFilter getBPMNProcessFilter() {
		return processFilter;
	}

	public void setBPMNProcessFilter(BPMNProcessFilter processFilter) {
		this.processFilter = processFilter;
		processes = filterBPMNProcesses(BPMNProcess.findAll(), processFilter);
	}
	
	@Override
	public void selectEntry(int entryId) {
		for (Iterator iter = processes.iterator(); iter.hasNext();) {
			BPMNProcess process = (BPMNProcess) iter.next();
			if(process.getID() == entryId) {
				selectedProcesses.add(process);
				return;
			}
		}
	}
	
	@Override
	public void deselectEntry(int entryId) {
		for (Iterator iter = processes.iterator(); iter.hasNext();) {
			BPMNProcess process = (BPMNProcess) iter.next();
			if(process.getID() == entryId) {
				selectedProcesses.remove(process);
				return;
			}
		}
	}
	
	@Override
	public boolean isEntrySelected(int entryId) {
		for(BPMNProcess process : selectedProcesses){
			if(process.getID() == entryId) {
				return true;
			}
		}
		return false;
	}

	public void deleteSelectedEntries() {
		for(BPMNProcess process : selectedProcesses){
			processes.remove(process);
			process.remove();
		}
	}
	
	public void selectAllEntries() {
		for(BPMNProcess bpmnProcess : processes){
			selectedProcesses.add(bpmnProcess);
		}
	}

	@Override
	public Object getEntry(int entryId) {
		for(BPMNProcess process : processes){
			if(process.getID() == entryId){
				return process;
			}
		}
		return null;
	}
}
