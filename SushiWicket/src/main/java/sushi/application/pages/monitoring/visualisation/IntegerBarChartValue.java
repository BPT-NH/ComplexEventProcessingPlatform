package sushi.application.pages.monitoring.visualisation;

/**
 * This class is a helper to create a @see SushiColumnChartOptions object.
 * It represents one value for a column chart of an integer attribute.
 * This object counts the number of appearances of integer values in a certain range.
 */
public class IntegerBarChartValue {

	private int startPeriod;
	private int endPeriod;
	//contains the number of events with attribut values in the defined range
	private int frequency = 0;
	
	public IntegerBarChartValue(int start, int end) {
		startPeriod = start;
		endPeriod = end;
	}
	
	public int getStartPeriod() {
		return startPeriod;
	}
	public void setStartPeriod(int startPeriod) {
		this.startPeriod = startPeriod;
	}
	public int getEndPeriod() {
		return endPeriod;
	}
	public void setEndPeriod(int endPeriod) {
		this.endPeriod = endPeriod;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public void increaseFrequency() {
		frequency += 1;
	}
	
	public String getNameOfPeriod() {
		if (startPeriod == endPeriod) return startPeriod + "";
		return startPeriod + " to " + endPeriod;
	}
	
	public boolean containsValue(int value) {
		return (value >= startPeriod && value <= endPeriod);
	}
	
}
