package sushi.excel.importer;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import sushi.event.SushiEvent;
import sushi.event.collection.SushiMapTree;

/**
 * @author micha
 *
 */
public class SushiImportEvent extends SushiEvent {
	
	private static final long serialVersionUID = 8101339625770879566L;
	private Date importTime;
	private String extractedTimestampName;

	public SushiImportEvent(Date extractedTimestamp, SushiMapTree<String, Serializable> values, String extractedTimestampName, Date currentTimestamp) {
		super(extractedTimestamp, values);
		this.extractedTimestampName = extractedTimestampName;
		this.importTime = currentTimestamp;
	}

	public Date getImportTime() {
		return importTime;
	}

	public String getExtractedTimestampName() {
		return extractedTimestampName;
	}
	
	@Override
	public String toString() {
		String eventText = "Import event with ID: " + this.ID + " time: " + this.timestamp + " import time: " + this.importTime.toString();
		Map<String, Serializable> values = this.getValues();
		Iterator<String> valueIterator = values.keySet().iterator();
		while(valueIterator.hasNext()){
			String valueKey = valueIterator.next();
			eventText = eventText + " " + valueKey + ": " + values.get(valueKey);
		}
		return eventText;
	}
	
}
