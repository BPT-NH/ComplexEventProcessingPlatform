package sushi.persistence;

public enum DatabaseEnvironments {
	
	DEVELOPMENT ("sushi_development"), 
	TEST ("sushi_testing"), 
	PRODUCTION ("sushi_production");
	
	private String databaseName;
	
	DatabaseEnvironments(String environments){
		this.databaseName = environments;
	}

	public String getDatabaseName() {
		return databaseName;
	}
	
}

