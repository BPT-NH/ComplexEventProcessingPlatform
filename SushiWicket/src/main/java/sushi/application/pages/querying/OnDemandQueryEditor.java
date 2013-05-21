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
	private Button executeQueryButton;
	private AjaxButton editQueryButton, deleteQueryButton, saveQueryButton;

	public OnDemandQueryEditor() {

		super();

		helpText = ON_DEMAND_QUERY_HELP_TEXT;

		updateQueryListChoice();
		buildMainLayout();
		buildFinalLayout();
	}

	private void updateQueryListChoice() {
		// queryTitles = sushiEsper.getAllOnDemandQueryTitles();
		queryTitles = SushiQuery.getAllTitlesOfOnDemandQueries();
		if (!queryTitles.isEmpty()) {
			selectedQueryTitle = queryTitles.get(0);
		}
	}

	private void buildFinalLayout() {

		saveQueryButton = new AjaxButton("saveQueryButton", layoutForm) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				try {
					String queryTitle = (String) queryNameTextField.getValue();
					if (queryTitle.isEmpty()) {
						getFeedbackPanel().error(
								"Please specify a name for the query!");
						target.add(getFeedbackPanel());
						return;
					}
					
					SushiQuery newQuery = SushiQuery
							.findQueryByTitle(queryTitle);
					if (newQuery == null) {
						newQuery = new SushiQuery(queryTitle, query,
								SushiQueryTypeEnum.ONDEMAND);
						queryTitles.add(queryTitle);
					}

					newQuery.setQueryString(query);
					newQuery.validate(sushiEsper.getEsperRuntime());
					newQuery.save();
					// queryTitles.add(queryTitle);
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

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				String queryTitle = selectedQueryTitle;
				textFieldDefaultValues.setQueryNameTextField(queryTitle);
				query = SushiQuery.findQueryByTitle(queryTitle)
						.getQueryString();
				// SushiQuery.removeQueryWithTitle(queryTitle);
				// updateQueryListChoice();
				target.add(queryNameTextField);
				target.add(queryTextArea);
				// target.add(queryListChoice);
			}
		};
		layoutForm.add(editQueryButton);

		deleteQueryButton = new AjaxButton("deleteQueryButton", layoutForm) {

			private static final long serialVersionUID = 1L;

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

		executeQueryButton = new BlockingAjaxButton("executeQueryButton",
				layoutForm) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("Number of Events in Runtime before Query: "
						+ sushiEsper.getEsperRuntime().getNumEventsEvaluated());
				String queryTitle = selectedQueryTitle;
				SushiQuery query = SushiQuery.findQueryByTitle(queryTitle);
				queryResult = query.execute(sushiEsper);
				target.add(queryResultTextArea);
			}
		};
		layoutForm.add(executeQueryButton);

	}

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
