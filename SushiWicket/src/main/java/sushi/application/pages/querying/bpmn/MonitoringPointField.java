package sushi.application.pages.querying.bpmn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.application.pages.querying.bpmn.model.BPMNTreeTableElement;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;
import sushi.event.SushiEventType;

public class MonitoringPointField implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private AjaxButton deleteButton;
	private Component addButton;
	private List<String> eventTypeNamesList;
	private BPMNTreeTableElement treeTableElement;
	private AbstractBPMNElement bpmnElement;
	private String enableEventTypeName;
	private String beginEventTypeName;
	private String terminateEventTypeName;
	private String skipEventTypeName;
	
	public MonitoringPointField(BPMNTreeTableElement treeTableElement){
		this.treeTableElement = treeTableElement;
		this.bpmnElement = treeTableElement.getContent();
		eventTypeNamesList = new ArrayList<String>();
		for (SushiEventType eventType : SushiEventType.findAll()) {
			eventTypeNamesList.add(eventType.getTypeName());
		}
	}

	public void addMonitoringField(Form<Void> monitoringForm, final MonitoringPointStateTransition monitoringPointType) {
		final MonitoringPoint monitoringPoint = treeTableElement.getMonitoringPoint(monitoringPointType);
		SushiEventType monitoringPointEventType = null;
		if(monitoringPoint != null){
			monitoringPointEventType = monitoringPoint.getEventType();
			updateEventTypeNames(monitoringPointType, monitoringPointEventType);
		}
		 //Div-Container für Label und Select
	    final WebMarkupContainer selectContainer = new WebMarkupContainer(monitoringPointType.getName() + "SelectContainer");
	    selectContainer.setOutputMarkupPlaceholderTag(true);
	    monitoringForm.add(selectContainer);
		
		//EnableButton
		addButton = new AjaxButton(monitoringPointType.getName() + "Button", monitoringForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				//MonitoringPoint hinzufügen
				selectContainer.setVisible(true);
				deleteButton.setVisible(true);
				addButton.setVisible(false);
				target.add(selectContainer);
				target.add(deleteButton);
				target.add(addButton);
	        }
	    };
	    addButton.setOutputMarkupPlaceholderTag(true);
	    monitoringForm.add(addButton);
	    
	    //Select
	    final DropDownChoice<String> eventTypeSelect;
		if(monitoringPointEventType != null){
			eventTypeSelect = new DropDownChoice<String>(monitoringPointType.getName() + "Select", new PropertyModel<String>(this, monitoringPointType.getName() + "EventTypeName"), eventTypeNamesList);
		} else {
			eventTypeSelect = new DropDownChoice<String>(monitoringPointType.getName() + "Select", new Model<String>(), eventTypeNamesList);
		}
		
		eventTypeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 
			
			private static final long serialVersionUID = 1L;

			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				final String eventTypeName = eventTypeSelect.getChoices().get(Integer.parseInt(eventTypeSelect.getValue()));
				if(eventTypeName != null && !eventTypeName.isEmpty()){
					SushiEventType selectedEventType = SushiEventType.findByTypeName(eventTypeName);
					if(selectedEventType != null){
						//Da wegen dem ComponentBuilder nicht auf dem originalen BPMN-Process aus der Datenbank gearbeitet wird, 
						//muss der noch geholt werden
						BPMNProcess originalProcess = BPMNProcess.findByContainedElement(bpmnElement);
						AbstractBPMNElement originalBPMNElement = originalProcess.getBPMNElementById(bpmnElement.getId());
						if(originalBPMNElement != null){
							MonitoringPoint originalMonitoringPoint = originalBPMNElement.getMonitoringPointByStateTransitionType(monitoringPointType);
							if(originalMonitoringPoint == null){
								originalMonitoringPoint = new MonitoringPoint(selectedEventType, monitoringPointType, null);
								originalMonitoringPoint.save();
							}
							originalBPMNElement.addMonitoringPoint(originalMonitoringPoint);
							originalBPMNElement.merge();
							updateEventTypeNames(monitoringPointType, selectedEventType);
						}
						
					}
				}
			}
		});
		eventTypeSelect.setOutputMarkupPlaceholderTag(true);
		selectContainer.add(eventTypeSelect);
		
		//Delete-Button
		deleteButton = new AjaxButton(monitoringPointType.getName() + "DeleteButton", monitoringForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				//MonitoringPoint entfernen
				//Da wegen dem ComponentBuilder nicht auf dem originalen BPMN-Process aus der Datenbank gearbeitet wird, 
				//muss der noch geholt werden
				BPMNProcess originalProcess = BPMNProcess.findByContainedElement(bpmnElement);
				AbstractBPMNElement originalBPMNElement = originalProcess.getBPMNElementById(bpmnElement.getId());
				if(originalBPMNElement != null){
					MonitoringPoint originalMonitoringPoint = originalBPMNElement.getMonitoringPointByStateTransitionType(monitoringPointType);
					originalBPMNElement.removeMonitoringPoint(originalMonitoringPoint);
					originalBPMNElement.save();
					updateEventTypeNames(monitoringPointType, null);
				}
				selectContainer.setVisible(false);
				addButton.setVisible(true);
				deleteButton.setVisible(false);
				target.add(selectContainer);
				target.add(addButton);
				target.add(deleteButton);
	        }
	    };
	    deleteButton.setOutputMarkupPlaceholderTag(true);
	    monitoringForm.add(deleteButton);
	    
	    if(monitoringPoint != null){
	    	addButton.setVisible(false);
	    } else {
	    	selectContainer.setVisible(false);
	    	deleteButton.setVisible(false);
	    }
	}

	private void updateEventTypeNames(final MonitoringPointStateTransition monitoringPointType, SushiEventType monitoringPointEventType) {
		String eventTypeName = (monitoringPointEventType != null) ? monitoringPointEventType.getTypeName() : null;
		switch(monitoringPointType){
		case begin:
			beginEventTypeName = eventTypeName;
			break;
		case enable:
			enableEventTypeName = eventTypeName;
			break;
		case skip:
			skipEventTypeName = eventTypeName;
			break;
		case terminate:
			terminateEventTypeName = eventTypeName;
			break;
		default:
			break;
		}
	}

}
