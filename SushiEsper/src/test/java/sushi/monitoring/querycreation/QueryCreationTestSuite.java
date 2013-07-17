package sushi.monitoring.querycreation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sushi.monitoring.querycreation.complex.ComplexProcessTest;
import sushi.monitoring.querycreation.complex.ComplexProcessWithSomeMonitoringPointsTest;
import sushi.monitoring.querycreation.subprocess.ProcessWithTwoEndEventsTest;
import sushi.monitoring.querycreation.subprocess.SubProcessTest;
import sushi.monitoring.querycreation.subprocess.SubProcessWithCancelEventTest;
import sushi.monitoring.querycreation.timer.MessageAndTimerTest;
import sushi.monitoring.querycreation.timer.MessageAndTimerWithTimerTest;
import sushi.monitoring.querycreation.timer.MessageAndTimerWithZeroTimeTest;

/**
 * Testsuite with all tests for query creation and monitoring of the execution of BPMN processes.
 * @author micha
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	SimpleSequenceTest.class,
	SimpleSequenceStateTransitionTest.class,
	AndTest.class, 
	XORTest.class,
	LoopTest.class,
	ComplexProcessTest.class,
	ComplexProcessWithSomeMonitoringPointsTest.class,
	ProcessWithTwoEndEventsTest.class,
	SubProcessTest.class,
	SubProcessWithCancelEventTest.class,
	MessageAndTimerTest.class,
	MessageAndTimerWithTimerTest.class,
	MessageAndTimerWithZeroTimeTest.class
})
public class QueryCreationTestSuite {

}
