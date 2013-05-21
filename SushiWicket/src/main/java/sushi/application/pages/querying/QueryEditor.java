package sushi.application.pages.querying;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.io.IClusterable;

import sushi.application.SushiApplication;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.tree.SushiAttributeTreeExpansion;
import sushi.application.components.tree.SushiAttributeTreeExpansionModel;
import sushi.application.components.tree.SushiAttributeTreeProvider;
import sushi.application.components.tree.SushiLabelTree;
import sushi.application.pages.AbstractSushiPage;
import sushi.esper.SushiEsper;
import sushi.event.attribute.SushiAttribute;

public abstract class QueryEditor extends AbstractSushiPage {

	private static final long serialVersionUID = 1L;
	
	protected SushiEsper sushiEsper = ((SushiApplication) getApplication()).getSushiEsper();
	protected List<String> queryTitles;
	protected Form<Void> layoutForm;
	protected TextFieldDefaultValues textFieldDefaultValues;
	protected TextField<String> queryNameTextField;
	protected AjaxButton helpButton;
	protected ListChoice<String> queryListChoice;
	protected TextArea<String> queryTextArea;
	protected String helpText, query, selectedQueryTitle;
	protected String queryResult;
	protected TextArea<String> queryResultTextArea;
	protected static String lineBreak = System.getProperty("line.separator");
	private QueryEditorHelpModal helpModal;
	
	public QueryEditor() {
		super();
	}

	protected void buildMainLayout() {
		
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		
		textFieldDefaultValues = new TextFieldDefaultValues();
		setDefaultModel(new CompoundPropertyModel<TextFieldDefaultValues>(textFieldDefaultValues));
		
		queryNameTextField = new TextField<String>("queryNameTextField");
		queryNameTextField.setOutputMarkupId(true);
		layoutForm.add(queryNameTextField);

		// Create the modal window.
		helpModal = new QueryEditorHelpModal("helpModal", helpText);
		add(helpModal);

		helpButton = new AjaxButton("helpButton", layoutForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				helpModal.show(target);
	        }
			
		};
//		helpButton.setIcon(new ThemeResource("../sushitheme/icons/help.gif"));
		layoutForm.add(helpButton);
		
		queryListChoice = new ListChoice<String>("queryListChoice", new PropertyModel<String>(this, "selectedQueryTitle"), queryTitles){
			private static final long serialVersionUID = 1L;

			protected CharSequence getDefaultChoice(String selected){
				return "";
			}
		};
		queryListChoice.setOutputMarkupId(true);
		queryListChoice.setMaxRows(5);
		layoutForm.add(queryListChoice);
		
		queryTextArea = new TextArea<String>("queryTextArea", new PropertyModel<String>(this, "query"));
		queryTextArea.setOutputMarkupId(true);
		layoutForm.add(queryTextArea);
		
//		buildQueryCreatorTabs(layoutForm, rootNodeOfEventTypeTree);
		
		buildEventTypeTree();
		buildQueryResultTextArea();
	}
	
	private void buildEventTypeTree() {
		SushiLabelTree<SushiAttribute> eventTypeTree = new SushiLabelTree<SushiAttribute>("eventTypeTree", new SushiAttributeTreeProvider(generateNodesOfEventTypeTree()), new SushiAttributeTreeExpansionModel());
		SushiAttributeTreeExpansion.get().collapseAll();
		layoutForm.add(eventTypeTree);
	}

	protected abstract ArrayList<SushiAttribute> generateNodesOfEventTypeTree();

	private void buildQueryResultTextArea() {
		
		queryResultTextArea = new TextArea<String>("queryResultTextArea", new PropertyModel<String>(this, "queryResult"));
		queryResultTextArea.setOutputMarkupId(true);
		// readonly: <textarea readonly="readonly" ...>
		layoutForm.add(queryResultTextArea);
		
	}

	class TextFieldDefaultValues implements IClusterable {
		
		private static final long serialVersionUID = 1L;
		
        public String queryNameTextField;
        
        public String getQueryNameTextField() {
			return queryNameTextField;
		}

		public void setQueryNameTextField(String queryNameTextField) {
			this.queryNameTextField = queryNameTextField;
		}

		@Override
        public String toString()
        {
            return "queryNameTextField = '" + queryNameTextField + "'; queryTextArea = '" + queryTextArea + "'";
        }
    }
}
