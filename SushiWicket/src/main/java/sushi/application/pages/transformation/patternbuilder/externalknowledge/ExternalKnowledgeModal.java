package sushi.application.pages.transformation.patternbuilder.externalknowledge;

import java.util.Map;

import sushi.application.components.form.BootstrapModal;
import sushi.event.SushiEvent;
import sushi.event.attribute.SushiAttribute;
import sushi.transformation.collection.SushiPatternTree;

public class ExternalKnowledgeModal extends BootstrapModal {

	private static final long serialVersionUID = -9020117235863750792L;
	
	private ExternalKnowledgePanel panel;
    
    public ExternalKnowledgeModal(String id) {
    	super(id, "External Knowledge Usage");
    	panel = new ExternalKnowledgePanel("externalKnowledgePanel");
    	panel.setOutputMarkupId(true);
    	add(panel);
    }

    public ExternalKnowledgePanel getPanel() {
		return panel;
	}

	public void setPanel(ExternalKnowledgePanel panel) {
		this.panel = panel;
	}

    
}
