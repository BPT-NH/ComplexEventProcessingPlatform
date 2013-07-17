package sushi.adapter;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.eventhandling.Broker;
import sushi.weather.importer.DWDImporter;
import sushi.xml.importer.XMLParsingException;

/**
 * Adapter for automated import of weather events 
 */
public class SushiWeatherAdapter implements Job{
	DWDImporter weatherImporter;
	long time;
	
	/**
	 * registers automatically the WeatherEventType
	 * start XQuarzjob with scheduleQuarzJob
	 */
	public SushiWeatherAdapter() {
		weatherImporter = new DWDImporter();
	}

	public static void main(String[] args) throws JSONException, SocketException, IOException, XMLParsingException {
		SushiStreamProcessingAdapter.getInstance().setActivatedWeatherAdapter(true);
		SushiWeatherAdapter weatherAdapter = new SushiWeatherAdapter();
		weatherAdapter.scheduleQuartzJob();
	}

	private void registerWeatherEventType() {
		try {
			if (SushiEventType.findBySchemaName("legend_warnings_CAP") != null) return;
			Broker.send(weatherImporter.getWeatherEventtype());
		} catch (XMLParsingException e) {
			
		}
	}

	/**
	 * sends newest weatherevents to esper 
	 */
	private void importWeatherEvents() throws JSONException, XMLParsingException, IOException{
		registerWeatherEventType();
		ArrayList<SushiEvent> weatherEvents = weatherImporter.getNewWeatherEvents();
		Broker.send(weatherEvents);
	}

	public void deleteQuartzJob() {
		try {
			// Grab the Scheduler instance from the Factory 
			org.quartz.Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.deleteJob("weatherJob","group2");
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void scheduleQuartzJob(){
		try {
			// Grab the Scheduler instance from the Factory 
			org.quartz.Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			// define the job and tie it to our SushiWeatherAdapter class
			JobDetail jd = new JobDetail("weatherJob","group2", SushiWeatherAdapter.class);

			// triggers all 120.5 seconds the execution of execution, never ends
			SimpleTrigger simpleTrigger = new SimpleTrigger("my weather job",scheduler.DEFAULT_GROUP,
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
			if (SushiStreamProcessingAdapter.getInstance().isActivatedWeatherAdapter()) importWeatherEvents();
		} catch (JSONException | XMLParsingException | IOException e) {
			e.printStackTrace();
		}
	}
}