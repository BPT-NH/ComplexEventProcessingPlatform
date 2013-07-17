package sushi.application.pages.eventrepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.components.form.BlockingAjaxButton;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.table.SelectEntryPanel;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.eventrepository.eventtypeeditor.EventTypeEditor;
import sushi.application.pages.eventrepository.model.EventTypeFilter;
import sushi.application.pages.eventrepository.model.EventTypeProvider;
import sushi.event.SushiEventType;

/**
 * {@link Panel}, which shows the {@link SushiEventType}s stored in the database.
 */
public class EventTypePanel extends Panel {

	private static final long serialVersionUID = 1L;
	private List<IColumn<SushiEventType, String>> columns;
	private EventTypeFilter eventTypeFilter;
	private EventTypeProvider eventTypeProvider;
	private DefaultDataTable<SushiEventType, String> dataTable;

	/**
	 * Constructor for the event type panel. 
	 * The page is initialized in this method and the data is loaded from the database.
	 * @param id
	 * @param abstractSushiPage
	 */
	public EventTypePanel(String id, final AbstractSushiPage abstractSushiPage) {
		super(id);
		
		eventTypeProvider = new EventTypeProvider();
		eventTypeFilter = new EventTypeFilter();
		eventTypeProvider.setEventTypeFilter(eventTypeFilter);
		
		Form<Void> buttonForm = new WarnOnExitForm("buttonForm");
		
		List<String> eventTypeFilterCriteriaList = new ArrayList<String>(Arrays.asList(new String[] { "ID", "Name"}));
		String selectedEventCriteria = "ID";

		final DropDownChoice<String> eventTypeFilterCriteriaSelect = new DropDownChoice<String>("eventTypeFilterCriteria", new Model<String>(selectedEventCriteria), eventTypeFilterCriteriaList);
		buttonForm.add(eventTypeFilterCriteriaSelect);
		
		List<String> conditions = new ArrayList<String>(Arrays.asList(new String[] { "<", "=", ">" }));
		String selectedCondition = "=";

		final DropDownChoice<String> eventFilterConditionSelect = new DropDownChoice<String>("eventTypeFilterCondition", new Model<String>(selectedCondition), conditions);
		buttonForm.add(eventFilterConditionSelect);
		
		final TextField<String> searchValueInput = new TextField<String>("searchValueInput", Model.of(""));
		buttonForm.add(searchValueInput);
		
		AjaxButton filterButton = new AjaxButton("filterButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				String eventFilterCriteria = eventTypeFilterCriteriaSelect.getChoices().get(Integer.parseInt(eventTypeFilterCriteriaSelect.getValue()));
				String eventFilterCondition = eventFilterConditionSelect.getChoices().get(Integer.parseInt(eventFilterConditionSelect.getValue()));
				String filterValue = searchValueInput.getValue();
				eventTypeProvider.setEventTypeFilter(new EventTypeFilter(eventFilterCriteria,eventFilterCondition, filterValue));
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(filterButton);
	    
	    AjaxButton resetButton = new AjaxButton("resetButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				eventTypeProvider.setEventTypeFilter(new EventTypeFilter());
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(resetButton);
	    
	   AjaxButton deleteButton = new BlockingAjaxButton("deleteButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				eventTypeProvider.deleteSelectedEntries();
				target.add(dataTable);
			}
	    };
	    buttonForm.add(deleteButton);
	    
	    AjaxButton selectAllButton = new AjaxButton("selectAllButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				eventTypeProvider.selectAllEntries();
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(selectAllButton);
	    
	    Button createButton = new AjaxButton("createButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				setResponsePage(EventTypeEditor.class);
	        }
	    };
	    buttonForm.add(createButton);
	    add(buttonForm);

		columns = new ArrayList<IColumn<SushiEventType, String>>();
		columns.add(new PropertyColumn<SushiEventType, String>(Model.of("ID"), "ID"));
		columns.add(new PropertyColumn<SushiEventType, String>(Model.of("Name"), "typeName"));
		columns.add(new PropertyColumn<SushiEventType, String>(Model.of("Timestamp"), "timestampName"));
		columns.add(new PropertyColumn<SushiEventType, String>(Model.of("Attributes"), "valueTypes"));
		columns.add(new AbstractColumn<SushiEventType, String>(new Model("Select")) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((SushiEventType) rowModel.getObject()).getID();
				cellItem.add(new SelectEntryPanel(componentId, entryId, eventTypeProvider));
			}
		});

		dataTable = new DefaultDataTable<SushiEventType, String>("eventtypes", columns, new EventTypeProvider(), 20);
		dataTable.setOutputMarkupId(true);

		add(dataTable);
	}
}
