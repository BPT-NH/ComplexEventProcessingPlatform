package sushi.application.pages.eventrepository.eventview;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import sushi.application.components.tree.TreeExpansion;
import sushi.application.components.tree.TreeExpansionModel;
import sushi.application.components.tree.TreeProvider;
import sushi.application.components.tree.SushiLabelTree;
import sushi.event.SushiEvent;
import sushi.event.collection.SushiMapElement;
import sushi.event.collection.SushiTreeElement;

/**
 * This panel displays an event with its key information and its attributes in a tree structure.
 */
public class EventViewPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private SushiEvent event;
	private SushiLabelTree<SushiTreeElement> tree;
	private Label label;
	private Label timestamp;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	public EventViewPanel(String id, SushiEvent event) {
    	super(id);
    	setEvent(event);
    }
    
    public EventViewPanel(String id) {
    	super(id);
	   
    	//add eventtype
    	label = new Label("eventType");
    	label.setOutputMarkupId(true);
    	add(label);
    	
    	//timestamp
    	timestamp = new Label("timestamp");
    	timestamp.setOutputMarkupId(true);
    	add(timestamp);
    	
		// hierarchical display of attributes
		tree = new SushiLabelTree<SushiTreeElement>("treeTable", new TreeProvider(new ArrayList<SushiTreeElement<String>>(generateNodesOfEventTypeTree())), new TreeExpansionModel());
        TreeExpansion.get().expandAll();
        tree.setOutputMarkupId(true);
		add(tree);
	}

    public void setEvent(SushiEvent event) {
    	this.event = event;
	   
    	//add eventtype
    	label = new Label("eventType", event.getEventType().getTypeName());
    	label.setOutputMarkupId(true);
    	addOrReplace(label);
    	
    	//timestamp
    	timestamp = new Label("timestamp", dateFormatter.format(event.getTimestamp()));
    	timestamp.setOutputMarkupId(true);
    	addOrReplace(timestamp);
    	
		// hierarchical display of attributes
		tree = new SushiLabelTree<SushiTreeElement>("treeTable", new TreeProvider(generateNodesOfEventTypeTree()), new TreeExpansionModel());
        TreeExpansion.get().expandAll();
        tree.setOutputMarkupId(true);
		addOrReplace(tree);
	}

	protected ArrayList<SushiTreeElement<String>> generateNodesOfEventTypeTree() {
		ArrayList<SushiTreeElement<String>> treeElements = new ArrayList<SushiTreeElement<String>>();
		if (event != null) {
			List<SushiMapElement<String, Serializable>> firstLevelValues = event.getValues().getTreeRootElements();
			for (SushiMapElement<String, Serializable> firstLevelValue : firstLevelValues) {
				SushiTreeElement<String> rootElement;
				if (!firstLevelValue.hasChildren()) {
					rootElement = new SushiTreeElement<String>(firstLevelValue.getKey() + " : " + firstLevelValue.getValue());
					treeElements.add(rootElement);
				} else {
					rootElement = new SushiTreeElement<String>(firstLevelValue.getKey());
					treeElements.add(rootElement);
					fillTreeLevel(rootElement, firstLevelValue.getChildren());
				}
			}
		}
		return treeElements;
	}
	
	private void fillTreeLevel(SushiTreeElement<String> parent, List<SushiMapElement<String, Serializable>> children) {
		for (SushiMapElement<String, Serializable> newValue : children) {
			SushiTreeElement<String> newElement;
			if (!newValue.hasChildren()) {
			newElement = new SushiTreeElement<String>(parent, newValue.getKey() + " : " + newValue.getValue());
			} else {
				newElement = new SushiTreeElement<String>(parent, newValue.getKey());
				fillTreeLevel(newElement, newValue.getChildren());
			}
		}
	}

}
