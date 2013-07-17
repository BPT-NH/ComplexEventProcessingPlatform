package sushi.application.pages.transformation;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * representation of a tree node
 *
 * @param <T> type of content to be stored
 */
public class TransformationTreeTableElement<T> implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private int ID;
	private T content;
	private TransformationTreeTableElement<T> parent;
	private ArrayList<TransformationTreeTableElement<T>> children = new ArrayList<TransformationTreeTableElement<T>>();
	private int probability;
	
	/**
	 * creates a root node
	 * 
	 * @param content the content to be stored in the new node
	 */
	public TransformationTreeTableElement(int ID, T content, int probability) {
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
	public TransformationTreeTableElement(TransformationTreeTableElement<T> parent, int ID, T content, int probability) {
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

	public TransformationTreeTableElement<T> getParent() {
		return parent;
	}

	public ArrayList<TransformationTreeTableElement<T>> getChildren() {
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

	public void setParent(TransformationTreeTableElement<T> parent) {
		this.parent = parent;
	}
}
