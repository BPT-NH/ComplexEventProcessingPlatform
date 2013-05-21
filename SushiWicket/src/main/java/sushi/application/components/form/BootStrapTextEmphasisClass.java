package sushi.application.components.form;

public enum BootStrapTextEmphasisClass {
	
	Muted("muted"), Warning("text-warning"), Error("text-error"),Info("text-info"),Success("text-success");
	
	private String classValue;

	BootStrapTextEmphasisClass(String classValue){
		this.classValue = classValue;
	}

	public String getClassValue() {
		return classValue;
	}

}
