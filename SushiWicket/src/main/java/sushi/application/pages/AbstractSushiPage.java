package sushi.application.pages;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.application.SushiAuthenticatedSession;
import sushi.application.components.SushiNavBarDropDownButton;
import sushi.application.pages.adapter.AdapterPage;
import sushi.application.pages.aggregation.AggregationPage;
import sushi.application.pages.correlation.CorrelationPage;
import sushi.application.pages.eventrepository.EventRepository;
import sushi.application.pages.eventrepository.eventtypeeditor.EventTypeEditor;
import sushi.application.pages.export.Export;
import sushi.application.pages.input.FileUploader;
import sushi.application.pages.input.bpmn.BPMNProcessUpload;
import sushi.application.pages.main.MainPage;
import sushi.application.pages.monitoring.bpmn.BPMNMonitoringPage;
import sushi.application.pages.monitoring.notification.NotificationPage;
import sushi.application.pages.monitoring.visualisation.VisualisationPage;
import sushi.application.pages.querying.LiveQueryEditor;
import sushi.application.pages.querying.OnDemandQueryEditor;
import sushi.application.pages.querying.bpmn.BPMNQueryEditor;
import sushi.application.pages.simulator.SimulationPage;
import sushi.application.pages.user.LoginPage;
import de.agilecoders.wicket.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.button.DropDownAutoOpen;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarComponents;

public abstract class AbstractSushiPage extends WebPage {
	
	private static final long serialVersionUID = 1L;
	
	private String footer = "&copy; 2013 bachelor project W1 at the <a href=\"http://www.hpi.uni-potsdam.de/\" target=\"_blank\">Hasso Plattner Institute</a>";
//	private boolean stripTags;

	private String logInString;

	private NotificationPanel feedbackPanel;

	@SuppressWarnings("unchecked")
	public AbstractSushiPage(){
		// TODO: richtige URL?!
//		add(new FaviconLink("favicon", "favicon.ico"));
		
		if(((SushiAuthenticatedSession)Session.get()).getUser() != null){
			logInString = ((SushiAuthenticatedSession)Session.get()).getUser().getName();
		} else {
			logInString = "Sign In";
		}
		
		Navbar navbar = new Navbar("sushiNavBar");
		navbar.fluid();
		navbar.brandName(Model.of("GET EVENTS"));
		    
		navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT, 
				newImportDropDownButton(),
				newProcessingDropDownButton(),
				new NavbarButton<EventRepository>(EventRepository.class, Model.of("Event Repository")).setIconType(IconType.book),
				newQueriesDropDownButton(),
				newMonitoringDropDownButton(),
				new NavbarButton<Export>(Export.class, Model.of("Export")).setIconType(IconType.download),
				newEventProducerDropDownButton()));
		
		navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT, 
				new NavbarButton<MainPage>(LoginPage.class, Model.of(logInString)).setIconType(IconType.user)));
		
		add(navbar);		
		addFeedbackPanel();
		
		Label footer = new Label("footer", new PropertyModel<String>(this, "footer"));
		footer.setEscapeModelStrings(false);
		add(footer);
	}
	
	private Component newImportDropDownButton() {
		 return new SushiNavBarDropDownButton(Model.of("Import"))
		 	.addButton(new MenuBookmarkablePageLink<FileUploader>(FileUploader.class, Model.of("Excel / XML / XSD")).setIconType(IconType.inbox))
		 	.addButton(new MenuBookmarkablePageLink<BPMNProcessUpload>(BPMNProcessUpload.class, Model.of("BPMN")).setIconType(IconType.leaf))
		 	.setIconType(IconType.upload).add(new DropDownAutoOpen());
	 }
	
	private Component newProcessingDropDownButton(){
		 return new SushiNavBarDropDownButton(Model.of("Processing"))
		 	.addButton(new MenuBookmarkablePageLink<EventTypeEditor>(AggregationPage.class, Model.of("Event Aggregator")))
		 	.addButton(new MenuBookmarkablePageLink<CorrelationPage>(CorrelationPage.class, Model.of("Event Correlator")))
		 	.setIconType(IconType.cog).add(new DropDownAutoOpen());		
	}
	
	 private Component newQueriesDropDownButton() {
		 return new SushiNavBarDropDownButton(Model.of("Queries"))
		 	.addButton(new MenuBookmarkablePageLink<OnDemandQueryEditor>(OnDemandQueryEditor.class, Model.of("On-Demand")))
		 	.addButton(new MenuBookmarkablePageLink<LiveQueryEditor>(LiveQueryEditor.class, Model.of("Live")).setIconType(IconType.time))
		 	.addButton(new MenuBookmarkablePageLink<BPMNQueryEditor>(BPMNQueryEditor.class, Model.of("BPMN")).setIconType(IconType.leaf))
		 	.setIconType(IconType.thlarge).add(new DropDownAutoOpen());
	 }
	 
	 private Component newEventProducerDropDownButton() {
		 return new SushiNavBarDropDownButton(Model.of("Event Producing"))
		 	.addButton(new MenuBookmarkablePageLink<SimulationPage>(SimulationPage.class, Model.of("Simulator")))
		 	.addButton(new MenuBookmarkablePageLink<AdapterPage>(AdapterPage.class, Model.of("Adapter")))
		 	.setIconType(IconType.wrench).add(new DropDownAutoOpen());
	 }
	 
	 private Component newMonitoringDropDownButton() {
		 return new SushiNavBarDropDownButton(Model.of("Monitoring"))
		 	.addButton(new MenuBookmarkablePageLink<VisualisationPage>(VisualisationPage.class, Model.of("Visualisation")))
		 	.addButton(new MenuBookmarkablePageLink<BPMNMonitoringPage>(BPMNMonitoringPage.class, Model.of("BPMN")).setIconType(IconType.leaf))
		 	.addButton(new MenuBookmarkablePageLink<NotificationPage>(NotificationPage.class, Model.of("Notification")))
		 	.add(new DropDownAutoOpen());
	 }
	
	private void addFeedbackPanel() {
		feedbackPanel = new NotificationPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
        add(feedbackPanel);
	}

	public NotificationPanel getFeedbackPanel() {
		return feedbackPanel;
	}

	public void setFeedbackPanel(NotificationPanel feedbackPanel) {
		this.feedbackPanel = feedbackPanel;
	}
	 
}
