package sushi.application.pages.simulator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.tree.ISortableTreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.application.pages.simulator.DurationEntryPanel;
import sushi.bpmn.decomposition.XORComponent;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiTree;
import sushi.simulation.DerivationType;

/**
 * wraps the given tree nodes
 *
 * @param <T>
 */
/**
 * @author micha
 *
 * @param <T>
 */
public class SimulationTreeTableProvider<T> extends AbstractDataProvider implements ISortableTreeProvider<SimulationTreeTableElement<T>, String> {

	private static final long serialVersionUID = 1L;
	private List<SimulationTreeTableElement<T>> treeTableElements;
	private List<SimulationTreeTableElement<T>> treeTableRootElements;
	private List<SimulationTreeTableElement<T>> selectedTreeTableElements = new ArrayList<SimulationTreeTableElement<T>>();
	private List<SushiAttribute> correlationAttributes;
	
	public SimulationTreeTableProvider() {
		this.treeTableElements = new ArrayList<SimulationTreeTableElement<T>>();
	}
	
	/**
	 * constructor
	 * 
	 * @param treeNodes root nodes of the tree, child nodes are accessed by this component automatically
	 */
	public SimulationTreeTableProvider(ArrayList<SimulationTreeTableElement<T>> treeNodes) {
		this.treeTableElements = treeNodes;
	}

	@Override
	public void detach() {
	}

	@Override
	public Iterator<? extends SimulationTreeTableElement<T>> getRoots() {
		return getRootElements().iterator();
	}
	
	private List<SimulationTreeTableElement<T>> getRootElements(){
		treeTableRootElements  = new ArrayList<SimulationTreeTableElement<T>>();
		for(SimulationTreeTableElement<T> element : treeTableElements){
			if(element.getParent() == null){
				treeTableRootElements.add(element);
			}
		}
		return treeTableRootElements;
	}

	@Override
	public boolean hasChildren(SimulationTreeTableElement<T> node) {
		return !node.getChildren().isEmpty();
	}

	@Override
	public Iterator<? extends SimulationTreeTableElement<T>> getChildren(SimulationTreeTableElement<T> node) {
		return node.getChildren().iterator();
	}

	@Override
	public SimulationTreeTableElementModel<T> model(SimulationTreeTableElement<T> node) {
		return new SimulationTreeTableElementModel<T>(getRootElements(), node);
	}

	@Override
	public ISortState<String> getSortState() {
		return new SingleSortState<String>();
	}

	@Override
	public void selectEntry(int entryId) {
		for (SimulationTreeTableElement<T> treeTableElement : treeTableElements) {
			if(treeTableElement.getID() == entryId) {
				selectedTreeTableElements.add(treeTableElement);
				return;
			}
		}
	}

	@Override
	public void deselectEntry(int entryId) {
		for (SimulationTreeTableElement<T> treeTableElement : treeTableElements) {
			if(treeTableElement.getID() == entryId) {
				selectedTreeTableElements.remove(treeTableElement);
				return;
			}
		}
	}

	@Override
	public boolean isEntrySelected(int entryId) {
		for (SimulationTreeTableElement<T> treeTableElement : selectedTreeTableElements) {
			if(treeTableElement.getID() == entryId) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Returns the next free ID for an new element.
	 * @return
	 */
	public int getNextID(){
		int highestNumber = 0;
		for(SimulationTreeTableElement<T> element : treeTableElements ){
			highestNumber = element.getID() > highestNumber ? element.getID() : highestNumber;
		}
		return ++highestNumber;
	}

	public List<SimulationTreeTableElement<T>> getTreeTableElements() {
		return treeTableElements;
	}
	
	public void addTreeTableElement(SimulationTreeTableElement<T> treeTableElement) {
		this.treeTableElements.add(treeTableElement);
		if(!selectedTreeTableElements.isEmpty()){
			SimulationTreeTableElement<T> parent = selectedTreeTableElements.get(0);
			parent.getChildren().add(treeTableElement);
			treeTableElement.setParent(parent);
		} 
	}
	
	public void addTreeTableElementWithParent(SimulationTreeTableElement<T> treeTableElement, SimulationTreeTableElement<T> parent) {
		this.treeTableElements.add(treeTableElement);
			parent.getChildren().add(treeTableElement);
			treeTableElement.setParent(parent);
	}

	public void setTreeTableElements(List<SimulationTreeTableElement<T>> treeTableElements) {
		this.treeTableElements = treeTableElements;
	}

	public void deleteSelectedEntries() {
		for(SimulationTreeTableElement<T> element : selectedTreeTableElements){
			element.remove();
		}
		treeTableElements.removeAll(selectedTreeTableElements);
		selectedTreeTableElements.clear();
	}

	public List<SimulationTreeTableElement<T>> getSelectedTreeTableElements() {
		return selectedTreeTableElements;
	}
	
	public List<SimulationTreeTableElement<T>> getRootTreeTableElements() {
		return getRootElements();
	}
	
	public SushiTree<T> getModelAsTree(){
		SushiTree<T> tree = new SushiTree<T>();
		for(SimulationTreeTableElement<T> element : treeTableRootElements){
			addElementToTree(null, element, tree);
		}
		return tree;
	}

	private void addElementToTree(SimulationTreeTableElement<T> parent, SimulationTreeTableElement<T> element, SushiTree<T> tree) {
		if(parent != null){
			tree.addChild(parent.getContent(), element.getContent());
		} else {
			tree.addChild(null, element.getContent());
		}
		if(element.getContent() instanceof SushiEventType){
			Map<SushiAttribute, String> attributeMap = new HashMap<SushiAttribute, String>();
			for(SimulationTreeTableElement<T> child : element.getChildren()){
				attributeMap.put((SushiAttribute) child.getContent(), (String) child.getInput());
			}
			tree.addChild(element.getContent(), (T) attributeMap);
		}
		else{
			for(SimulationTreeTableElement<T> child : element.getChildren()){
				addElementToTree(element, child, tree);
			}
		}
	}
	
	public String getInputForEntry(int entryID) {
		return getEntry(entryID).getInput();
	}

	public void setInputForEntry(String input, int entryID) {
			getEntry(entryID).setInput(input);
	}
	
	public String getDurationForEntry(int entryID) {
		return getEntry(entryID).getDuration();
	}

	public void setDurationForEntry(String duration, int entryID) {
			getEntry(entryID).setDuration(duration);
	}
	
	public void setDerivationTypeForEntry(DerivationType derivationType, int entryID) {
		getEntry(entryID).setDerivationType(derivationType);
	}
	
	public DerivationType getDerivationTypeForEntry(int entryID) {
		return getEntry(entryID).getDerivationType();
	}
	
	public void setDerivationForEntry(String derivation, int entryID) {
		getEntry(entryID).setDerivation(derivation);
	}
	
	public String getDerivationForEntry(int entryID) {
		return getEntry(entryID).getDerivation();
	}
	
	public boolean editableColumnsVisibleForEntry(int entryID){
		//TODO: rename!
		return getEntry(entryID).editableColumnsVisible();
	}

	public Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> getAttributeValuesFromModel() {
		Map<SushiEventType, Map<SushiAttribute, List<Serializable>>> eventTypeAttributes = new HashMap<SushiEventType, Map<SushiAttribute, List<Serializable>>>();
		for (SimulationTreeTableElement<T> treeTableElement : treeTableElements) {
			if(treeTableElement.getContent() instanceof SushiEventType){
				Map<SushiAttribute, List<Serializable>> attributes = new HashMap<SushiAttribute, List<Serializable>>();
				for(SimulationTreeTableElement<T> attributeElement :treeTableElement.getChildren()){
					attributes.put((SushiAttribute) attributeElement.getContent(), getValuesFromInput(attributeElement));
				}
				eventTypeAttributes.put((SushiEventType) treeTableElement.getContent(), attributes);
			}
		}
		return eventTypeAttributes;
	}

	private List<Serializable> getValuesFromInput(SimulationTreeTableElement<T> attributeElement) {
		List<Serializable> values = new ArrayList<Serializable>();
		String valueString = attributeElement.getInput();
		SushiAttribute attribute = (SushiAttribute) attributeElement.getContent();
		if(attribute.getType().equals(SushiAttributeTypeEnum.DATE)){
			//TODO: datum parsen
		}
		else if(attribute.getType().equals(SushiAttributeTypeEnum.INTEGER)){
			String[] split = valueString.split(",");
			for(int i = 0; i < split.length; i++){
				if(split[i].contains("-")){
					String[] subSplit = split[i].split("-");
					//falsche eingaben abfangen
					if(subSplit.length != 2){
						//TODO: was tun bei falscher eingabe?
					}
					else{
						for(Integer j = Integer.parseInt(subSplit[0].trim()); j <= Integer.parseInt(subSplit[1].trim()); j++){
							values.add(j);
						}
					}
				}
				else{
					values.add(Integer.parseInt(split[i].trim()));
				}
			}
		}
		else{
			String[] split = valueString.split(",");
			for(int i = 0; i < split.length; i++){
				values.add(split[i].trim());
			}
		}
		return values;
	}

	public void updateAllEqualInputFields(String input, int entryID) {
		SimulationTreeTableElement<T> sourceTreeTableElement = getEntry(entryID);
		if(!correlationAttributes.contains(sourceTreeTableElement.getContent())){
			return;
		}
		for (SimulationTreeTableElement<T> treeTableElement : treeTableElements) {
			if(treeTableElement.getContent().equals(sourceTreeTableElement.getContent())) {
				treeTableElement.setInput(input);
				Integer otherEntryId = treeTableElement.getID();
				setInputForEntry(input, otherEntryId);
			}
		}
		
	}

	public void setCorrelationAttributes(List<SushiAttribute> correlationAttributes) {
		this.correlationAttributes = correlationAttributes;
	}
	
	public void deleteAllEntries() {
		for(SimulationTreeTableElement<T> element : treeTableElements){
			element.remove();
		}
		selectedTreeTableElements.clear();
		treeTableElements.clear();
	}
	
	/**
	 * Checks if an element of the provider has empty input fields.
	 * @return
	 */
	public boolean hasEmptyFields(){
		for(SimulationTreeTableElement<T> element : treeTableElements){
			if(element.getContent() instanceof SushiAttribute){
				if(element.getInput() == null || element.getInput().isEmpty()){
					return true;
				}
			}
		}
		return false;
	}

	public Map<SushiEventType, String> getEventTypesWithDuration() {
		Map<SushiEventType, String> eventTypesWithDuration = new HashMap<SushiEventType, String>();
		for(SimulationTreeTableElement<T> element : treeTableElements){
			if(element.getContent() instanceof SushiEventType){
				eventTypesWithDuration.put((SushiEventType) element.getContent(), element.getDuration());
			}
		}
		return eventTypesWithDuration;
	}
	
	public Map<SushiEventType, String> getEventTypesWithDerivation() {
		Map<SushiEventType, String> eventTypesWithDuration = new HashMap<SushiEventType, String>();
		for(SimulationTreeTableElement<T> element : treeTableElements){
			if(element.getContent() instanceof SushiEventType){
				eventTypesWithDuration.put((SushiEventType) element.getContent(), element.getDerivation());
			}
		}
		return eventTypesWithDuration;
	}
	
	public Map<SushiEventType, DerivationType> getEventTypesWithDerivationType() {
		Map<SushiEventType, DerivationType> eventTypesWithDuration = new HashMap<SushiEventType, DerivationType>();
		for(SimulationTreeTableElement<T> element : treeTableElements){
			if(element.getContent() instanceof SushiEventType){
				eventTypesWithDuration.put((SushiEventType) element.getContent(), element.getDerivationType());
			}
		}
		return eventTypesWithDuration;
	}
	
	public Map<AbstractBPMNElement, String> getBPMNElementWithDuration() {
		Map<AbstractBPMNElement, String> taskssWithDuration = new HashMap<AbstractBPMNElement, String>();
		for(SimulationTreeTableElement<T> element : treeTableElements){
			if(element.getContent() instanceof AbstractBPMNElement){
				taskssWithDuration.put((AbstractBPMNElement) element.getContent(), element.getDuration());
			}
		}
		return taskssWithDuration;
	}
	
	@Override
	public SimulationTreeTableElement<T> getEntry(int entryId) {
		for(SimulationTreeTableElement<T> treeTableElement : treeTableElements){
			if(treeTableElement.getID() == entryId){
				return treeTableElement;
			}
		}
		return null;
	}

	public void registerDurationInputAtEntry(DurationEntryPanel durationEntryPanel, int entryId) {
				getEntry(entryId).setDurationEntryPanel(durationEntryPanel);
	}

	public Map<Object, String> getProbabilityStrings() {
		Map<Object, String> probabilityStrings = new HashMap<Object, String>();
		for(SimulationTreeTableElement<T> element : treeTableElements){
			if(element.getContent() instanceof XORComponent){
				for(SimulationTreeTableElement<T> child : element.getChildren()){
					probabilityStrings.put(child, child.getProbability());
				}
			}
		}
		return probabilityStrings;
	}

	public Map<AbstractBPMNElement, DerivationType> getBPMNElementWithDerivationType() {
		Map<AbstractBPMNElement, DerivationType> elementsWithDerivation = new HashMap<AbstractBPMNElement, DerivationType>();
		for(SimulationTreeTableElement<T> element : treeTableElements){
			if(element.getContent() instanceof AbstractBPMNElement){
				elementsWithDerivation.put((AbstractBPMNElement) element.getContent(), element.getDerivationType());
			}
		}
		return elementsWithDerivation;
	}

	public Map<AbstractBPMNElement, String> getBPMNElementWithDerivation() {
		Map<AbstractBPMNElement, String> elementWithDerivation = new HashMap<AbstractBPMNElement, String>();
		for(SimulationTreeTableElement<T> element : treeTableElements){
			if(element.getContent() instanceof AbstractBPMNElement){
				elementWithDerivation.put((AbstractBPMNElement) element.getContent(), element.getDerivation());
			}
		}
		return elementWithDerivation;
	}
}
