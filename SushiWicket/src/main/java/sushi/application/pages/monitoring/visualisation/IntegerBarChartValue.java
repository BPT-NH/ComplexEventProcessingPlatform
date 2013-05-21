package sushi.application.pages.monitoring.visualisation;

public class IntegerBarChartValue {

	private int startPeriod;
	private int endPeriod;
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
