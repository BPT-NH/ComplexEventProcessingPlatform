package sushi.application.pages.monitoring.visualisation;

import java.util.ArrayList;
import java.util.List;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;

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
import com.googlecode.wickedcharts.highcharts.options.series.CoordinatesSeries;

	public class TimeDelayDiagramm extends Options{

			  private static final long serialVersionUID = 1L;

		  public TimeDelayDiagramm() {
			    ChartOptions chartOptions = new ChartOptions();
			    chartOptions
			        .setType(SeriesType.SCATTER);
			    this
			        .setChartOptions(chartOptions);

			    this
			        .setTitle(new Title("Delay of Container-Events"));

			    //X-Achse
			    
			    Axis xAxis = new Axis();
			    xAxis
			        .setType(AxisType.LINEAR);

			    DateTimeLabelFormat dateTimeLabelFormat = new DateTimeLabelFormat()
			        .setProperty(DateTimeProperties.MONTH, "%e. %b")
			        .setProperty(DateTimeProperties.YEAR, "%b");
//			    xAxis.setDateTimeLabelFormats(dateTimeLabelFormat);
			    
			    this.setxAxis(xAxis);

			    //Y-Achse
			    
			    Axis yAxis = new Axis();
			    yAxis
			        .setTitle(new Title("Delay-Time in Hours"));
			    yAxis
		        	.setType(AxisType.LINEAR);
			    
			    this.setyAxis(yAxis);

			    //Tooltip
			    
			    Tooltip tooltip = new Tooltip();
			    tooltip
			        .setFormatter(new Function(
			            "return '<b>'+ this.series.name +'</b><br/>'+Highcharts.dateFormat('%e. %b', this.x) +': '+ this.y +' m';"));
			    this.setTooltip(tooltip);
			    
			    CoordinatesSeries series2 = new CoordinatesSeries();
			    series2.setColor(new RgbaColor(119, 152, 191, 0.5f));
			    series2.setName("Container-Data");
			    series2.setData(getSeriesData());
			    addSeries(series2);

			  }

		private List<Coordinate<Number, Number>> getSeriesData() {
			
		    List<Coordinate<Number, Number>> seriesData1 = new ArrayList<Coordinate<Number, Number>>();
			int oneHourInMilliSecs = 60*60*1000;
			
			SushiEventType type = SushiEventType.findByTypeName("TimeDiffOfContainers");
			if (type == null) {
				return seriesData1;
			}
						
			for (SushiEvent event : SushiEvent.findByEventType(type)) {
				seriesData1.add(new Coordinate<Number, Number>(event.getTimestamp().getTime(), Integer.valueOf(event.getValues().get("TimeDifference").toString())/oneHourInMilliSecs));
			}
			
			return seriesData1;
		}
	}