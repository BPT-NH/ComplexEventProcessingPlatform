package sushi.application.pages.querying;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;

import sushi.application.components.form.BlockingAjaxButton;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;

import com.espertech.esper.client.EPException;

/**
 * This page enables the creation and modification of Live @see SushiQuery 
 */
public class LiveQueryEditor extends QueryEditor {

	private static final long serialVersionUID = 1L;
	private static final String LIVE_QUERY_HELP_TEXT = "Live Queries are either asked from the eventtypes" + lineBreak +
			"or defined by a pattern. " + lineBreak +
			lineBreak +
			"Example-Query:" + lineBreak +
			"SELECT ValueName, Timestamp" + lineBreak +
			"FROM EventType" + lineBreak +
			"WHERE eventType = 'ValueNameX'" + lineBreak +
			lineBreak +
			"Example-Query for Patterns:" + lineBreak +
			"SELECT A.Value1, B.Value2 Timestamp" + lineBreak +
			"FROM Pattern[ every A=EventType1 " + lineBreak +
			"-> B=EventType2(A.Value1 = B.Value1)]" + lineBreak +
			lineBreak +
			"Other useful constructs might be: and, or, not" + lineBreak;
	
	private BlockingAjaxButton showQueryLogButton;
	private AjaxButton editQueryButton, deleteQueryButton, saveQueryButton;

	public LiveQueryEditor() {
		
		super();
		helpText = LIVE_QUERY_HELP_TEXT;
		
		updateQueryListChoice();
		buildMainLayout();
		buildFinalLayout();
	}
	
	private void updateQueryListChoice() {
		queryTitles = sushiEsper.getAllLiveQueryTitles();
		if (!queryTitles.isEmpty()) {
			selectedQueryTitle = queryTitles.get(0);
		}
	}
	
	private void buildFinalLayout() {

		saveQueryButton = new AjaxButton("saveQueryButton") {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				try {
					String queryTitle = (String)queryNameTextField.getValue();
					if (queryTitle.isEmpty()) {
						getFeedbackPanel().error("Please specify a name for the query!");
						target.add(getFeedbackPanel());
						return;
					}
					SushiQuery liveQuery = SushiQuery.findQueryByTitle(queryTitle);
					if (liveQuery == null){
						liveQuery = new SushiQuery(queryTitle, query, SushiQueryTypeEnum.LIVE);
						sushiEsper.addLiveQuery(liveQuery);
						queryTitles.add(queryTitle);
					}
					liveQuery.setQueryString(query);
					liveQuery.save();
					//set input fields to null
					textFieldDefaultValues.setQueryNameTextField("");
					query = "";
					target.add(queryListChoice);
					target.add(queryNameTextField);
					target.add(queryTextArea);
				} catch (EPException e) {
					getFeedbackPanel().error(e.getMessage());
					target.add(getFeedbackPanel());
				}
			} 
		};
		layoutForm.add(saveQueryButton);
		
		editQueryButton = new AjaxButton("editQueryButton") {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				String queryTitle = selectedQueryTitle;
				textFieldDefaultValues.setQueryNameTextField(queryTitle);
				query = sushiEsper.getLiveQueryByTitle(queryTitle).getQueryString();
				updateQueryListChoice();
				target.add(queryNameTextField);
				target.add(queryTextArea);
				target.add(queryListChoice);
	        }
	    };
	    layoutForm.add(editQueryButton);
		
		deleteQueryButton = new AjaxButton("deleteQueryButton") {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				String queryTitle = selectedQueryTitle;
				if (!queryTitle.isEmpty()) {
					sushiEsper.removeLiveQuery((String) queryTitle);
					queryTitles.remove(queryTitle);
					target.add(queryListChoice);
				}
			}
		};
		layoutForm.add(deleteQueryButton);
		
		showQueryLogButton = new BlockingAjaxButton("showQueryLogButton", layoutForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				String queryTitle = selectedQueryTitle;
				queryResult = (sushiEsper.getListenerByName(queryTitle)).getLog();
				target.add(queryResultTextArea);
			}
		};
		layoutForm.add(showQueryLogButton);
	}
	
	/**
	 * creates a preview of the registered events and their attributes for easier query formulation
	 */
	@Override
	protected ArrayList<SushiAttribute> generateNodesOfEventTypeTree() {
		ArrayList<SushiAttribute> treeElements = new ArrayList<SushiAttribute>();
		List<SushiEventType> eventTypes = SushiEventType.findAll();
		for (SushiEventType eventType : eventTypes) { 
			SushiAttribute eventTypeRootElement = new SushiAttribute(eventType.getTypeName());
			treeElements.add(eventTypeRootElement);
			
			List<SushiAttribute> rootAttributes = eventType.getRootLevelValueTypes();
			for (SushiAttribute rootAttribute : rootAttributes) {
				rootAttribute.setParent(eventTypeRootElement);
			}
		}
		return treeElements;
	}
}
