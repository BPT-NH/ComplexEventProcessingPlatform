package sushi.application;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import sushi.application.images.ImageReference;
import sushi.application.pages.main.MainPage;
import sushi.application.pages.user.LoginPage;
import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.transformation.TransformationManager;
import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.html5player.Html5PlayerCssReference;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.html5player.Html5PlayerJavaScriptReference;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.icon.OpenWebIconsCssReference;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.jqueryui.JQueryUIJavaScriptReference;
import de.agilecoders.wicket.markup.html.references.BootstrapPrettifyCssReference;
import de.agilecoders.wicket.markup.html.references.BootstrapPrettifyJavaScriptReference;
import de.agilecoders.wicket.markup.html.references.ModernizrJavaScriptReference;
import de.agilecoders.wicket.markup.html.themes.metro.MetroTheme;
import de.agilecoders.wicket.settings.BootstrapSettings;
import de.agilecoders.wicket.settings.BootswatchThemeProvider;
import de.agilecoders.wicket.settings.ThemeProvider;

/**
 * The controller for the web application. Most of the initialization is done here.
 * @author micha
 */
public class SushiApplication extends WebApplication {

	private SushiStreamProcessingAdapter sushiEsper;
	private BootstrapSettings bootStrapSettings;

	@Override
	public Class<? extends WebPage> getHomePage() {
		return MainPage.class;
	}
	
	 @Override
	 public Session newSession(Request request, Response response){
		 return new SushiAuthenticatedSession(request);
	 }

	@Override
	public void init() {
		super.init();
		
		getMarkupSettings().setStripWicketTags(true);

		bootStrapSettings = new BootstrapSettings();
		bootStrapSettings.minify(true); // use minimized version of all bootstrap
		
		ThemeProvider themeProvider = new BootswatchThemeProvider() {{
            add(new MetroTheme());
            defaultTheme("cerulean");
        }};
        bootStrapSettings.setThemeProvider(themeProvider);

		Bootstrap.install(this, bootStrapSettings);
		
		mountImages();

		sushiEsper = SushiStreamProcessingAdapter.getInstance();
//		initializeEventTypesForShowCase();
		TransformationManager.getInstance();
		
		setAuthorizationStrategy();
		
	}

	/**
	 * Initializes the authorization strategy for the web application. 
	 * Pages, which implement the {@link SushiAuthenticatedWebPage} interface, are only accessible for authenticated users.
	 */
	private void setAuthorizationStrategy() {
		getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy(){
			
            public boolean isActionAuthorized(Component component, Action action){
                // authorize everything
                return true;
            }

            public <T extends IRequestableComponent> boolean isInstantiationAuthorized(Class<T> componentClass){
                // Check if the new Page requires authentication (implements the marker interface)
                if (SushiAuthenticatedWebPage.class.isAssignableFrom(componentClass)){
                    // Is user signed in?
                    if (((SushiAuthenticatedSession)Session.get()).isSignedIn())
                    {
                        // okay to proceed
                        return true;
                    }

                    // Intercept the request, but remember the target for later.
                    // Invoke Component.continueToOriginalDestination() after successful logon to
                    // continue with the target remembered.

                    throw new RestartResponseAtInterceptPageException(LoginPage.class);
                }

                // okay to proceed
                return true;
            }
        });
	}

	/**
	 * Loads the images for the web application at start-up.
	 */
	private void mountImages() {
		ResourceReference alignmentImage = new PackageResourceReference(ImageReference.class, "alignment.jpg");
		mountResource("/images/alignment", alignmentImage);
		
		ResourceReference eventStreamImage = new PackageResourceReference(ImageReference.class, "eventStream.jpg");
		mountResource("/images/eventStream", eventStreamImage);
		
		ResourceReference groupImage = new PackageResourceReference(ImageReference.class, "group.jpg");
		mountResource("/images/group", groupImage);
		
		ResourceReference processImage = new PackageResourceReference(ImageReference.class, "process.jpg");
		mountResource("/images/process", processImage);
	}

	private void initializeEventTypesForShowCase() {
		List<SushiAttribute> attributes;
		SushiEventType eventType;

		if (SushiEventType.findByTypeName("ScheduledArrivalEvent") == null) {
			attributes = Arrays.asList(
						new SushiAttribute("Containernummer", SushiAttributeTypeEnum.STRING)
					);
			eventType = new SushiEventType("ScheduledArrivalEvent", attributes, "ETA seavessel");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("ActualArrivalEvent") == null) {
			attributes = Arrays.asList(
					new SushiAttribute("Containernummer", SushiAttributeTypeEnum.STRING)
				);
			eventType = new SushiEventType("ActualArrivalEvent", attributes, "ATA seavessel");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("TimeDiffMoreThan24H") == null) {
			attributes = Arrays.asList(
					new SushiAttribute("Containernummer", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("TimeDifference", SushiAttributeTypeEnum.INTEGER), 
					new SushiAttribute("ATA", SushiAttributeTypeEnum.DATE), 
					new SushiAttribute("ETA", SushiAttributeTypeEnum.DATE)
				);
			eventType = new SushiEventType("TimeDiffMoreThan24H", attributes, "ATA seavessel");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("truckUsage") == null) {
			attributes = Arrays.asList(
					new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Action", SushiAttributeTypeEnum.STRING)
				);
			eventType = new SushiEventType("truckUsage", attributes, "Timestamp");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("obuEvent") == null) {
			attributes = Arrays.asList(
					new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Location", SushiAttributeTypeEnum.STRING)
				);
			eventType = new SushiEventType("obuEvent", attributes, "Timestamp");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("truckUsageInterval") == null) {
			attributes = Arrays.asList(
					new SushiAttribute("Timestamp End", SushiAttributeTypeEnum.DATE), 
					new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING)
				);
			eventType = new SushiEventType("truckUsageInterval", attributes, "Timestamp Begin");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("enrichedObuEvent") == null) {
			attributes = Arrays.asList(
					new SushiAttribute("Truck Usage Start", SushiAttributeTypeEnum.DATE), 
					new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Location", SushiAttributeTypeEnum.STRING)
				);
			eventType = new SushiEventType("enrichedObuEvent", attributes, "Timestamp");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("DrivenRoute") == null) {
			attributes = Arrays.asList(
					new SushiAttribute("Timestamp Second Location", SushiAttributeTypeEnum.DATE), 
					new SushiAttribute("Origin", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Destination", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Truck Usage Start", SushiAttributeTypeEnum.DATE), 
					new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Distance in km", SushiAttributeTypeEnum.INTEGER)
				);
			eventType = new SushiEventType("DrivenRoute", attributes, "Timestamp First Location");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("TruckRoute") == null) {
			attributes = Arrays.asList(
					new SushiAttribute("Origin", SushiAttributeTypeEnum.STRING),
					new SushiAttribute("Destination", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Distance in km", SushiAttributeTypeEnum.INTEGER), 
					new SushiAttribute("Time approx", SushiAttributeTypeEnum.INTEGER)
				);
			eventType = new SushiEventType("TruckRoute", attributes, "Import time");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("TraveledDistance") == null) {
			attributes = Arrays.asList(
					new SushiAttribute("Truck Usage Start", SushiAttributeTypeEnum.DATE), 
					new SushiAttribute("Truck Usage End", SushiAttributeTypeEnum.DATE), 
					new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Total distance in km", SushiAttributeTypeEnum.INTEGER)
				);
			eventType = new SushiEventType("TraveledDistance", attributes, "Time of Detection");
			Broker.send(eventType);
		}
		
//		if (SushiEventType.findByTypeName("tenTruckStops") == null) {
//			attributes = Arrays.asList(
//					new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING), 
//					new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING), 
//					new SushiAttribute("Last Stop", SushiAttributeTypeEnum.DATE)
//				);
//			eventType = new SushiEventType("tenTruckStops", attributes, "First Stop");
//			Broker.send(eventType);
//		}
		
//		if (SushiEventType.findByTypeName("Event1") == null) {
//			attributes = Arrays.asList(
//					new SushiAttribute("Attribute_A", SushiAttributeTypeEnum.INTEGER), 
//					new SushiAttribute("Attribute_B", SushiAttributeTypeEnum.STRING)
//				);
//			eventType = new SushiEventType("Event1", attributes, "Timestamp");
//			Broker.send(eventType);
//		}
//		
//		if (SushiEventType.findByTypeName("Event2") == null) {
//			attributes = Arrays.asList(
//					new SushiAttribute("Attribute_C", SushiAttributeTypeEnum.STRING), 
//					new SushiAttribute("Attribute_D", SushiAttributeTypeEnum.DATE), 
//					new SushiAttribute("Attribute_E", SushiAttributeTypeEnum.INTEGER)
//				);
//			eventType = new SushiEventType("Event2", attributes, "Timestamp");
//			Broker.send(eventType);
//		}
//		
//		if (SushiEventType.findByTypeName("Event3") == null) {
//			attributes = Arrays.asList(
//					new SushiAttribute("Attribute_F", SushiAttributeTypeEnum.INTEGER), 
//					new SushiAttribute("Attribute_G", SushiAttributeTypeEnum.STRING)
//				);
//			eventType = new SushiEventType("Event3", attributes, "Timestamp");
//			Broker.send(eventType);
//		}
		
		if (SushiEventType.findByTypeName("KinoRating") == null) {
			attributes = Arrays.asList(
					new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER), 
					new SushiAttribute("Rating", SushiAttributeTypeEnum.STRING)
				);
			eventType = new SushiEventType("KinoRating", attributes, "Timestamp");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("KinoFilme") == null) {
			attributes = Arrays.asList(
					new SushiAttribute("Location", SushiAttributeTypeEnum.INTEGER), 
					new SushiAttribute("Movie", SushiAttributeTypeEnum.STRING), 
					new SushiAttribute("Action", SushiAttributeTypeEnum.STRING)
				);
			eventType = new SushiEventType("KinoFilme", attributes, "Timestamp");
			Broker.send(eventType);
		}
	}

	/**
	 * Gets the adapter for the Esper event processing engine.
	 * @return
	 */
	public SushiStreamProcessingAdapter getSushiEsper() {
		return sushiEsper;
	}

	public void setSushiEsper(SushiStreamProcessingAdapter sushiEsper) {
		this.sushiEsper = sushiEsper;
	}
	
	 /**
     * Configure all resource bundles (css and js).
     * The resource bundles also include the bootstrap css und js.
     */
    private void configureResourceBundles() {
        getResourceBundles().addJavaScriptBundle(SushiApplication.class, "core.js",
                                                 (JavaScriptResourceReference) getJavaScriptLibrarySettings().getJQueryReference(),
                                                 (JavaScriptResourceReference) getJavaScriptLibrarySettings().getWicketEventReference(),
                                                 (JavaScriptResourceReference) getJavaScriptLibrarySettings().getWicketAjaxReference(),
                                                 (JavaScriptResourceReference) ModernizrJavaScriptReference.INSTANCE
        );

        getResourceBundles().addJavaScriptBundle(SushiApplication.class, "bootstrap.js",
                                                 (JavaScriptResourceReference) bootStrapSettings.getJsResourceReference(),
                                                 (JavaScriptResourceReference) BootstrapPrettifyJavaScriptReference.INSTANCE
        );

        getResourceBundles().addJavaScriptBundle(SushiApplication.class, "bootstrap-extensions.js",
                                                 JQueryUIJavaScriptReference.instance(),
                                                 Html5PlayerJavaScriptReference.instance()
        );

        getResourceBundles().addCssBundle(SushiApplication.class, "bootstrap-extensions.css",
                                          Html5PlayerCssReference.instance(),
                                          OpenWebIconsCssReference.instance()
        );

        if (bootStrapSettings.useResponsiveCss()) {
            getResourceBundles().addCssBundle(SushiApplication.class, "application.css",
                                              (CssResourceReference) bootStrapSettings.getResponsiveCssResourceReference(),
                                              (CssResourceReference) BootstrapPrettifyCssReference.INSTANCE
            );
        } else {
            getResourceBundles().addCssBundle(SushiApplication.class, "application.css",
                                              (CssResourceReference) BootstrapPrettifyCssReference.INSTANCE
            );
        }
    }
}