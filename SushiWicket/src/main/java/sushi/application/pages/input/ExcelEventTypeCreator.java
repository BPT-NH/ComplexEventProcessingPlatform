package sushi.application.pages.input;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sushi.FileUtils;
import sushi.application.components.form.BlockingAjaxButton;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.table.AttributeTypeCheckBoxPanel;
import sushi.application.components.table.AttributeTypeDropDownChoicePanel;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.input.model.EventAttributeProvider;
import sushi.application.pages.main.MainPage;
import sushi.csv.importer.CSVNormalizer;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelNormalizer;
import sushi.excel.importer.FileNormalizer;
import sushi.excel.importer.SushiImportEvent;
import sushi.excel.importer.TimeStampNames;
import sushi.xml.importer.AbstractXMLParser;

public class ExcelEventTypeCreator extends AbstractSushiPage {

	private static final long serialVersionUID = 1L;

	public static String GENERATED_TIMESTAMP_COLUMN_NAME = AbstractXMLParser.GENERATED_TIMESTAMP_COLUMN_NAME;
	private List<IColumn<SushiAttribute, String>> attributeTableColumns;
	private DefaultDataTable<SushiAttribute, String> attributeTable;
	private EventAttributeProvider eventAttributeProvider;
	private ArrayList<String> columnTitles = new ArrayList<String>();
	private FileNormalizer fileNormalizer;
	private String filePath;
	private List<Map<String,String>> tableRows = new ArrayList<Map<String,String>>();
	private DropDownChoice<String> timestampDropDownChoice;
	private ListView<List<String>> headerContainer;
	private ListView<Map<String, String>> rowContainer;
	private WebMarkupContainer tableContainer;
	private ExcelEventTypeCreator excelEventTypeCreator;
	private Form<Void> layoutForm;
	private TextField<String> eventTypeNameInput;
	private String eventTypeName;
	private SushiAttributeTree eventTypeAttributesTree;
	private SushiAttribute timestampAttribute = new SushiAttribute();

	private AjaxCheckBox importTimeCheckBox;
	private boolean eventTypeUsingImportTime = false;

	private String timestampName;

	public ExcelEventTypeCreator(PageParameters parameters) {

		super();
		this.excelEventTypeCreator = this;
		this.filePath = parameters.get("filePath").toString();
		this.eventTypeName = FileUtils.getFileNameWithoutExtension(filePath);
		int index = filePath.lastIndexOf('.');
		String fileExtension = filePath.substring(index + 1, filePath.length());
		if(fileExtension.contains("xls")){
			fileNormalizer = new ExcelNormalizer();
		} else {
			fileNormalizer = new CSVNormalizer();
		}
		columnTitles = (ArrayList<String>) fileNormalizer.getColumnTitlesFromFile(filePath);

		buildMainlayout();
	}


	private void buildMainlayout() {
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);

		List<PropertyColumn> columns = new ArrayList<PropertyColumn>();
		for (String title : columnTitles) {
			columns.add(new PropertyColumn(new Model(title), title));
		}
		columns.add(new PropertyColumn(new Model(GENERATED_TIMESTAMP_COLUMN_NAME), GENERATED_TIMESTAMP_COLUMN_NAME));

		// text field with event type name
		eventTypeNameInput = new TextField<String>("eventTypeNameInput", new PropertyModel<String>(this, "eventTypeName"));
		eventTypeNameInput.setOutputMarkupId(true);
		layoutForm.add(eventTypeNameInput);
		
		List<SushiAttribute> attributes = new ArrayList<SushiAttribute>();
		List<String> attributeExpressions = new ArrayList<String>();
		for (String attributeName : columnTitles) {
			// TODO: only Date attributes should be added to timestamp dropdown choice (~= attributeExpressions)
			SushiAttribute attribute = new SushiAttribute(attributeName, SushiAttributeTypeEnum.STRING);
			attributes.add(attribute);
			attributeExpressions.add(attribute.getAttributeExpression());
		}
		
		// initialize attributes (value types) tree of event type
		eventTypeAttributesTree = new SushiAttributeTree(attributes);
		// initialize provider for event preview table
		eventAttributeProvider = new EventAttributeProvider(attributes, timestampName);
		
		// if there is no attribute that could be used as timestamp
		if (attributeExpressions.isEmpty()) {
			// use import time as timestamp
			timestampName = GENERATED_TIMESTAMP_COLUMN_NAME;
			eventTypeUsingImportTime = true;
		} else {
			// else pre-select a timestamp in the dropdown choice
			timestampName = setTimestampPreselection(attributes);
			timestampAttribute = eventTypeAttributesTree.getAttributeByExpression(timestampName);
			eventAttributeProvider.deselectEntry(timestampAttribute);
		}
		eventAttributeProvider.setTimestampName(timestampName);
		
		// render table with events and values
		configurePreviewTable();		
		
		importTimeCheckBox = new AjaxCheckBox("importTimeCheckBox", new PropertyModel<Boolean>(this, "eventTypeUsingImportTime")) {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
                if (eventTypeUsingImportTime) {
                	timestampName = GENERATED_TIMESTAMP_COLUMN_NAME;
                } else {
                	timestampName = timestampAttribute.getName();
                }
                // disable timestamp dropdown choice if check box is checked
            	timestampDropDownChoice.setEnabled(!eventTypeUsingImportTime);
        		eventAttributeProvider.setTimestampName(timestampName);
                target.add(timestampDropDownChoice);
                target.add(attributeTable);
                target.add(tableContainer);
            }
        };
		
		timestampDropDownChoice = new DropDownChoice<String>("timestampDropDownChoice", new PropertyModel<String>(this, "timestampName"), attributeExpressions);
		timestampDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				timestampAttribute = eventTypeAttributesTree.getAttributeByExpression(timestampName);
				eventAttributeProvider.setTimestampName(timestampName);
				target.add(attributeTable);
				target.add(tableContainer);
			}
		});

		// disable components if there is no attribute that could be used as timestamp
		if (attributeExpressions.isEmpty()) {
        	importTimeCheckBox.setEnabled(false);
        	timestampDropDownChoice.setEnabled(false);
		}
        importTimeCheckBox.setOutputMarkupId(true);
        timestampDropDownChoice.setOutputMarkupId(true);
		layoutForm.add(importTimeCheckBox);
		layoutForm.add(timestampDropDownChoice);		
		
		attributeTableColumns = new ArrayList<IColumn<SushiAttribute, String>>();
		attributeTableColumns.add(new AbstractColumn<SushiAttribute, String>(new Model("Select")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				SushiAttribute attribute = ((SushiAttribute) rowModel.getObject());
				boolean checkBoxEnabled = true;
				// disable attribute checkbox if the attribute is selected in timestamp dropdown
				if (!eventTypeUsingImportTime && timestampAttribute != null && timestampAttribute.equals(attribute)) {
					checkBoxEnabled = false;
				}
				cellItem.add(new AttributeTypeCheckBoxPanel(componentId, attribute, checkBoxEnabled, eventAttributeProvider, tableContainer));
			}
		});
		attributeTableColumns.add(new PropertyColumn<SushiAttribute, String>(Model.of("Name"), "name"));
		attributeTableColumns.add(new AbstractColumn<SushiAttribute, String>(new Model("Type")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				SushiAttribute attribute = ((SushiAttribute) rowModel.getObject());
				boolean dropDownChoiceEnabled = true;
				// disable attribute type dropdown choice if the attribute is selected in timestamp dropdown
				if (!eventTypeUsingImportTime && timestampAttribute != null && timestampAttribute.equals(attribute)) {
					dropDownChoiceEnabled = false;
				}
				cellItem.add(new AttributeTypeDropDownChoicePanel(componentId, attribute, dropDownChoiceEnabled, eventAttributeProvider));
			}
		});
		
		attributeTable = new DefaultDataTable<SushiAttribute, String>("attributeTable", attributeTableColumns, eventAttributeProvider, 20);
		attributeTable.setOutputMarkupId(true);
		
		layoutForm.add(attributeTable);

		// confirm button
		BlockingAjaxButton confirmButton = new BlockingAjaxButton("confirmButton", layoutForm) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);

				if (eventTypeName.isEmpty()) {
					eventTypeName = FileUtils.getFileNameWithoutExtension(filePath); 
				}
				
				// check if event type with this name already exists
				if (SushiEventType.getAllTypeNames().contains(eventTypeName)) {
					// TODO: Ask the user if it shall be added to the existing event type with this name
					excelEventTypeCreator.getFeedbackPanel().error("Event type with this name already exists!");
					target.add(excelEventTypeCreator.getFeedbackPanel());
					return;
				}
				
				/* 
				 * remove attribute to be used as timestamp from attributes (value type) tree
				 * since the attribute is stored in the event type directly
				 */
				if (!eventTypeUsingImportTime) {
					timestampAttribute.removeAttribute();
					eventTypeAttributesTree.removeRoot(timestampAttribute);
				}
				
				// remove attributes that are not selected
				eventTypeAttributesTree.retainAllAttributes(eventAttributeProvider.getSelectedAttributes());
				SushiEventType eventType;
				try {
				eventType = new SushiEventType(eventTypeName, eventTypeAttributesTree, timestampName);
				Broker.send(eventType);
				} catch (RuntimeException e) {
					excelEventTypeCreator.getFeedbackPanel().error(e.getMessage());
					target.add(excelEventTypeCreator.getFeedbackPanel());
					return;
				}
				List<SushiEvent> events = fileNormalizer.importEventsFromFile(filePath, eventTypeAttributesTree.getRoots(), timestampName);
				if (eventTypeUsingImportTime) {
					for (SushiEvent actualEvent : events) {
						actualEvent.setTimestamp(new Date());
					}
				}
				for (SushiEvent event : events) {
					event.setEventType(eventType);
				}

				Broker.send(events);
				PageParameters pageParameters = new PageParameters();

				pageParameters.add("successFeedback", events.size() + " events have been added to " + eventTypeName);
				setResponsePage(MainPage.class, pageParameters);
			}
		};
		
		layoutForm.add(confirmButton);
	}
	

	private void configurePreviewTable() {

		List<SushiImportEvent> events = fileNormalizer.importEventsForPreviewFromFile(filePath, columnTitles);

		for (SushiImportEvent event : events) {
			Map<String, String> eventValues = new HashMap<String, String>();
			if (event.getTimestamp() != null) {
				eventValues.put(event.getExtractedTimestampName(), event.getTimestamp().toString());
			}
			for (String columnTitle : columnTitles) {
				if(!(TimeStampNames.contains(columnTitle) || columnTitle.equals(GENERATED_TIMESTAMP_COLUMN_NAME))){
					String actualEventValue = (String) event.getValues().get(columnTitle);
					if (actualEventValue != null) {
						eventValues.put(columnTitle, actualEventValue.toString());
					}
				}
			}
			eventValues.put(GENERATED_TIMESTAMP_COLUMN_NAME, event.getImportTime().toString());
			tableRows.add(eventValues);
		}

		// table

		tableContainer = new WebMarkupContainer("tableContainer");
		tableContainer.setOutputMarkupId(true);
		
		List<String> allColumnTitles = new ArrayList<String>(columnTitles);
		allColumnTitles.add(GENERATED_TIMESTAMP_COLUMN_NAME);
		List<List<String>> headers = new ArrayList<List<String>>();
		headers.add(allColumnTitles);
		
		headerContainer = new ListView<List<String>>("headerContainer", headers) {
			private static final long serialVersionUID = -5180277632835328415L;
			@Override
			protected void populateItem(ListItem<List<String>> item) {
				List<String> selectedColumnTitles = eventAttributeProvider.getSelectedColumnNames();
				item.add(new ListView<String>("column", selectedColumnTitles) {
					private static final long serialVersionUID = -6368677142275503560L;
					@Override
					protected void populateItem(ListItem<String> item) {
						item.add(new Label("cell", item.getModelObject()));
					}
					
				});	
			}
		};
		headerContainer.setOutputMarkupId(true);

		rowContainer = new ListView<Map<String, String>>("rowContainer", tableRows) {
			private static final long serialVersionUID = -3289707574304971363L;
			@Override
			protected void populateItem(ListItem<Map<String, String>> item) {
				final List<String> selectedColumnTitles = eventAttributeProvider.getSelectedColumnNames();
				final Map<String, String> row = item.getModelObject();
				item.add(new ListView<String>("column", selectedColumnTitles) {
					private static final long serialVersionUID = -2477176270802239757L;
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

	public String setTimestampPreselection(List<SushiAttribute> attributes) {
		SushiAttribute timestampAttribute = findBestTimestampMatch(attributes);
		if (timestampAttribute != null) {
			return timestampAttribute.getName();
		} else {
			return GENERATED_TIMESTAMP_COLUMN_NAME;
		}
	}

	public SushiAttribute findBestTimestampMatch(List<SushiAttribute> attributes) {
		for (SushiAttribute attribute : attributes) {
			String attributeName = attribute.getName().toLowerCase();
			Pattern regex = Pattern.compile("^time|time$|date|datum|uhrzeit|zeit$");
			Matcher match = regex.matcher(attributeName);
			if (match.find()) {
				return attribute; 
			}
		}
		return null;				
	}
}