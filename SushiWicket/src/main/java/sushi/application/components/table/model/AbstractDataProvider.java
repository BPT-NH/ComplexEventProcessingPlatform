package sushi.application.components.table.model;


/**
 * This class should be the parent class for classes, which presents data to the user in a table.
 * @author micha
 */
public abstract class AbstractDataProvider {
	
	public abstract void selectEntry(int entryId);
	
	public abstract void deselectEntry(int entryId);
	
	public abstract boolean isEntrySelected(int entryId);
	
	public abstract Object getEntry(int entryId);

}
