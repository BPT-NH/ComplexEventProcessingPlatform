package sushi.application.components.table.model;


public abstract class AbstractDataProvider {
	
	public abstract void selectEntry(int entryId);
	
	public abstract void deselectEntry(int entryId);
	
	public abstract boolean isEntrySelected(int entryId);
	
	public abstract Object getEntry(int entryId);

}
