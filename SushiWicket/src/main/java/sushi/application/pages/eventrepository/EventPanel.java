package sushi.application.pages.eventrepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
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
import sushi.application.pages.eventrepository.model.EventFilter;
import sushi.application.pages.eventrepository.model.EventProvider;
import sushi.event.SushiEvent;

/**
 * {@link Panel}, which shows the {@link SushiEvent}s stored in the database.
 */
public class EventPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private List<IColumn<SushiEvent, String>> columns;
	private DefaultDataTable<SushiEvent, String> dataTable;
	private EventFilter eventFilter;
	private EventProvider eventProvider;
	private EventRepository eventRepository;
	
	/**
	 * Constructor for the event panel. 
	 * The page is initialized in this method and the data is loaded from the database.
	 * @param id
	 * @param abstractSushiPage
	 */
	@SuppressWarnings("unchecked")
	public EventPanel(String id, final EventRepository abstractSushiPage) {
		super(id);
		
		eventProvider = new EventProvider();
		eventFilter = new EventFilter();
		eventProvider.setEventFilter(eventFilter);
		eventRepository = abstractSushiPage;
		
		Form<Void> buttonForm = new WarnOnExitForm("buttonForm");
		
		List<String> eventFilterCriteriaList = new ArrayList<String>(Arrays.asList(new String[] {"ID", "Event Type (ID)", "Process Instance"}));
		for(String eventAttribute : SushiEvent.findAllEventAttributes()){
			eventFilterCriteriaList.add(eventAttribute);
		}
		String selectedEventCriteria = "ID";

		final DropDownChoice<String> eventFilterCriteriaSelect = new DropDownChoice<String>("eventFilterCriteria", new Model<String>(selectedEventCriteria), eventFilterCriteriaList);
		buttonForm.add(eventFilterCriteriaSelect);
		
		List<String> conditions = new ArrayList<String>(Arrays.asList(new String[] { "<", "=", ">" }));
		String selectedCondition = "=";

		final DropDownChoice<String> eventFilterConditionSelect = new DropDownChoice<String>("eventFilterCondition", new Model<String>(selectedCondition), conditions);
		buttonForm.add(eventFilterConditionSelect);
		
		final TextField<String> searchValueInput = new TextField<String>("searchValueInput", Model.of(""));
		buttonForm.add(searchValueInput);
		
		AjaxButton filterButton = new AjaxButton("filterButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				String eventFilterCriteria = eventFilterCriteriaSelect.getChoices().get(Integer.parseInt(eventFilterCriteriaSelect.getValue()));
				String eventFilterCondition = eventFilterConditionSelect.getChoices().get(Integer.parseInt(eventFilterConditionSelect.getValue()));
				String filterValue = searchValueInput.getValue();
				eventProvider.setEventFilter(new EventFilter(eventFilterCriteria, eventFilterCondition, filterValue));
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(filterButton);
	    
	    AjaxButton resetButton = new BlockingAjaxButton("resetButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);;
				eventProvider.setEventFilter(new EventFilter());
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(resetButton);
	    
	    AjaxButton deleteButton = new BlockingAjaxButton("deleteButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);;
				eventProvider.deleteSelectedEntries();
				target.add(dataTable);
			}
	    };
	    buttonForm.add(deleteButton);
	    
	    AjaxButton selectAllButton = new AjaxButton("selectAllButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				eventProvider.selectAllEntries();
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(selectAllButton);
	    
	    add(buttonForm);
		
		columns = new ArrayList<IColumn<SushiEvent, String>>();
		columns.add(new PropertyColumn<SushiEvent, String>(Model.of("ID"), "ID"));
		columns.add(new PropertyColumn<SushiEvent, String>(Model.of("Timestamp"), "timestamp"));
		columns.add(new PropertyColumn<SushiEvent, String>(Model.of("EventType"), "eventType"));
		columns.add(new AbstractColumn<SushiEvent, String>(Model.of("Values"), "values") {
			@Override
			public void populateItem(final Item<ICellPopulator<SushiEvent>> item, final String componentId,
				final IModel<SushiEvent> rowModel)
			{
				String shortenedValues = ((SushiEvent) rowModel.getObject()).getValues().toString();
				if  (shortenedValues.length() > 200) {
					shortenedValues = shortenedValues.substring(0, 200) + "...";
				}
				Label label = new Label(componentId, shortenedValues);
				label.add(new AjaxEventBehavior("onclick") {
		             protected void onEvent(AjaxRequestTarget target) {
		                 //on click open Event View Modal
		            	 eventRepository.getEventViewModal().getPanel().setEvent(rowModel.getObject());
		            	 eventRepository.getEventViewModal().getPanel().detach();
		            	 target.add(eventRepository.getEventViewModal().getPanel());
		            	 eventRepository.getEventViewModal().show(target);
		             }
		         });
				item.add(label);
			}
		
		}
		);
		columns.add(new PropertyColumn<SushiEvent, String>(Model.of("Process Instances"), "processInstances"));
		columns.add(new AbstractColumn<SushiEvent, String>(new Model("Select")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((SushiEvent) rowModel.getObject()).getID();
				cellItem.add(new SelectEntryPanel(componentId, entryId, eventProvider));
			};			
		});

		dataTable = new DefaultDataTable<SushiEvent, String>("events", columns, eventProvider, 20);
		dataTable.setOutputMarkupId(true);
		
		add(dataTable);
				
	}
	
};
