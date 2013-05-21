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
import sushi.application.pages.eventrepository.model.ProcessInstanceFilter;
import sushi.application.pages.eventrepository.model.ProcessInstanceProvider;
import sushi.process.SushiProcessInstance;

/**
 * Panel representing the content panel for the process instance tab.
 */
public class ProcessInstancePanel extends Panel {

	private List<IColumn<SushiProcessInstance, String>> columns;
	private ProcessInstanceFilter processInstanceFilter;
	private ProcessInstanceProvider processInstanceProvider;
	private DefaultDataTable<SushiProcessInstance, String> dataTable;

	public ProcessInstancePanel(String id, final AbstractSushiPage abstractSushiPage) {
		super(id);
		
		processInstanceProvider = new ProcessInstanceProvider();
		processInstanceFilter = new ProcessInstanceFilter();
		processInstanceProvider.setProcessInstanceFilter(processInstanceFilter);
		
		Form<Void> buttonForm = new WarnOnExitForm("buttonForm");
		
		List<String> processFilterCriteriaList = new ArrayList<String>(Arrays.asList(new String[] { "ID", "Process", "Process (ID)"}));
		String selectedEventCriteria = "ID";

		final DropDownChoice<String> eventTypeFilterCriteriaSelect = new DropDownChoice<String>("processInstanceFilterCriteria", new Model<String>(selectedEventCriteria), processFilterCriteriaList);
		buttonForm.add(eventTypeFilterCriteriaSelect);
		
		List<String> conditions = new ArrayList<String>(Arrays.asList(new String[] { "<", "=", ">" }));
		String selectedCondition = "=";

		final DropDownChoice<String> eventFilterConditionSelect = new DropDownChoice<String>("processInstanceFilterCondition", new Model<String>(selectedCondition), conditions);
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
				processInstanceProvider.setProcessInstanceFilter(new ProcessInstanceFilter(eventFilterCriteria,eventFilterCondition, filterValue));
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(filterButton);
	    
	    AjaxButton resetButton = new BlockingAjaxButton("resetButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				processInstanceProvider.setProcessInstanceFilter(new ProcessInstanceFilter());
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(resetButton);
	    
	    AjaxButton deleteButton = new BlockingAjaxButton("deleteButton", buttonForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				processInstanceProvider.deleteSelectedEntries();
				target.add(dataTable);
			}
	    };
	    
	    buttonForm.add(deleteButton);
	    
	    AjaxButton selectAllButton = new AjaxButton("selectAllButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				processInstanceProvider.selectAllEntries();
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(selectAllButton);
	    
	    add(buttonForm);

	    columns = new ArrayList<IColumn<SushiProcessInstance, String>>();
		columns.add(new PropertyColumn<SushiProcessInstance, String>(Model.of("ID"), "ID"));
		columns.add(new PropertyColumn<SushiProcessInstance, String>(Model.of("Process"), "process"));
		columns.add(new PropertyColumn<SushiProcessInstance, String>(Model.of("Correlation Attributes"), "correlationAttributesAndValues"));
		columns.add(new PropertyColumn<SushiProcessInstance, String>(Model.of("Timer Event"), "timerEvent"));
		columns.add(new AbstractColumn<SushiProcessInstance, String>(new Model("Select")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((SushiProcessInstance) rowModel.getObject()).getID();
				cellItem.add(new SelectEntryPanel(componentId, entryId, processInstanceProvider));
			}
		});

		dataTable = new DefaultDataTable<SushiProcessInstance, String>("processInstances", columns, new ProcessInstanceProvider(), 20);
		dataTable.setOutputMarkupId(true);

		add(dataTable);
	}
};
