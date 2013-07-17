package sushi.application.pages.querying;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;

import sushi.application.components.form.BlockingAjaxButton;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.query.SushiQuery;
import sushi.query.SushiQueryTypeEnum;

import com.espertech.esper.client.EPStatementException;

/**
 * This page enables the creation and modification of On-Demand @see SushiQuery 
 */
public class OnDemandQueryEditor extends QueryEditor {

	private static final long serialVersionUID = 1L;
	private static final String ON_DEMAND_QUERY_HELP_TEXT = "On-Demand Queries are always asked from a Window"
			+ lineBreak
			+ lineBreak
			+ "Example-Query:"
			+ lineBreak
			+ "SELECT ValueName, Timestamp"
			+ lineBreak
			+ "FROM EventTypeWindow"
			+ lineBreak
			+ "WHERE ValueName = 'ValueX'";
	private BlockingAjaxButton executeQueryButton;
	private AjaxButton editQueryButton, deleteQueryButton, saveQueryButton;

	public OnDemandQueryEditor() {
		super();
		helpText = ON_DEMAND_QUERY_HELP_TEXT;

		updateQueryListChoice();
		buildMainLayout();
		buildFinalLayout();
	}

	private void updateQueryListChoice() {
		queryTitles = SushiQuery.getAllTitlesOfOnDemandQueries();
		if (!queryTitles.isEmpty()) {
			selectedQueryTitle = queryTitles.get(0);
		}
	}

	@SuppressWarnings("serial")
	private void buildFinalLayout() {

		saveQueryButton = new AjaxButton("saveQueryButton", layoutForm) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				try {
					String queryTitle = (String) queryNameTextField.getValue();
					if (queryTitle.isEmpty()) {
						getFeedbackPanel().error("Please specify a name for the query!");
						target.add(getFeedbackPanel());
						return;
					}
					
					SushiQuery newQuery = SushiQuery.findQueryByTitle(queryTitle);
					if (newQuery == null) {
						newQuery = new SushiQuery(queryTitle, query,
								SushiQueryTypeEnum.ONDEMAND);
						queryTitles.add(queryTitle);
					}

					newQuery.setQueryString(query);
					newQuery.validate();
					newQuery.save();
					textFieldDefaultValues.setQueryNameTextField("");
					query = "";
					target.add(queryListChoice);
					target.add(queryNameTextField);
					target.add(queryTextArea);
					target.add(getFeedbackPanel());
				} catch (EPStatementException e) {
					getFeedbackPanel().error(e.getMessage());
					target.add(getFeedbackPanel());
				}
			}
		};
		layoutForm.add(saveQueryButton);

		editQueryButton = new AjaxButton("editQueryButton", layoutForm) {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				String queryTitle = selectedQueryTitle;
				textFieldDefaultValues.setQueryNameTextField(queryTitle);
				query = SushiQuery.findQueryByTitle(queryTitle)
						.getQueryString();
				target.add(queryNameTextField);
				target.add(queryTextArea);
			}
		};
		layoutForm.add(editQueryButton);

		deleteQueryButton = new AjaxButton("deleteQueryButton", layoutForm) {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				;
				String queryTitle = selectedQueryTitle;
				if (!queryTitle.isEmpty()) {
					SushiQuery.removeQueryWithTitle(queryTitle);
					queryTitles.remove(queryTitle);
					target.add(queryListChoice);
				}
			}
		};
		layoutForm.add(deleteQueryButton);

		//executes the on-demand query on the events in the windows
		executeQueryButton = new BlockingAjaxButton("executeQueryButton",
				layoutForm) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				System.out.println("Number of Events in Runtime before Query: "
						+ sushiEsper.getEsperRuntime().getNumEventsEvaluated());
				String queryTitle = selectedQueryTitle;
				SushiQuery query = SushiQuery.findQueryByTitle(queryTitle);
				queryResult = query.execute();
				target.add(queryResultTextArea);
			}
		};
		layoutForm.add(executeQueryButton);
	}

	/**
	 * generates a preview of windows registered at esper for an easier query creation
	 */
	@Override
	protected ArrayList<SushiAttribute> generateNodesOfEventTypeTree() {
		ArrayList<SushiAttribute> treeElements = new ArrayList<SushiAttribute>();
		String[] windowNames = sushiEsper.getWindowNames();
		for (String windowName : windowNames) {
			SushiAttribute eventTypeRootElement = new SushiAttribute(windowName);
			treeElements.add(eventTypeRootElement);
			
			String eventTypeName = windowName.replace("Window", "");
			SushiEventType eventType = SushiEventType.findByTypeName(eventTypeName);
			List<SushiAttribute> rootAttributes = eventType.getRootLevelValueTypes();
			for (SushiAttribute rootAttribute : rootAttributes) {
				rootAttribute.setParent(eventTypeRootElement);
			}
		}
		return treeElements;

	}
}
