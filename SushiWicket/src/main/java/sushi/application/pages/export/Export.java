package sushi.application.pages.export;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import sushi.application.components.table.SelectEntryPanel;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.eventrepository.model.EventFilter;
import sushi.application.pages.eventrepository.model.EventProvider;
import sushi.csv.importer.CSVExporter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;

public class Export extends AbstractSushiPage {
	
	private String selectedEventTypeName;
	private SushiEventType selectedEventType;
	private Form<Void> layoutForm;
	protected String eventTypeNameFromTree;
	private DropDownChoice<String> eventTypeDropDownChoice;
	private ArrayList<IColumn<SushiEvent, String>> columns;
	private DefaultDataTable<SushiEvent, String> dataTable;
	private EventProvider eventProvider;

	public Export() {
		super();
		
		layoutForm = new Form<Void>("layoutForm");
		add(layoutForm);
		
		eventProvider = new EventProvider();
		eventProvider.setEventFilter(new EventFilter("Event Type (ID)", "=", "-1"));
		
		addEventTypeSelect();
		addExportButton();
		addEventTable();
	}

	private void addEventTypeSelect() {
		List<String> eventTypes = SushiEventType.getAllTypeNames();
		eventTypeDropDownChoice = new DropDownChoice<String>("eventTypeDropDownChoice", new PropertyModel<String>(this, "selectedEventTypeName"), eventTypes);
		eventTypeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateOnChangeOfDropDownChoice(target);
			}
		});
		layoutForm.add(eventTypeDropDownChoice);
	}
	
	protected void updateOnChangeOfDropDownChoice(AjaxRequestTarget target) {
		selectedEventType = SushiEventType.findByTypeName(selectedEventTypeName);
		
		if (selectedEventType != null) {
			eventProvider.setEventFilter(new EventFilter("Event Type (ID)", "=", Integer.toString(selectedEventType.getID())));
			target.add(dataTable);
		} 
	}
	
	private void addExportButton() {
		Button filterButton = new Button("exportButton") {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit() {
				CSVExporter csvExporter = new CSVExporter();
				List<SushiEvent> events = (eventProvider.getSelectedEvents().isEmpty()) ? eventProvider.getEvents() : eventProvider.getSelectedEvents(); 
				File csv = csvExporter.generateExportFile(selectedEventType, events);
				
				IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(csv)); // Use whatever file you need here
				ResourceStreamRequestHandler resourceStreamRequestHandler = new ResourceStreamRequestHandler(resourceStream);
				resourceStreamRequestHandler.setFileName("exportCSV.csv");
				RequestCycle.get().scheduleRequestHandlerAfterCurrent(resourceStreamRequestHandler);
			}
				
	    };
	    layoutForm.add(filterButton);
	}
	
	private void addEventTable() {
		columns = new ArrayList<IColumn<SushiEvent, String>>();
		columns.add(new PropertyColumn<SushiEvent, String>(Model.of("ID"), "ID"));
		columns.add(new PropertyColumn<SushiEvent, String>(Model.of("Timestamp"), "timestamp"));
		columns.add(new PropertyColumn<SushiEvent, String>(Model.of("EventType"), "eventType"));
		columns.add(new PropertyColumn<SushiEvent, String>(Model.of("Values"), "values"));
		columns.add(new PropertyColumn<SushiEvent, String>(Model.of("Process Instances"), "processInstances"));
		
		columns.add(new AbstractColumn<SushiEvent, String>(new Model("Select")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((SushiEvent) rowModel.getObject()).getID();
				cellItem.add(new SelectEntryPanel(componentId, entryId, eventProvider));
			}
		});

		dataTable = new DefaultDataTable<SushiEvent, String>("events", columns, eventProvider, 20);
		dataTable.setOutputMarkupId(true);
		
		add(dataTable);
	}

}
