package sushi.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.AttachableElement;
import sushi.esper.SushiEsper;
import sushi.esper.SushiUtils;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.collection.SushiMapTree;
import sushi.monitoring.bpmn.BPMNQueryMonitor;
import sushi.process.SushiProcessInstance;
import sushi.util.SetUtil;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.event.xml.XMLEventBean;


/**
 * A listener for {@link SushiPatternQuery}.
 * @author micha
 */
public class SushiPatternQueryListener extends SushiLiveQueryListener implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<SushiEventType> loopBreakEventTypes;
	private boolean isLoopQueryListener = false;
	private List<Integer> alreadyTriggeredProcessInstances = new ArrayList<Integer>();
	private AbstractBPMNElement catchingElement = null;
	private AbstractBPMNElement timerElement = null;
	private float timeDuration = 0;
	private SushiEventType boundaryTimerEventType;
	
	public SushiPatternQueryListener(SushiQuery patternQuery) {
		super(patternQuery);
	}
	
	public SushiPatternQueryListener(SushiQuery patternQuery, List<SushiEventType> loopBreakEventTypes) {
		super(patternQuery);
		this.loopBreakEventTypes = loopBreakEventTypes;
		this.isLoopQueryListener = true;
	}
	
	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {
		if(newData[0].getUnderlying() instanceof HashMap){
			//ProcessInstance für PatternEvent ermitteln
			List<Set<Integer>> processInstancesList = new ArrayList<Set<Integer>>();
			HashMap patternEvent = (HashMap) newData[0].getUnderlying();
			for(Object value : patternEvent.values()){
				if(value instanceof XMLEventBean){
					XMLEventBean bean = (XMLEventBean) value;
					if(bean.get("ProcessInstances") != null){
						processInstancesList.add(new HashSet<Integer>((List<Integer>) bean.get("ProcessInstances")));
					}
				}
			}
			System.err.println(query.getTitle() + ": " + processInstancesList);
			Set<Integer> processInstances = SetUtil.intersection(processInstancesList);
			if(!processInstances.isEmpty()){
				/*TODO: processInstances.iterator().next() ist nicht ausreichend, hier würde nur die erste ProcessInstance berücksichtigt werden, 
				 * auch wenn das Event zu mehreren ProcessInstanzen gehört*/
				BPMNQueryMonitor.getInstance().setQueryFinishedForProcessInstance((SushiPatternQuery) query, SushiProcessInstance.findByID(processInstances.iterator().next()));
				//Neues Event erzeugen und an Esper schicken
				SushiEvent event = new SushiEvent(SushiEventType.findByTypeName(query.getTitle()), new Date());
				for(int processInstanceID : processInstances){
					event.addProcessInstance(SushiProcessInstance.findByID(processInstanceID));
				}
				if(event.getEventType() != null){
					SushiEsper.getInstance().addEvent(event);			
				}
				//Gefangene Events nochmal abschicken, die in anderer Query wieder gebraucht werden
				if(!SushiUtils.isIntersectionNotEmpty(alreadyTriggeredProcessInstances, new ArrayList<Integer>(processInstances))){ /*Wurde Event schon zum zweiten Mal abgeschickt? */
					SushiEvent lastEvent = getLastEvent(newData);
					if(lastEvent != null){
						//Schleife wurde getriggert
						if(isLoopQueryListener){
							resendLastEvent(lastEvent);
						}
						//Catching-Event wurde getriggert
						if(catchingElement != null && lastEvent.getEventType().getTypeName().equals(catchingElement.getName())){
							resendLastEvent(lastEvent);
						}
						//Timer-Event wurde getriggert
						if(timerElement != null && lastEvent.getEventType().getTypeName().equals(timerElement.getName())){
							TimerListener timerListener = new TimerListener(lastEvent, boundaryTimerEventType, timeDuration);
							timerListener.start();
						}
					}
				}
			}
		}
		System.out.println("Event received for query " + query.getTitle() + ": " + newData[0].getUnderlying());
	}
	
	private SushiEvent getLastEvent(EventBean[] newData){
		SushiEvent event = null;
		if(newData[0].getUnderlying() instanceof HashMap){
			HashMap patternEvent = (HashMap) newData[0].getUnderlying();
			for(Object value : patternEvent.values()){
				if(value instanceof XMLEventBean){
					XMLEventBean bean = (XMLEventBean) value;
					SushiEventType eventType = SushiEventType.findByTypeName(bean.getEventType().getName());
					
					SushiMapTree<String, Serializable> values = new SushiMapTree<String, Serializable>();
					for(SushiAttribute valueType : eventType.getValueTypes()){
						values.put(valueType.getName(), (Serializable) bean.get(valueType.getName()));
					}
					
					event = new SushiEvent(eventType, new Date(), values);
					List<Integer> processInstanceIDs = (List<Integer>) bean.get("ProcessInstances");
					alreadyTriggeredProcessInstances.addAll(processInstanceIDs);
					for(Integer processInstanceID : processInstanceIDs){
						event.addProcessInstance(SushiProcessInstance.findByID(processInstanceID));
					}
				}
			}
		}
		return event;
	}

	private void resendLastEvent(SushiEvent event) {
		//TODO: Nicht speichern, Event nur zu Esper senden
		SushiEsper.getInstance().addEvent(event);
	}

	public List<SushiEventType> getLoopBreakEventTypes() {
		return loopBreakEventTypes;
	}

	public void setLoopBreakEventTypes(List<SushiEventType> loopBreakEventTypes) {
		this.loopBreakEventTypes = loopBreakEventTypes;
		this.isLoopQueryListener = true;
	}

	public boolean isLoopQueryListener() {
		return isLoopQueryListener;
	}

	public void setLoopQueryListener(boolean isLoopQueryListener) {
		this.isLoopQueryListener = isLoopQueryListener;
	}

	public AbstractBPMNElement getCatchingElement() {
		return catchingElement;
	}

	public void setCatchingElement(AbstractBPMNElement catchingElement) {
		this.catchingElement = catchingElement;
	}
	
	public AbstractBPMNElement getTimerElement() {
		return timerElement;
	}

	public void setTimer(AttachableElement attachableElement, SushiEventType boundaryTimerEventType, float timeDuration) {
		this.timerElement = (AbstractBPMNElement) attachableElement;
		this.boundaryTimerEventType = boundaryTimerEventType;
		this.timeDuration  = timeDuration;
	}

}