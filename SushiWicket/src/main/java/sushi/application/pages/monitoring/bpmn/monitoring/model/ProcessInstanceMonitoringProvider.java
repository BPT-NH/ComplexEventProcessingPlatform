package sushi.application.pages.monitoring.bpmn.monitoring.model;

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
import sushi.monitoring.bpmn.BPMNQueryMonitor;
import sushi.monitoring.bpmn.ProcessInstanceMonitor;
import sushi.process.SushiProcess;

/**
 * @author micha
 */
public class ProcessInstanceMonitoringProvider extends AbstractDataProvider implements ISortableDataProvider<ProcessInstanceMonitor, String>, IFilterStateLocator {
	
	private static final long serialVersionUID = 1L;
	private static List<ProcessInstanceMonitor> processInstanceMonitors;
	private static List<ProcessInstanceMonitor> selectedProcessInstanceMonitors;
	private ISortState sortState = new SingleSortState();
	private ProcessInstanceMonitoringFilter processInstanceMonitorFilter = new ProcessInstanceMonitoringFilter();
	private SushiProcess process;
	
	public ProcessInstanceMonitoringProvider(SushiProcess process) {
		this.process = process;
		processInstanceMonitors = filterProcessInstanceMonitors(BPMNQueryMonitor.getInstance().getProcessInstanceMonitors(process), processInstanceMonitorFilter);
		selectedProcessInstanceMonitors = new ArrayList<ProcessInstanceMonitor>();
	}
	
	@Override
	public void detach() {
//		events = null;
	}
	
	@Override
	public Iterator<? extends ProcessInstanceMonitor> iterator(long first, long count) {
		List<ProcessInstanceMonitor> data = processInstanceMonitors;
		Collections.sort(data, new Comparator<ProcessInstanceMonitor>() {
			public int compare(ProcessInstanceMonitor e1, ProcessInstanceMonitor e2) {
				return (new Integer(e1.getID()).compareTo(e2.getID()));
			}
		});
		return data.subList((int)first, (int)Math.min(first + count, data.size())).iterator();
	}

	private List<ProcessInstanceMonitor> filterProcessInstanceMonitors(List<ProcessInstanceMonitor> processInstanceMonitorsToFilter, ProcessInstanceMonitoringFilter processInstanceMonitorFilter) {
		List<ProcessInstanceMonitor> returnedProcessInstanceMonitors = new ArrayList<ProcessInstanceMonitor>();
		if(processInstanceMonitorsToFilter != null){
			for(ProcessInstanceMonitor processInstanceMonitor: processInstanceMonitorsToFilter){
				if(processInstanceMonitorFilter.match(processInstanceMonitor)){
					returnedProcessInstanceMonitors.add(processInstanceMonitor);
				}
			}
		}
		return returnedProcessInstanceMonitors;
	}

	@Override
	public IModel<ProcessInstanceMonitor> model(ProcessInstanceMonitor processInstanceMonitor) {
		return Model.of(processInstanceMonitor);
	}

	@Override
	public long size() {
		return processInstanceMonitors.size();
	}

	public List<ProcessInstanceMonitor> getProcessInstanceMonitors() {
		return processInstanceMonitors;
	}
	
	public List<ProcessInstanceMonitor> getSelectedProcessInstanceMonitors(){
		return selectedProcessInstanceMonitors;
	}

	public static void setProcessInstanceMonitors(List<ProcessInstanceMonitor> eventList) {
		processInstanceMonitors = eventList;
	}

	@Override
	public ISortState<String> getSortState() {
		return sortState;
	}

	@Override
	public Object getFilterState() {
		return processInstanceMonitorFilter;
	}

	@Override
	public void setFilterState(Object state) {
		this.processInstanceMonitorFilter = (ProcessInstanceMonitoringFilter) state;
	}

	public ProcessInstanceMonitoringFilter getProcessInstanceMonitorFilter() {
		return processInstanceMonitorFilter;
	}

	public void setProcessInstanceMonitorFilter(ProcessInstanceMonitoringFilter processInstanceMonitorFilter) {
		this.processInstanceMonitorFilter = processInstanceMonitorFilter;
		processInstanceMonitors = filterProcessInstanceMonitors(BPMNQueryMonitor.getInstance().getProcessInstanceMonitors(process), processInstanceMonitorFilter);
	}
	
	@Override
	public void selectEntry(int entryId) {
		for (Iterator iter = processInstanceMonitors.iterator(); iter.hasNext();) {
			ProcessInstanceMonitor processInstanceMonitor = (ProcessInstanceMonitor) iter.next();
			if(processInstanceMonitor.getID() == entryId) {
				selectedProcessInstanceMonitors.add(processInstanceMonitor);
				return;
			}
		}
	}
	
	@Override
	public void deselectEntry(int entryId) {
		for (Iterator<ProcessInstanceMonitor> iter = processInstanceMonitors.iterator(); iter.hasNext();) {
			ProcessInstanceMonitor processInstanceMonitor = (ProcessInstanceMonitor) iter.next();
			if(processInstanceMonitor.getID() == entryId) {
				selectedProcessInstanceMonitors.remove(processInstanceMonitor);
				return;
			}
		}
	}
	
	@Override
	public boolean isEntrySelected(int entryId) {
		for (ProcessInstanceMonitor processInstanceMonitor : selectedProcessInstanceMonitors) {
			if(processInstanceMonitor.getID() == entryId) {
				return true;
			}
		}
		return false;
	}

	public void selectAllEntries() {
		for(ProcessInstanceMonitor processInstanceMonitor : processInstanceMonitors){
			selectedProcessInstanceMonitors.add(processInstanceMonitor);
		}
	}

	public SushiProcess getProcess() {
		return process;
	}

	public void setProcess(SushiProcess process) {
		this.process = process;
		processInstanceMonitors = filterProcessInstanceMonitors(BPMNQueryMonitor.getInstance().getProcessInstanceMonitors(process), processInstanceMonitorFilter);
		selectedProcessInstanceMonitors = new ArrayList<ProcessInstanceMonitor>();
	}

	@Override
	public Object getEntry(int entryId) {
		for (ProcessInstanceMonitor processInstanceMonitor : processInstanceMonitors) {
			if(processInstanceMonitor.getID() == entryId) {
				return processInstanceMonitor;
			}
		}
		return null;
	}

}
