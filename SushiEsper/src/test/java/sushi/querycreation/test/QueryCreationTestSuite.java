package sushi.querycreation.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sushi.querycreation.test.complex.ComplexProcessTest;
import sushi.querycreation.test.complex.ComplexProcessWithSomeMonitoringPointsTest;
import sushi.querycreation.test.subprocess.ProcessWithTwoEndEventsTest;
import sushi.querycreation.test.subprocess.SubProcessTest;
import sushi.querycreation.test.subprocess.SubProcessWithCancelEventTest;
import sushi.querycreation.test.timer.MessageAndTimerTest;
import sushi.querycreation.test.timer.MessageAndTimerWithTimerTest;
import sushi.querycreation.test.timer.MessageAndTimerWithZeroTimeTest;

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
