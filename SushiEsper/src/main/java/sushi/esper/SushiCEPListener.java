package sushi.esper;

import sushi.event.SushiEvent;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class SushiCEPListener implements UpdateListener {
	
	public void update(EventBean[] newData, EventBean[] oldData) {
		if(newData[0].getUnderlying() instanceof SushiEvent){
			SushiEvent event = (SushiEvent) newData[0].getUnderlying();
		}
		System.out.println("Event received: " + newData[0].getUnderlying());
	}
	
}