package sushi.adapter;

import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.xml.sax.SAXException;

import sushi.esper.SushiStreamProcessingAdapter;
import sushi.eventhandling.Broker;
import sushi.traffic.importer.TomTomTrafficimporter;

/**
 * Adapter for automated import of traffic events 
 */
public class SushiTrafficAdapter implements Job {

	TomTomTrafficimporter trafficImporter;
	long time;
	
	/**
	 * registers automatically the TrafficEventType
	 * start XQuarzjob with scheduleQuarzJob
	 */
	public SushiTrafficAdapter(){
		trafficImporter = new TomTomTrafficimporter();
	}

	public static void main(String[] args) throws JSONException {
		SushiTrafficAdapter trafficAdapter = new SushiTrafficAdapter();
		trafficAdapter.scheduleQuartzJob();
	}

	private void registerTrafficEventType(){
		Broker.send(trafficImporter.getTrafficEventtype());
	}

	/**
	 * import traffic events for berlin and potsdam 
	 */
	public void importTrafficEventsBerlinPotsdam() throws JSONException{
		registerTrafficEventType();
		Broker.send(trafficImporter.getTrafficSushiEventsBerlin());
		Broker.send(trafficImporter.getTrafficSushiEventsPotsdam());
	}
	
	/**
	 * imports traffic events of the route from hamburg to berlin 
	 */
	public void importTrafficEventsHamburgToBerlin() throws JSONException, XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		registerTrafficEventType();
		Broker.send(trafficImporter.getTrafficSushiEventHamburgToBerlin());
	}

	/**
	 * creates an Quarz scheduler which imports every 120.5s trafficevents
	 */
	public void scheduleQuartzJob(){
		try {
			// Grab the Scheduler instance from the Factory 
			org.quartz.Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			// define the job and tie it to our SushiTrafficAdapter class
			JobDetail jd = new JobDetail("trafficJob","group", SushiTrafficAdapter.class);

			// triggers all 120.5 seconds the execution of execution, never ends
			SimpleTrigger simpleTrigger = new SimpleTrigger("my traffic job",scheduler.DEFAULT_GROUP,
					new Date(),null,SimpleTrigger.REPEAT_INDEFINITELY,120500);
			// Tell quartz to schedule the job using our trigger
			scheduler.scheduleJob(jd, simpleTrigger);
			// and start it
			scheduler.start();
			//scheduler.shutdown();
		} catch (SchedulerException se) {
			se.printStackTrace();
		}
	}

	/**
	 * this method will be called from the scheduler
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			if (SushiStreamProcessingAdapter.getInstance().isActivatedWeatherAdapter()) importTrafficEventsHamburgToBerlin();
		} catch (JSONException | XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteQuartzJob() {
		try {
		org.quartz.Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.deleteJob("trafficJob","group");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
	}
}
