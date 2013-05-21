package sushi.application.pages.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sushi.application.components.form.BlockingAjaxButton;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.main.MainPage;
import sushi.csv.importer.CSVNormalizer;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelNormalizer;
import sushi.excel.importer.FileNormalizer;
import sushi.excel.importer.SushiImportEvent;

public class ExcelEventTypeMatcher extends AbstractSushiPage {

	private static final long serialVersionUID = 1L;
	
	private List<String> columnTitles = new ArrayList<String>();
	private FileNormalizer fileNormalizer;
	private String filePath;
	private List<Map<String,String>> tableRows = new ArrayList<Map<String,String>>();
	private List<String> selectedColumnTitles = new ArrayList<String>();
	private List<SushiEventType> selectedEventTypes;
	private ListView<List<String>> headerContainer;
	private ListView<Map<String, String>> rowContainer;
	private WebMarkupContainer tableContainer;
	private CheckBoxMultipleChoice<SushiEventType> existingTypesCheckBoxMultipleChoice;
	private DropDownChoice<SushiEventType> eventTypeForPreviewDropDownChoice;
	private SushiEventType eventTypeForPreview;
	private Form<Void> layoutForm;

	public ExcelEventTypeMatcher(PageParameters parameters) {
		
		this.filePath = parameters.get("filePath").toString();
		int index = filePath.lastIndexOf('.');
		String fileExtension = filePath.substring(index + 1, filePath.length());
		if (fileExtension.contains("xls")) {
			fileNormalizer = new ExcelNormalizer();
		} else {
			fileNormalizer = new CSVNormalizer();
		}
		columnTitles = fileNormalizer.getColumnTitlesFromFile(filePath);
		if (!FileUploader.noEventTypesFound(parameters)) {
			buildMainLayout();
		}
	}

	private void buildMainLayout() {
		
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		// find matching event types
		String importTimeName = ExcelEventTypeCreator.GENERATED_TIMESTAMP_COLUMN_NAME;
		List<SushiEventType> eventTypes = (List<SushiEventType>) SushiEventType.findMatchingEventTypes(columnTitles, importTimeName);
		
		if (!eventTypes.isEmpty()) {
			selectedEventTypes = new ArrayList<SushiEventType>(Arrays.asList(eventTypes.get(0)));
			eventTypeForPreview = selectedEventTypes.get(0);
			selectedColumnTitles = eventTypeForPreview.getRootAttributeNames();
		}
		
		configurePreviewTable();
		
		Button openExcelEventTypeCreatorButton = new Button("openExcelEventTypeCreatorButton") {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit() { 
				PageParameters pageParameters = new PageParameters();
				pageParameters.add("filePath", filePath);
				setResponsePage(ExcelEventTypeCreator.class, pageParameters);
			}
		};
		
		layoutForm.add(openExcelEventTypeCreatorButton);
		
		existingTypesCheckBoxMultipleChoice = new CheckBoxMultipleChoice<SushiEventType>("existingTypesCheckBoxMultipleChoice", new PropertyModel<ArrayList<SushiEventType>>(this, "selectedEventTypes"), eventTypes);
		existingTypesCheckBoxMultipleChoice.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(eventTypeForPreviewDropDownChoice);
				if (!selectedEventTypes.isEmpty()) {
					eventTypeForPreview = selectedEventTypes.get(0);
					updatePreviewTable(target);
				}
			}
		});
		existingTypesCheckBoxMultipleChoice.setSuffix("");
		
		layoutForm.add(existingTypesCheckBoxMultipleChoice);
		
		eventTypeForPreviewDropDownChoice = new DropDownChoice<SushiEventType>("eventTypeForPreviewDropDownChoice", new PropertyModel<SushiEventType>(this, "eventTypeForPreview"), selectedEventTypes);

		eventTypeForPreviewDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updatePreviewTable(target);
			}
		});
		
		eventTypeForPreviewDropDownChoice.setOutputMarkupId(true);
		layoutForm.add(eventTypeForPreviewDropDownChoice);
		
		BlockingAjaxButton addToEventTypeButton = new BlockingAjaxButton("addToEventTypeButton", layoutForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				if (selectedEventTypes.isEmpty()) {
					getFeedbackPanel().error("Please select at least one event type!");
				} else {
					int eventsCount = 0;
					for (SushiEventType selectedEventType : selectedEventTypes) {
						SushiEventType eventType = selectedEventType;
						String timestamp = eventType.getTimestampName();
						List<SushiEvent> events = fileNormalizer.importEventsFromFile(filePath, eventType.getRootLevelValueTypes(), timestamp);
						for (SushiEvent event : events) {
							event.setEventType(selectedEventType);
						}
						Broker.send(events);
						eventsCount = events.size();
					}
					String selectedEventTypesString = selectedEventTypes.toString().substring(1, selectedEventTypes.toString().length()-1);
					PageParameters pageParameters = new PageParameters();
					pageParameters.add("successFeedback", eventsCount + " events have been added to " + selectedEventTypesString);
					setResponsePage(MainPage.class, pageParameters);
				}
			}
		};
		
		layoutForm.add(addToEventTypeButton);
	}
	
	private void updatePreviewTable(AjaxRequestTarget target) {
		selectedColumnTitles = eventTypeForPreview.getRootAttributeNames();
		target.add(tableContainer);
	}

	private void configurePreviewTable() {
		
		List<SushiImportEvent> events = fileNormalizer.importEventsForPreviewFromFile(filePath, columnTitles);
                
		for (SushiImportEvent event : events) {
			Map<String, String> eventValues = new HashMap<String, String>();
			if (event.getTimestamp() != null) {
				// TODO: another name for key required!!!
				eventValues.put(event.getExtractedTimestampName(), event.getTimestamp().toString());
			}
			Set<String> attributeNames = event.getValues().keySet(); 
			for (String attributeName : attributeNames) {
				String attributeValue = event.getValues().get(attributeName).toString();
				eventValues.put(attributeName, attributeValue);
			}
			eventValues.put(ExcelEventTypeCreator.GENERATED_TIMESTAMP_COLUMN_NAME, event.getImportTime().toString());
			tableRows.add(eventValues);
		}
		
        tableContainer = new WebMarkupContainer("tableContainer");
        tableContainer.setOutputMarkupId(true);
        
        List<String> allColumnTitles = new ArrayList<String>(columnTitles);
		allColumnTitles.add(ExcelEventTypeCreator.GENERATED_TIMESTAMP_COLUMN_NAME);
		List<List<String>> headers = new ArrayList<List<String>>();
		headers.add(allColumnTitles);
        
		headerContainer = new ListView<List<String>>("headerContainer", headers) {
			private static final long serialVersionUID = 3658948592812295572L;
			@Override
			protected void populateItem(ListItem<List<String>> item) {
				item.add(new ListView<String>("column", selectedColumnTitles) {
					private static final long serialVersionUID = -3627947713326647386L;
					@Override
					protected void populateItem(ListItem<String> item) {
						item.add(new Label("cell", item.getModelObject()));
					}
					
				});	
			}
		};
		headerContainer.setOutputMarkupId(true);
	    
		rowContainer = new ListView<Map<String, String>>("rowContainer", tableRows) {
			private static final long serialVersionUID = -3353890746328461012L;
			@Override
			protected void populateItem(ListItem<Map<String, String>> item) {
				final Map<String, String> row = item.getModelObject();
				item.add(new ListView<String>("column", selectedColumnTitles) {
					private static final long serialVersionUID = -6270375159398080371L;
					int i = 0;
					@Override
					protected void populateItem(ListItem<String> item) {
						item.add(new Label("cell", row.get(selectedColumnTitles.get(i++))));

					}
				});
			}
	    };
	    rowContainer.setOutputMarkupId(true);
	    
	    tableContainer.add(headerContainer);
        tableContainer.add(rowContainer);
        layoutForm.add(tableContainer);
	}
}
