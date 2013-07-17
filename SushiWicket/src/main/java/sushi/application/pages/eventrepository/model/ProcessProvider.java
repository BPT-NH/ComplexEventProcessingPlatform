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
import sushi.process.SushiProcess;

/**
 * This class is the provider for {@link SushiProcess}es.
 * A filter can be specified to return only some processes.
 * @author micha
 */
@SuppressWarnings("serial")
public class ProcessProvider extends AbstractDataProvider implements ISortableDataProvider<SushiProcess, String>, IFilterStateLocator {
	
	private static List<SushiProcess> processes;
	private ISortState sortState = new SingleSortState();
	private ProcessFilter processFilter = new ProcessFilter();
	private List<SushiProcess> selectedProcesses;
	
	/**
	 * Constructor for providing {@link SushiProcess}es.
	 */
	public ProcessProvider() {
		processes = filterProcesses(SushiProcess.findAll(), processFilter);
		selectedProcesses = new ArrayList<SushiProcess>();
	}
	
	@Override
	public void detach() {
//		Processs = null;
	}
	
	@Override
	public Iterator<? extends SushiProcess> iterator(long first, long count) {
		List<SushiProcess> data = processes;
		Collections.sort(data, new Comparator<SushiProcess>() {
			public int compare(SushiProcess e1, SushiProcess e2) {
				return (new Integer(e1.getID()).compareTo(e2.getID()));
			}
		});
		return data.subList((int)first, (int)Math.min(first + count, data.size())).iterator();
	}

	private List<SushiProcess> filterProcesses(List<SushiProcess> processesToFilter, ProcessFilter processFilter) {
		List<SushiProcess> returnedProcesses = new ArrayList<SushiProcess>();
		for(SushiProcess process: processesToFilter){
			if(processFilter.match(process)){
				returnedProcesses.add(process);
			}
		}
		return returnedProcesses;
	}

	@Override
	public IModel<SushiProcess> model(SushiProcess Process) {
		return Model.of(Process);
	}

	@Override
	public long size() {
		return processes.size();
	}

	public static List<SushiProcess> getProcesses() {
		return processes;
	}

	public static void setProcesses(List<SushiProcess> processList) {
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
		this.processFilter = (ProcessFilter) state;
	}

	public ProcessFilter getProcessFilter() {
		return processFilter;
	}

	public void setProcessFilter(ProcessFilter processFilter) {
		this.processFilter = processFilter;
		processes = filterProcesses(SushiProcess.findAll(), processFilter);
	}
	
	@Override
	public void selectEntry(int entryId) {
		for (Iterator iter = processes.iterator(); iter.hasNext();) {
			SushiProcess process = (SushiProcess) iter.next();
			if(process.getID() == entryId) {
				selectedProcesses.add(process);
				return;
			}
		}
	}
	
	@Override
	public void deselectEntry(int entryId) {
		for (Iterator<SushiProcess> iter = processes.iterator(); iter.hasNext();) {
			SushiProcess process = (SushiProcess) iter.next();
			if(process.getID() == entryId) {
				selectedProcesses.remove(process);
				return;
			}
		}
	}
	
	@Override
	public boolean isEntrySelected(int entryId) {
		for(SushiProcess process : selectedProcesses){
			if(process.getID() == entryId) {
				return true;
			}
		}
		return false;
	}

	public void deleteSelectedEntries() {
		for(SushiProcess process : selectedProcesses){
			processes.remove(process);
			process.remove();
		}
	}
	
	public void selectAllEntries() {
		for(SushiProcess process : processes){
			selectedProcesses.add(process);
		}
	}
	
	@Override
	public Object getEntry(int entryId) {
		for(SushiProcess process : processes){
			if(process.getID() == entryId){
				return process;
			}
		}
		return null;
	}
}
