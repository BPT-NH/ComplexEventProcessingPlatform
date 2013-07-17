package sushi.monitoring.querycreation;

import static org.junit.Assert.assertTrue;
import sushi.monitoring.AbstractMonitoringTest;
import sushi.monitoring.bpmn.BPMNQueryMonitor;
import sushi.monitoring.bpmn.DetailedQueryStatus;
import sushi.monitoring.bpmn.ProcessInstanceStatus;
import sushi.process.SushiProcessInstance;

/**
 * This class centralizes methods for all tests, which test the creation of BPMN queries and monitor their execution.
 * @author micha
 */
public abstract class AbstractQueryCreationTest extends AbstractMonitoringTest {
	
	protected void assertQueryStatus() {
		//Auf Listener hören
		BPMNQueryMonitor queryMonitor = BPMNQueryMonitor.getInstance();
		for(SushiProcessInstance processInstance : SushiProcessInstance.findAll()){
			assertTrue(queryMonitor.getStatus(processInstance) == ProcessInstanceStatus.Finished);
			for(DetailedQueryStatus detailedQueryStatus : queryMonitor.getDetailedStatus(processInstance).getElements()){
				assertTrue(detailedQueryStatus.getViolationStatus().isEmpty());
			}
		}
	}
	
}
