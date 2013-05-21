package sushi.application.pages.aggregation;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * representation of a tree node
 *
 * @param <T> type of content to be stored
 */
public class AggregationTreeTableElement<T> implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private int ID;
	private T content;
	private AggregationTreeTableElement<T> parent;
	private ArrayList<AggregationTreeTableElement<T>> children = new ArrayList<AggregationTreeTableElement<T>>();
	private int probability;
	
	/**
	 * creates a root node
	 * 
	 * @param content the content to be stored in the new node
	 */
	public AggregationTreeTableElement(int ID, T content, int probability) {
		this.ID = ID;
		this.content = content;
		this.probability = probability;
	}
	
	/**
	 * creates a node and adds it to its parent
	 * 
	 * @param parent
	 * @param content the content to be stored in the node
	 */
	public AggregationTreeTableElement(AggregationTreeTableElement<T> parent, int ID, T content, int probability) {
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

	public AggregationTreeTableElement<T> getParent() {
		return parent;
	}

	public ArrayList<AggregationTreeTableElement<T>> getChildren() {
		return children;
	}
	
	@Override
    public String toString() {
		if (content == null) {
			return new String();
		}
		return content.toString();
    }

	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}

	public void remove() {
		if(this.parent != null){
			this.parent.getChildren().remove(this);
		}
		//Müssen Kinder noch explizit entfernt werden?
	}

	public void setParent(AggregationTreeTableElement<T> parent) {
		this.parent = parent;
	}
}
