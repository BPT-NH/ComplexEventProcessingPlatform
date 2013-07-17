package sushi.application.pages.monitoring.visualisation;

import java.util.ArrayList;
import java.util.List;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.visualisation.SushiChartConfiguration;

import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.CreditOptions;
import com.googlecode.wickedcharts.highcharts.options.DataLabels;
import com.googlecode.wickedcharts.highcharts.options.Function;
import com.googlecode.wickedcharts.highcharts.options.Global;
import com.googlecode.wickedcharts.highcharts.options.HorizontalAlignment;
import com.googlecode.wickedcharts.highcharts.options.Labels;
import com.googlecode.wickedcharts.highcharts.options.Legend;
import com.googlecode.wickedcharts.highcharts.options.LegendLayout;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.Overflow;
import com.googlecode.wickedcharts.highcharts.options.PlotOptions;
import com.googlecode.wickedcharts.highcharts.options.PlotOptionsChoice;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.Tooltip;
import com.googlecode.wickedcharts.highcharts.options.VerticalAlignment;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;
 
/**
 * This class prepares a column diagram that illustrates the frequency of the values of an attribute of an eventtype
 * This object can be used to create a chart-object with the wicked chart framework.
 */
public class SushiColumnChartOptions extends Options {
 
  private static final long serialVersionUID = 1L;
 
	public SushiEventType eventType;
	public String attributeName;
	public SushiAttributeTypeEnum attributeType;
	public int rangeSize;
	public String title;

  
  public SushiColumnChartOptions(SushiChartConfiguration configuration) {
		  eventType = configuration.getEventType();
		  attributeName = configuration.getAttributeName();
		  attributeType = configuration.getAttributeType();
		  rangeSize = configuration.getRangeSize();
		  title = configuration.getTitle();
	  
    setChartOptions(new ChartOptions()
        .setType(SeriesType.COLUMN));
 
    setGlobal(new Global()
        .setUseUTC(Boolean.TRUE));
 
    setTitle(new Title(title));
 
 
    setxAxis(new Axis()
        .setCategories(eventType.getTypeName())
        .setTitle(new Title(null)));
 
    setyAxis(new Axis()
        .setTitle(
            new Title("Frequency")
                .setAlign(HorizontalAlignment.HIGH))
        .setLabels(new Labels().setOverflow(Overflow.JUSTIFY)));
 
    setTooltip(new Tooltip()
        .setFormatter(new Function(
            "return ''+this.series.name +': '+ this.y;")));
 
    setPlotOptions(new PlotOptionsChoice()
        .setBar(new PlotOptions()
            .setDataLabels(new DataLabels()
                .setEnabled(Boolean.TRUE))));
 
    setLegend(new Legend()
        .setLayout(LegendLayout.VERTICAL)
        .setAlign(HorizontalAlignment.RIGHT)
        .setVerticalAlign(VerticalAlignment.TOP)
        .setX(-100)
        .setY(100)
        .setFloating(Boolean.TRUE)
        .setBorderWidth(1)
        .setBackgroundColor(new HexColor("#ffffff"))
        .setShadow(Boolean.TRUE));
 
    setCredits(new CreditOptions()
        .setEnabled(Boolean.FALSE));
 
    for (SimpleSeries serie : buildSeries()) {
    	addSeries(serie);
    }
  
  }
  
  /**
   * create data series for diagram
   * @return data series
   */
  public List<SimpleSeries> buildSeries() {
	  if(attributeType.equals(SushiAttributeTypeEnum.STRING)) {
		   return getStringSeries();}
	  else {
		  return getIntegerValues();}
  }
  
  /**
   * create data series for string attributes
   * @return data series
   */
  public List<SimpleSeries> getStringSeries() {
	  List<SimpleSeries> series = new ArrayList<SimpleSeries>();
	  //collect values of attribute, each value will be one column
	  List<String> distinctValues = SushiEvent.findDistinctValuesOfAttributeOfType(attributeName, eventType);
	  for (String value : distinctValues) {
		  //count number of appearances
		  long numberOfAppearances = SushiEvent.findNumberOfAppearancesByAttributeValue(attributeName, value, eventType);
		  SimpleSeries serie = new SimpleSeries();
	      serie.setName(value);
	      serie.setData(numberOfAppearances);
		  series.add(serie);
	  }
	  return series;	
  }

  /**
   * create data series for integer attributes
   * @return data series
   */
  public List<SimpleSeries> getIntegerValues() {
	  try{
		  List<SimpleSeries> series = new ArrayList<SimpleSeries>();
		  //build groups
		  List<IntegerBarChartValue> periods = new ArrayList<IntegerBarChartValue>();
		  long max = SushiEvent.getMaxOfAttributeValue(attributeName, eventType);
		  long min = SushiEvent.getMinOfAttributeValue(attributeName, eventType);
		  if (min < 0) {
			  //build ranges for min to 0
			  for (int i = -1; i <= min; i -= rangeSize) {
				  periods.add(new IntegerBarChartValue(i - rangeSize-1, i));
			  }
		  }
			  // build ranges from 0 up  
		  for (int i = 0; i <= max; i += rangeSize) {
			  periods.add(new IntegerBarChartValue(i, i + rangeSize-1));
		  }
		  //collect frequencies
		  List<SushiEvent> events = SushiEvent.findByEventType(eventType);
		  for (SushiEvent event : events) {
			  int value = Integer.parseInt((String) event.getValues().getValueOfAttribute(attributeName));
			  for (IntegerBarChartValue period : periods) {
				  if (period.containsValue(value)) {
					  period.increaseFrequency();
					  break;
				  }
			  }
		  }
		  //put into series
		  for (IntegerBarChartValue value : periods) {
			  if (value.getFrequency() == 0) continue;
			  SimpleSeries serie = new SimpleSeries();
		      serie.setName(value.getNameOfPeriod());
		      serie.setData(value.getFrequency());
			  series.add(serie);
		  }
		  return series;
	  } catch(Exception e) {
		  return getStringSeries();
	  }
  }

}