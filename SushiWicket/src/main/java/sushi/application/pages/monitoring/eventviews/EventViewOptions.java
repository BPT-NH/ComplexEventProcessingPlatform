package sushi.application.pages.monitoring.eventviews;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tools.ant.util.DateUtils;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.process.SushiProcessInstance;
import sushi.visualisation.SushiEventView;
import sushi.visualisation.SushiChartConfiguration;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.AxisType;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.DateTimeLabelFormat;
import com.googlecode.wickedcharts.highcharts.options.DateTimeLabelFormat.DateTimeProperties;
import com.googlecode.wickedcharts.highcharts.options.Function;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.Tooltip;
import com.googlecode.wickedcharts.highcharts.options.ZoomType;
import com.googlecode.wickedcharts.highcharts.options.color.RgbaColor;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CustomCoordinatesSeries;

/**
 * This class prepares a splatter diagram that illustrates the occurence of events of certain eventtypes
 * This object can be used to create a chart-object with the wicked chart framework.
 */
public class EventViewOptions extends Options{
	
	public SushiEventView eventView;

    private List<Coordinate<String, Number>> eventsWithoutProcessInstance = new ArrayList<Coordinate<String, Number>>();
    private HashMap<SushiProcessInstance, List<Coordinate<String, Number>>> processSeriesData = new HashMap<SushiProcessInstance, List<Coordinate<String, Number>>>();
	
	private HashMap<SushiEventType, Integer> mappingTypeToInt = new HashMap<SushiEventType, Integer>();

    private static final long serialVersionUID = 1L;

	public EventViewOptions(SushiEventView configuration) {
	  eventView = configuration;
	  
	    ChartOptions chartOptions = new ChartOptions();
	    chartOptions.setType(SeriesType.SCATTER);
	    
	    //enable zooming
	    chartOptions.setZoomType(ZoomType.X);
	    
	    this.setChartOptions(chartOptions);

	    this.setTitle(new Title("EventView (" + eventView.getTimePeriod().toString() + ")"));

	    //X-Achse
	    Axis xAxis = new Axis();
	    xAxis.setType(AxisType.DATETIME);

	    DateTimeLabelFormat dateTimeLabelFormat = new DateTimeLabelFormat()
	    	.setProperty(DateTimeProperties.DAY, "%e.%m.%Y")
	        .setProperty(DateTimeProperties.MONTH, "%m/%Y")
	        .setProperty(DateTimeProperties.YEAR, "%Y");
	    xAxis.setDateTimeLabelFormats(dateTimeLabelFormat);
	    
	    this.setxAxis(xAxis);

	    //Y-Achse
	    Axis yAxis = new Axis();
	    yAxis.setTitle(new Title("EventTypes"));
	    yAxis.setType(AxisType.LINEAR);
	    //disable Decimals, because integers represent event types, decimals make no sense here
	    yAxis.setAllowDecimals(false);
	    yAxis.setMax(eventView.getEventTypes().size());
	    setyAxis(yAxis);
	    
	    //Tooltip
	    Tooltip tooltip = new Tooltip();
	    tooltip.setFormatter(new Function(
	            "return '<b>'+ this.series.name +'</b><br/>'+Highcharts.dateFormat('%e.%m.%Y', this.x);"));
	    this.setTooltip(tooltip);
	    
	    //create a mapping from eventtypes to integer values, because in a splatter chart strings cannot be used in the y-axis
	    generateMappingForEventTypes();
	    
	    for (SushiEventType type : eventView.getEventTypes()) {
	    	sortEventsForEventType(type);
	    }

	    //add series for each process instance to diagram
	    for (Entry<SushiProcessInstance, List<Coordinate<String, Number>>> seriesTuple : processSeriesData.entrySet()) {
	    	List<Coordinate<String, Number>> seriesData = seriesTuple.getValue();
	    	CustomCoordinatesSeries<String, Number> series = new CustomCoordinatesSeries<String, Number>();
		    series.setName(seriesTuple.getKey().toString());
		    series.setData(seriesData);
		    addSeries(series);
	    }
	    
		//add series for uncorrelated events
	    CustomCoordinatesSeries<String, Number> series = new CustomCoordinatesSeries<String, Number>();
	    series.setName("uncorrelated");
	    series.setData(eventsWithoutProcessInstance);
	    addSeries(series);
	  };
	  
	private void generateMappingForEventTypes() {
		//save an integer value for each event type
	    for (int i= 1; i <= eventView.getEventTypes().size(); i++) {
	    	mappingTypeToInt.put(eventView.getEventTypes().get(i-1), i);
	    }
	}
	  
	private void sortEventsForEventType(SushiEventType eventType){
		for (SushiEvent event : SushiEvent.findByEventTypeAndTime(eventType, eventView.getTimePeriod())) {
			List<SushiProcessInstance> processInstances = event.getProcessInstances();
			if (processInstances.isEmpty()) {
				//no process instance
				eventsWithoutProcessInstance.add(getCoordinate(event, eventType));
				continue;
			}
			for (SushiProcessInstance instance : processInstances) {
				List<Coordinate<String, Number>> seriesData = processSeriesData.get(instance);
				if (seriesData.equals(null)) {	//create new seriesDatea
				    seriesData = new ArrayList<Coordinate<String, Number>>();
				}
				seriesData.add(getCoordinate(event, eventType));
			    processSeriesData.put(instance, seriesData);
			}
		}
	}
	
	private Coordinate<String, Number> getCoordinate(SushiEvent event, SushiEventType eventType) {
		return new Coordinate<String, Number>
		(DateUtils.format(event.getTimestamp(), "'Date.UTC('yyyy, M, d, h, m, s')'"),
				mappingTypeToInt.get(eventType));
	}

	
	/**
	 * creates an explanation string that translates the mapping from integer values to event types
	 * @return explanation string
	 */
	public String getExplanationString() {
		String explanation = "";
		//sort event types
		Map<Integer, SushiEventType> intToEvent = invert(mappingTypeToInt);
		for (int i = 1; i <= intToEvent.size(); i++) {
			explanation += (i) + " : " + intToEvent.get(i).getTypeName() + "\t";
		}
		return explanation;
	}
	
	/**
	 * inverts a map, so that the former values become keys
	 * @param map
	 * @return inverted map
	 */
	private static <V, K> Map<V, K> invert(Map<K, V> map) {
	    Map<V, K> inv = new HashMap<V, K>();

	    for (Entry<K, V> entry : map.entrySet())
	        inv.put(entry.getValue(), entry.getKey());

	    return inv;
	}
	
}