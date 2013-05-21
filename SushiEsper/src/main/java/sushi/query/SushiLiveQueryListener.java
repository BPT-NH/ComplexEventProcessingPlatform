package sushi.query;

import java.util.List;

import sushi.event.SushiEvent;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

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