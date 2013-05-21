package sushi.bpmn.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.bpmn.decomposition.Component;
import sushi.bpmn.monitoringpoint.MonitoringPoint;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;

/**
 * @author micha
 *
 */
@Entity
@Table(name = "BPMNProcess")
@Inheritance(strategy=InheritanceType.JOINED)
public class BPMNProcess extends AbstractBPMNElement {

	private static final long serialVersionUID = 1L;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private List<AbstractBPMNElement> BPMNElements = new ArrayList<AbstractBPMNElement>();

	public BPMNProcess() {
		super();
	}
	
	public BPMNProcess(String ID, String name, List<MonitoringPoint> monitoringPoints) {
		super(ID, name, monitoringPoints);
	}
	
	public boolean isProcess() {
		return true;
	}
	
	public void addBPMNElements(List<AbstractBPMNElement> elements) {
		BPMNElements.addAll(elements);
	}

	public void addBPMNElement(AbstractBPMNElement element) {
		if (element != null && !(BPMNElements.contains(element))) {
			BPMNElements.add(element);
		}
	}
	
	public void removeBPMNElements(Collection<AbstractBPMNElement> elements) {
		BPMNElements.removeAll(elements);
	}

	public void removeBPMNElement(AbstractBPMNElement element) {
		BPMNElements.remove(element);
	}

	public List<AbstractBPMNElement> getBPMNElements() {
		return BPMNElements;
	}
	
	public List<AbstractBPMNElement> getBPMNElementsWithOutSequenceFlows() {
		List<AbstractBPMNElement> elements = new ArrayList<AbstractBPMNElement>();
		for(AbstractBPMNElement element : BPMNElements){
			if(!(element instanceof BPMNSequenceFlow)){
				elements.add(element);
			}
		}
		return elements;
	}
	
	public List<BPMNTask> getAllTasks() {
		List<BPMNTask> elements = new ArrayList<BPMNTask>();
		for(AbstractBPMNElement element : this.getBPMNElementsWithOutSequenceFlows()){
			if(element instanceof BPMNTask){
				elements.add((BPMNTask) element);
			}
		}
		return elements;
	}
	
	public List<Component> getAllComponents() {
		List<Component> elements = new ArrayList<Component>();
		for(AbstractBPMNElement element : this.getBPMNElementsWithOutSequenceFlows()){
			if(element instanceof Component){
				elements.add((Component) element);
			}
		}
		return elements;
	}
	
	public ArrayList<String> getBPMNElementIDs() {
		ArrayList<String> ids = new ArrayList<String>();
		for (AbstractBPMNElement element : BPMNElements) {
			ids.add(element.getId());
		}
		return ids;
	}

	
	public AbstractBPMNElement getBPMNElementById(String ID) {
		for (AbstractBPMNElement element : BPMNElements) {
			if (element.getId().equals(ID)) {
				return element;
			}
		}
		return null;
	}
	
	public AbstractBPMNElement getBPMNElementByName(String name) {
		for (AbstractBPMNElement element : BPMNElements) {
			if (element.getName().equals(name)) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Returns the start event for a process.
	 * @return
	 */
	public BPMNStartEvent getStartEvent() {
		for (AbstractBPMNElement element : BPMNElements) {
			if (element instanceof BPMNStartEvent) {
				return (BPMNStartEvent) element;
			}
		}
		return null;
	}
	
	/**
	 * Returns all start events for a process.
	 * @return
	 */
	public List<BPMNStartEvent> getStartEvents() {
		List<BPMNStartEvent> startEvents = new ArrayList<BPMNStartEvent>();
		for (AbstractBPMNElement element : BPMNElements) {
			if (element instanceof BPMNStartEvent) {
				startEvents.add((BPMNStartEvent) element);
			}
		}
		return startEvents;
	}
	
	/**
	 * Returns the end event for a process.
	 * @return
	 */
	public BPMNEndEvent getEndEvent() {
		for (AbstractBPMNElement element : BPMNElements) {
			if (element instanceof BPMNEndEvent) {
				return (BPMNEndEvent) element;
			}
		}
		return null;
	}
	
	/**
	 * Returns all end events for a process.
	 * @return
	 */
	public List<BPMNEndEvent> getEndEvents() {
		List<BPMNEndEvent> endEvents = new ArrayList<BPMNEndEvent>();
		for (AbstractBPMNElement element : BPMNElements) {
			if (element instanceof BPMNEndEvent) {
				endEvents.add((BPMNEndEvent) element);
			}
		}
		return endEvents;
	}

	public AbstractBPMNElement getNextElementFor(AbstractBPMNElement element) {
		// lookForSequenceFlow
		for (AbstractBPMNElement flow : BPMNElements) {
			if (flow.isSequenceFlow()) {
				BPMNSequenceFlow seqflow = (BPMNSequenceFlow) flow;
				if (seqflow.getSourceRef().equals(element.getId())) {

					return getBPMNElementById(seqflow.getTargetRef());
				}
			}
		}
		// lookForEvents
		return getAttachedElementsFor(element);
	}

	public AbstractBPMNElement getAttachedElementsFor(AbstractBPMNElement element) {
		for (AbstractBPMNElement el : BPMNElements) {
			if (el.isBoundaryEvent()) {
				BPMNBoundaryEvent event = (BPMNBoundaryEvent) el;
				if (event.getAttachedToElement().getId().equals(element.getId())) {
					return event;
				}
			}
		}
		return null;
	}
	
	public String printProcess() {
		return printProcess(this);
	}
	
	/**
	 * This method returns a textual representation of all process elements and their monitoring points.
	 * @param process
	 * @return
	 */
	public String printProcess(BPMNProcess process) {
		StringBuffer output = new StringBuffer();
		Collection<AbstractBPMNElement> elements = new ArrayList<>();
		if(process.getStartEvent() != null){
			elements.add(process.getStartEvent());
			elements.addAll(process.getStartEvent().getIndirectSuccessors());
		} else {
			elements = process.getBPMNElementsWithOutSequenceFlows();
		}
		for(AbstractBPMNElement element : elements){
			output.append(element.toString());
			output.append(System.getProperty("line.separator"));
			for (MonitoringPoint monitoringPoint : element.getMonitoringPoints()) {
				output.append("[Monitoring Point] " + monitoringPoint.toString());
				output.append(System.getProperty("line.separator"));
			}
			if(element instanceof BPMNSubProcess){
				output.append(printProcess((BPMNSubProcess)element));
			}
		}
		return output.toString();
	}
	
	public static BPMNProcess findByID(int ID){
		List<BPMNProcess> list = BPMNProcess.findByAttribute("ID", new Integer(ID).toString());
		if(list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public static List<BPMNProcess> findByName(String name){
		Query query = Persistor.getEntityManager().createNativeQuery("" +
				"SELECT * " +
				"FROM BPMNElement " +
				"WHERE ID IN (" +
				"	SELECT RESULT.ID " +
				"	FROM (" +
				"		SELECT * " +
				"		FROM BPMNElement AS SELECTEDBPMNELEMENT " +
				"		WHERE ID IN (" +
				"			SELECT ID " +
				"			FROM BPMNProcess AS SELECTEDBPMNPROCESS)) AS RESULT" +
				"			WHERE RESULT.NAME ='" + name + "')", BPMNProcess.class);
		return query.getResultList();
	}
	
	public static List<BPMNProcess> findByAttribute(String columnName, String value){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM BPMNProcess WHERE " + columnName + " = '" + value + "'", BPMNProcess.class);
		return query.getResultList();
	}
	
	public static List<BPMNProcess> findAll() {
		Query q = Persistor.getEntityManager().createQuery("select t from BPMNProcess t");
		return q.getResultList();
	}

	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM BPMNProcess");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public List<AbstractBPMNGateway> getAllSplitGateways() {
		List<AbstractBPMNGateway> elements = new ArrayList<AbstractBPMNGateway>();
		for(AbstractBPMNElement element : this.getBPMNElementsWithOutSequenceFlows()){
			if(element instanceof AbstractBPMNGateway && ((AbstractBPMNGateway) element).isSplitGateway()){
				elements.add((AbstractBPMNGateway) element);
			}
		}
		return elements;
	}

	public List<AbstractBPMNGateway> getAllJoinGateways() {
		List<AbstractBPMNGateway> elements = new ArrayList<AbstractBPMNGateway>();
		for(AbstractBPMNElement element : this.getBPMNElementsWithOutSequenceFlows()){
			if(element instanceof AbstractBPMNGateway && ((AbstractBPMNGateway) element).isJoinGateway()){
				elements.add((AbstractBPMNGateway) element);
			}
		}
		return elements;
	}
	
	@Override
	public Persistable remove() {
		SushiProcess process = SushiProcess.findByBPMNProcess(this);
		if (process != null){
			process.setBpmnProcess(null);
			process.merge();
		}
		return super.remove();
	}

	public boolean hasSubProcesses() {
		for (AbstractBPMNElement element : BPMNElements) {
			if (element instanceof BPMNSubProcess) {
				return true;
			}
		}
		return false;
	}
	
	public List<BPMNSubProcess> getSubProcesses() {
		List<BPMNSubProcess> subProcesses = new ArrayList<BPMNSubProcess>();
		for (AbstractBPMNElement element : BPMNElements) {
			if (element instanceof BPMNSubProcess) {
				subProcesses.add((BPMNSubProcess) element);
			}
		}
		return subProcesses;
	}
	
	public List<AbstractBPMNElement> getSubElementsWithMonitoringpoints(){
		List<AbstractBPMNElement> subElementsWithMonitoringpoints = new ArrayList<AbstractBPMNElement>();
		for(AbstractBPMNElement subElement : BPMNElements){
			if(subElement.hasMonitoringPoints()){
				subElementsWithMonitoringpoints.add(subElement);
			}
		}
		return subElementsWithMonitoringpoints;
	}
	
	public static boolean exists(String name) {
		return !BPMNProcess.findByName(name).isEmpty();
	}

	public static BPMNProcess findByContainedElement(AbstractBPMNElement bpmnElement) {
		for(BPMNProcess process : BPMNProcess.findAll()){
			for(AbstractBPMNElement element : process.getBPMNElementsWithOutSequenceFlows()){
				if(element.getID() == bpmnElement.getID()){
					return process;
				}
			}
		}
		return null;
	}
	
}
