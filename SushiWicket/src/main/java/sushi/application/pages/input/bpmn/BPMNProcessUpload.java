package sushi.application.pages.input.bpmn;

import sushi.application.pages.AbstractSushiPage;

/**
 * This page displays the {@link BPMNProcessUploadPanel}.
 * @author micha
 */
public class BPMNProcessUpload extends AbstractSushiPage{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a page, which displays the {@link BPMNProcessUploadPanel}.
	 */
	public BPMNProcessUpload(){
		super();
		
		add(new BPMNProcessUploadPanel("bpmnProcessUploadPanel", this));
	}
}
