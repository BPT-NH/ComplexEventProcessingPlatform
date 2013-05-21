package sushi.application.pages.monitoring.visualisation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.util.DateUtils;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.visualisation.SushiSimpleChartOptions;

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
import com.googlecode.wickedcharts.highcharts.options.color.RgbaColor;
import com.googlecode.wickedcharts.highcharts.options.series.Coordinate;
import com.googlecode.wickedcharts.highcharts.options.series.CustomCoordinatesSeries;

	public class SushiSplatterChartOptions extends Options{
		
		public SushiEventType eventType;
		public String attributeName;
		public String title;

			  private static final long serialVersionUID = 1L;

		  public SushiSplatterChartOptions(SushiSimpleChartOptions configuration) throws Exception {
			  eventType = configuration.getEventType();
			  attributeName = configuration.getAttributeName();
			  title = configuration.getTitle();
			  
			  
			    ChartOptions chartOptions = new ChartOptions();
			    chartOptions
			        .setType(SeriesType.SCATTER);
			    this
			        .setChartOptions(chartOptions);

			    this
			        .setTitle(new Title(title));

			    //X-Achse
			    
			    Axis xAxis = new Axis();
			    xAxis
			        .setType(AxisType.DATETIME);

			    DateTimeLabelFormat dateTimeLabelFormat = new DateTimeLabelFormat()
			    	.setProperty(DateTimeProperties.DAY, "%e.%m.%Y")
			        .setProperty(DateTimeProperties.MONTH, "%m/%Y")
			        .setProperty(DateTimeProperties.YEAR, "%Y");
			    	
			    xAxis.setDateTimeLabelFormats(dateTimeLabelFormat);
			    
			    this.setxAxis(xAxis);

			    //Y-Achse
			    
			    Axis yAxis = new Axis();
			    yAxis
			        .setTitle(new Title(attributeName));
			    yAxis
		        	.setType(AxisType.LINEAR);
			    
			    this.setyAxis(yAxis);

			    //Tooltip
			    
			    Tooltip tooltip = new Tooltip();
			    tooltip
			        .setFormatter(new Function(
			            "return '<b>'+ this.series.name +'</b><br/>'+Highcharts.dateFormat('%e.%m.%Y', this.x) +': '+ this.y ;"));
			    this.setTooltip(tooltip);
			    
			    CustomCoordinatesSeries<String, Number> series = new CustomCoordinatesSeries<String, Number>();
			    series.setColor(new RgbaColor(119, 152, 191, 0.5f));
			    series.setName(eventType.getTypeName());
			    series.setData(getSeriesData());
			    addSeries(series);

			  }

		private List<Coordinate<String, Number>> getSeriesData() throws Exception {
			
		    List<Coordinate<String, Number>> seriesData = new ArrayList<Coordinate<String, Number>>();
			
			for (SushiEvent event : SushiEvent.findByEventType(eventType)) {
				Serializable value = event.getValues().get(attributeName);
				if (value == null) throw new Exception("AttributeName " + attributeName + " contains null-Values for " + eventType);
				//should be integer
				int intValue = Integer.parseInt((String) value);
				seriesData.add(new Coordinate<String, Number>
							(DateUtils.format(event.getTimestamp(), "'Date.UTC('yyyy, M, d, h, m, s')'"),
									intValue));
			}
			
			return seriesData;
		}
	}