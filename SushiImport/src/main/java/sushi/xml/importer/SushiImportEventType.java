package sushi.xml.importer;

import sushi.event.SushiEventType;

/**
 * @author micha
 *
 */
public class SushiImportEventType extends SushiEventType {
	
	private boolean isImportedTimestamp = false;
	private String importedTimestampName;

	public SushiImportEventType(String eventTypeName){
		super(eventTypeName);
	}

	public void setImportedTimestampName(String importedTimestampName) {
		this.importedTimestampName = importedTimestampName;
	}

	public boolean isImportedTimestamp() {
		return isImportedTimestamp;
	}

	public void setImportedTimestamp(boolean isImportedTimestamp) {
		this.isImportedTimestamp = isImportedTimestamp;
	}

	public String getImportedTimestampName() {
		return importedTimestampName;
	}
	
	//TODO Importierte ID nicht als eigentliche ID berücksichtigen
	
	//TODO Importierter TimeStamp

}
