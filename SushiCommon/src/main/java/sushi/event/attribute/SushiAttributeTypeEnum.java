package sushi.event.attribute;

/**
 * Encapsulates types which we support for attributes  
 */
public enum SushiAttributeTypeEnum {
	
	STRING ("String"), 
	INTEGER ("Integer"), 
	DATE ("Date");
	
	private String type;
	
	SushiAttributeTypeEnum(String type){
		this.type = type;
	}

	public String getName() {
		return type;
	}
	
	@Override 
	public String toString() {
		// only capitalize the first letter
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}
}
