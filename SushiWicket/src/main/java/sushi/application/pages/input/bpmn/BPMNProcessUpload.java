package sushi.application.pages.input.bpmn;

import sushi.application.pages.AbstractSushiPage;

public class BPMNProcessUpload extends AbstractSushiPage{

	private static final long serialVersionUID = 1L;

	public BPMNProcessUpload(){
		super();
		
		add(new BPMNProcessUploadPanel("bpmnProcessUploadPanel", this));
	}
}
