package sushi.application.pages.main;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import sushi.application.pages.AbstractSushiPage;
import de.agilecoders.wicket.markup.html.bootstrap.carousel.Carousel;

public class MainPage extends AbstractSushiPage {

	private static final long serialVersionUID = 1L;
	
	public MainPage() {
		super();
		
		buildMainLayout();
	}
	
	public MainPage(PageParameters pageParameters) {
		this();
		
		if (pageParameters.get("successFeedback") != null) {
			getFeedbackPanel().success(pageParameters.get("successFeedback"));
		} else if (pageParameters.get("errorFeedback") != null) {
			getFeedbackPanel().error(pageParameters.get("errorFeedback"));
		}
	}

	private void buildMainLayout() {
		
		//not logged-in : show image-Carousel
		ImageModel imageModel = new ImageModel(this);
		Carousel carousel = new Carousel("carousel", imageModel);
		add(carousel);
	}
	
}
