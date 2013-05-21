package sushi.application.pages.simulator.model;

import java.io.Serializable;
import java.util.ArrayList;

import sushi.application.pages.simulator.DurationEntryPanel;
import sushi.bpmn.decomposition.Component;
import sushi.simulation.DerivationType;
import sushi.event.attribute.SushiAttribute;

/**
 * representation of a tree node
 *
 * @param <T> type of content to be stored
 */
public class SimulationTreeTableElement<T> implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private int ID;
	private T content;
	private SimulationTreeTableElement<T> parent;
	private ArrayList<SimulationTreeTableElement<T>> children = new ArrayList<SimulationTreeTableElement<T>>();
	private String probability;
	private String input;

	private DerivationType derivationType;
	private String derivation;

	private DurationEntryPanel durationEntryPanel;

	private String duration;
	
	public SimulationTreeTableElement(int ID, T content) {
		this(ID, content, "1");
	}
	
	/**
	 * creates a root node
	 * 
	 * @param content the content to be stored in the new node
	 */
	public SimulationTreeTableElement(int ID, T content, String probability) {
		this.ID = ID;
		this.content = content;
		this.setProbability(probability);
		this.setDerivationType(DerivationType.FIXED);
		this.setDuration("0");
		this.setDerivation("0");
	}
	
	/**
	 * creates a node and adds it to its parent
	 * 
	 * @param parent
	 * @param content the content to be stored in the node
	 */
	public SimulationTreeTableElement(SimulationTreeTableElement<T> parent, int ID, T content, String probability) {
		this(ID, content, probability);
		this.parent = parent;
		this.parent.getChildren().add(this);
	}

	public Integer getID() {
		return ID;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}

	public T getContent() {
		return content;
	}
	
	public boolean hasParent() {
		return parent != null;
	}

	public SimulationTreeTableElement<T> getParent() {
		return parent;
	}

	public ArrayList<SimulationTreeTableElement<T>> getChildren() {
		return children;
	}
	
	@Override
    public String toString() {
		if (content == null) {
			return new String();
		}
		return content.toString();
    }

	public void remove() {
		if(this.parent != null){
			this.parent.getChildren().remove(this);
		}
	}

	public void setParent(SimulationTreeTableElement<T> parent) {
		this.parent = parent;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public boolean editableColumnsVisible() {
		return content instanceof SushiAttribute;
	}
	
	public boolean canHaveSubElements() {
		return content instanceof Component;
	}
	
	public DerivationType getDerivationType(){
		return derivationType;
	}
	
	public void setDerivationType(DerivationType derivationType){
		this.derivationType = derivationType;
		if(durationEntryPanel != null){
			durationEntryPanel.setDerivationType(derivationType);
		}
	}

	public void setDerivation(String derivation) {
		this.derivation = derivation;
	}
	
	public String getDerivation() {
		return derivation;
	}

	public void setDurationEntryPanel(DurationEntryPanel durationEntryPanel) {
		this.durationEntryPanel = durationEntryPanel;
	}
	public String getDuration(){
		return duration;
	}
	public void setDuration(String duration){
		this.duration = duration;
	}
	public String getProbability() {
		return probability;
	}

	public void setProbability(String probability) {
		this.probability = probability;
	}
}
