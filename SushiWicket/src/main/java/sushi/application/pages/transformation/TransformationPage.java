package sushi.application.pages.transformation;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.eventrepository.eventtypeeditor.EventTypeEditor;
import sushi.application.pages.transformation.patternbuilder.externalknowledge.ExternalKnowledgeModal;
import sushi.transformation.TransformationManager;
import de.agilecoders.wicket.markup.html.bootstrap.tabs.BootstrapTabbedPanel;

public class TransformationPage extends AbstractSushiPage {
	
	private static final long serialVersionUID = 1L;
	private TransformationManager transformationManager;
	private TransformationPage transformationPage;
	private ExternalKnowledgeModal externalKnowledgeModal;

	public TransformationPage() {
		super();
		this.transformationPage = this;
		this.transformationManager = TransformationManager.getInstance();
		
		buildOpenEventTypeEditorButton();
		
		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new Model<String>("Advanced Rule Editor")) {
			@Override
			public Panel getPanel(String panelId) {
				return new AdvancedTransformationRuleEditorPanel(panelId, transformationPage);
			}
		});
		tabs.add(new AbstractTab(new Model<String>("Basic Rule Editor")) {
			@Override
			public Panel getPanel(String panelId) {
				return new BasicTransformationRuleEditorPanel(panelId, transformationPage);
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
		Form<Void> layoutForm = new Form<Void>("layoutForm");
		add(layoutForm);
		layoutForm.add(openEventTypeEditorButton);
	}

	public TransformationManager getTransformationManager() {
		return transformationManager;
	}

	public void setTransformationManager(TransformationManager transformationManager) {
		this.transformationManager = transformationManager;
	}

	public ExternalKnowledgeModal getExternalKnowledgeModal() {
		return externalKnowledgeModal;
	}

	public void setExternalKnowledgeModal(
			ExternalKnowledgeModal externalKnowledgeModal) {
		this.externalKnowledgeModal = externalKnowledgeModal;
	}
}
