package sushi.application.pages.aggregation;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.components.form.BootstrapModal;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.aggregation.patternbuilder.externalknowledge.ExternalKnowledgeModal;
import sushi.application.pages.eventrepository.eventtypeeditor.EventTypeEditor;
import de.agilecoders.wicket.markup.html.bootstrap.tabs.BootstrapTabbedPanel;

public class AggregationPage extends AbstractSushiPage {
	
	private static final long serialVersionUID = 1L;
	private AggregationPage aggregationPage;
	private ExternalKnowledgeModal externalKnowledgeModal;

	public AggregationPage() {
		super();
		this.aggregationPage = this;
		
		buildOpenEventTypeEditorButton();
		
		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new Model<String>("Rule Editor")) {
			@Override
			public Panel getPanel(String panelId) {
				return new AggregationRuleEditorPanel(panelId, aggregationPage);
			}
		});
		tabs.add(new AbstractTab(new Model<String>("Advanced Rule Editor")) {
			@Override
			public Panel getPanel(String panelId) {
				return new AdvancedRuleEditorPanel(panelId, aggregationPage);
			}
		});
		
		add(new BootstrapTabbedPanel<ITab>("tabs", tabs));
		
		externalKnowledgeModal = new ExternalKnowledgeModal("externalKnowledgeModal");
		externalKnowledgeModal.setOutputMarkupId(true);
		add(externalKnowledgeModal);
	}

	private void buildOpenEventTypeEditorButton() {
		Button openEventTypeEditorButton = new Button("openEventTypeEditorButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() { 
				setResponsePage(EventTypeEditor.class);
			}
		};
		add(openEventTypeEditorButton);
	}

	public ExternalKnowledgeModal getExternalKnowledgeModal() {
		return externalKnowledgeModal;
	}

	public void setExternalKnowledgeModal(
			ExternalKnowledgeModal externalKnowledgeModal) {
		this.externalKnowledgeModal = externalKnowledgeModal;
	}
}
