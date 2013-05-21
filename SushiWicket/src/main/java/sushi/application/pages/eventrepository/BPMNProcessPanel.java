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
import sushi.application.pages.eventrepository.model.BPMNProcessFilter;
import sushi.application.pages.eventrepository.model.BPMNProcessProvider;
import sushi.bpmn.element.BPMNProcess;

/**
 * Panel representing the content panel for the process tab.
 */
public class BPMNProcessPanel extends Panel {

	private List<IColumn<BPMNProcess, String>> columns;
	private BPMNProcessFilter bpmnProcessFilter;
	private BPMNProcessProvider bpmnProcessProvider;
	private DefaultDataTable<BPMNProcess, String> dataTable;

	public BPMNProcessPanel(String id, final AbstractSushiPage abstractSushiPage) {
		super(id);
		
		bpmnProcessProvider = new BPMNProcessProvider();
		bpmnProcessFilter = new BPMNProcessFilter();
		bpmnProcessProvider.setBPMNProcessFilter(bpmnProcessFilter);
		
		Form<Void> buttonForm = new WarnOnExitForm("buttonForm");
		
		List<String> processFilterCriteriaList = new ArrayList<String>(Arrays.asList(new String[] { "ID", "Name"}));
		String selectedEventCriteria = "ID";

		final DropDownChoice<String> eventTypeFilterCriteriaSelect = new DropDownChoice<String>("processFilterCriteria", new Model<String>(selectedEventCriteria), processFilterCriteriaList);
		buttonForm.add(eventTypeFilterCriteriaSelect);
		
		List<String> conditions = new ArrayList<String>(Arrays.asList(new String[] { "<", "=", ">" }));
		String selectedCondition = "=";

		final DropDownChoice<String> eventFilterConditionSelect = new DropDownChoice<String>("processFilterCondition", new Model<String>(selectedCondition), conditions);
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
				bpmnProcessProvider.setBPMNProcessFilter(new BPMNProcessFilter(eventFilterCriteria,eventFilterCondition, filterValue));
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(filterButton);
	    
	    AjaxButton resetButton = new AjaxButton("resetButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				bpmnProcessProvider.setBPMNProcessFilter(new BPMNProcessFilter());
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(resetButton);
	    
	    AjaxButton deleteButton = new BlockingAjaxButton("deleteButton", buttonForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);;
				bpmnProcessProvider.deleteSelectedEntries();
				target.add(dataTable);
			}
	    };
	    
	    buttonForm.add(deleteButton);
	    
	    AjaxButton selectAllButton = new AjaxButton("selectAllButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				bpmnProcessProvider.selectAllEntries();
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(selectAllButton);
	    
	    add(buttonForm);
	    
	    Button createButton = new Button("linkButton") {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit() {
//				setResponsePage(ProcessEditor.class);
	        }
	    };
	    buttonForm.add(createButton);
	    add(buttonForm);

		columns = new ArrayList<IColumn<BPMNProcess, String>>();
		columns.add(new PropertyColumn<BPMNProcess, String>(Model.of("ID"), "ID"));
		columns.add(new PropertyColumn<BPMNProcess, String>(Model.of("Name"), "name"));
		columns.add(new AbstractColumn<BPMNProcess, String>(new Model("Select")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((BPMNProcess) rowModel.getObject()).getID();
				cellItem.add(new SelectEntryPanel(componentId, entryId, bpmnProcessProvider));
			}
		});

		dataTable = new DefaultDataTable<BPMNProcess, String>("processes", columns, new BPMNProcessProvider(), 20);
		dataTable.setOutputMarkupId(true);

		add(dataTable);
	}
};
