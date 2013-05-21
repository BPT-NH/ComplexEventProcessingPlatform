package sushi.adapter;

import java.util.Date;

import org.json.JSONException;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import sushi.esper.SushiEsper;
import sushi.eventhandling.Broker;
import sushi.traffic.importer.TomTomTrafficimporter;

public class SushiTrafficAdapter implements Job {

	TomTomTrafficimporter trafficImporter;
	long time;
	
	/**
	 * registers automatically the TrafficEventType
	 * start XQuarzjob with scheduleQuarzJob
	 */
	public SushiTrafficAdapter(){
		trafficImporter = new TomTomTrafficimporter();
		registerTrafficEventType();
	}

	public static void main(String[] args) throws JSONException {
		SushiTrafficAdapter trafficAdapter = new SushiTrafficAdapter();
		trafficAdapter.scheduleQuartzJob();
	}

	private void registerTrafficEventType(){
		Broker.send(trafficImporter.getTrafficEventtype());
	}

	public void importTrafficEvents() throws JSONException{
		Broker.send(trafficImporter.getTrafficSushiEventsBerlin());
		Broker.send(trafficImporter.getTrafficSushiEventsPotsdam());
	}
	
	public void importTrafficEventsHamburgToBerlin() throws JSONException{
		Broker.send(trafficImporter.getTrafficSushiEventsBerlin());
	}

	public void scheduleQuartzJob(){
		try {
			// Grab the Scheduler instance from the Factory 
			org.quartz.Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			// define the job and tie it to our SushiTrafficAdapter class
			JobDetail jd = new JobDetail("trafficJob","group", SushiTrafficAdapter.class);

			// triggers all 120.5 seconds the execution of execution, never ends
			SimpleTrigger simpleTrigger = new SimpleTrigger("mytrigger",scheduler.DEFAULT_GROUP,
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

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			if (SushiEsper.getInstance().isActivatedWeatherAdapter()) importTrafficEvents();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
