package sushi.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.event.SushiEvent;
import sushi.monitoring.QueryMonitoringPoint;
import sushi.notification.SushiNotificationForQuery;
import sushi.notification.SushiNotificationRuleForQuery;
import sushi.process.SushiProcessInstance;
import sushi.util.SetUtil;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.xml.XMLEventBean;

public class SushiLiveQueryListener implements UpdateListener {
	
	private List<String> log;
	protected SushiQuery query;
	
	public SushiLiveQueryListener(SushiQuery liveQuery) {
		this.query = liveQuery;  
		//call UpdateListener?
	}

	public void update(EventBean[] newData, EventBean[] oldData) {
		if(newData[0].getUnderlying() instanceof SushiEvent){
			SushiEvent event = (SushiEvent) newData[0].getUnderlying();
		}
		System.out.println("Event received: " + newData[0].getUnderlying());
		query.addEntryToLog("Event received: " + newData[0].getUnderlying());
		//trigger notification
		for (SushiNotificationRuleForQuery notificationRule : query.findNotificationForQuery()) {
			notificationRule.trigger(newData[0].getUnderlying().toString());
		}
		//trigger monitoringPoints
		List<SushiProcessInstance> instances = getInstances(newData); 
		for (QueryMonitoringPoint point : QueryMonitoringPoint.findByQuery(query)){
			for (SushiProcessInstance instance : instances)	point.trigger(instance);
		}
	}
	
	private List<SushiProcessInstance> getInstances(EventBean[] newData) {
		List<SushiProcessInstance> instances = new ArrayList<SushiProcessInstance>();
		Set<Integer> processInstances = new HashSet<Integer>();
		List<Set<Integer>> processInstancesList = new ArrayList<Set<Integer>>();
		if(newData[0].getUnderlying() instanceof HashMap){
			//ProcessInstance fuer Events ermitteln
			HashMap event = (HashMap) newData[0].getUnderlying();
			for(Object value : event.values()){
				if(value instanceof XMLEventBean){
					XMLEventBean bean = (XMLEventBean) value;
					if(bean.get("ProcessInstances") != null){
						processInstancesList.add(new HashSet<Integer>((List<Integer>) bean.get("ProcessInstances")));
					}
				}
			}
			processInstances = SetUtil.intersection(processInstancesList);
		}
		if(newData[0] instanceof XMLEventBean){
			XMLEventBean bean = (XMLEventBean) newData[0];
			if(bean.get("ProcessInstances") != null){
				processInstancesList.add(new HashSet<Integer>((List<Integer>) bean.get("ProcessInstances")));
			}
			processInstances = SetUtil.intersection(processInstancesList);
		}
		for (int instanceID : processInstances) {
			instances.add(SushiProcessInstance.findByID(instanceID));
		}
		return instances;
	}

	public String getLog() {
		StringBuffer logString = new StringBuffer();
		for (String string : query.getLog()) {
			logString.append(string + System.getProperty("line.separator"));
		}
		return logString.toString();
	}
	
	public int getNumberOfLogEntries() {
		return query.getLog().size();
	}
	
}