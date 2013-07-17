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
import sushi.application.pages.eventrepository.model.ProcessFilter;
import sushi.application.pages.eventrepository.model.ProcessProvider;
import sushi.application.pages.eventrepository.processeditor.ProcessEditor;
import sushi.process.SushiProcess;

/**
 * {@link Panel}, which shows the {@link SushiProcess}es stored in the database.
 */
public class ProcessPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private List<IColumn<SushiProcess, String>> columns;
	private ProcessFilter processFilter;
	private ProcessProvider processProvider;
	private DefaultDataTable<SushiProcess, String> dataTable;

	/**
	 * Constructor for the process panel. 
	 * The page is initialized in this method and the data is loaded from the database.
	 * @param id
	 * @param abstractSushiPage
	 */
	public ProcessPanel(String id, final AbstractSushiPage abstractSushiPage) {
		super(id);
		
		processProvider = new ProcessProvider();
		processFilter = new ProcessFilter();
		processProvider.setProcessFilter(processFilter);
		
		Form<Void> buttonForm = new WarnOnExitForm("buttonForm");
		
		List<String> processFilterCriteriaList = new ArrayList<String>(Arrays.asList(new String[] { "ID", "Name", "Process Instance", "Correlation Attribute"}));
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
				String eventFilterCriteria = eventTypeFilterCriteriaSelect.getChoices().get(Integer.parseInt(eventTypeFilterCriteriaSelect.getValue()));
				String eventFilterCondition = eventFilterConditionSelect.getChoices().get(Integer.parseInt(eventFilterConditionSelect.getValue()));
				String filterValue = searchValueInput.getValue();
				processProvider.setProcessFilter(new ProcessFilter(eventFilterCriteria,eventFilterCondition, filterValue));
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(filterButton);
	    
	    AjaxButton resetButton = new AjaxButton("resetButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				processProvider.setProcessFilter(new ProcessFilter());
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(resetButton);
	    
	    AjaxButton deleteButton = new BlockingAjaxButton("deleteButton", buttonForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);;
				processProvider.deleteSelectedEntries();
				target.add(dataTable);
			}
	    };
	    buttonForm.add(deleteButton);
	    add(buttonForm);
	    
	    AjaxButton selectAllButton = new BlockingAjaxButton("selectAllButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);;
				processProvider.selectAllEntries();
				target.add(dataTable);
	        }
	    };
	    buttonForm.add(selectAllButton);
	    
	    Button createButton = new Button("createButton") {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit() {
				setResponsePage(ProcessEditor.class);
	        }
	    };
	    buttonForm.add(createButton);
	    add(buttonForm);

		columns = new ArrayList<IColumn<SushiProcess, String>>();
		columns.add(new PropertyColumn<SushiProcess, String>(Model.of("ID"), "ID"));
		columns.add(new PropertyColumn<SushiProcess, String>(Model.of("Name"), "name"));
		columns.add(new PropertyColumn<SushiProcess, String>(Model.of("Process Instances"), "processInstances"));
		columns.add(new AbstractColumn<SushiProcess, String>(new Model("Select")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((SushiProcess) rowModel.getObject()).getID();
				cellItem.add(new SelectEntryPanel(componentId, entryId, processProvider));
			}
		});

		dataTable = new DefaultDataTable<SushiProcess, String>("processes", columns, new ProcessProvider(), 20);
		dataTable.setOutputMarkupId(true);

		add(dataTable);
	}
};
