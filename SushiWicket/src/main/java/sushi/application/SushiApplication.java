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

import sushi.aggregation.SushiAggregation;
import sushi.aggregation.SushiAggregationRule;
import sushi.application.images.ImageReference;
import sushi.application.pages.main.MainPage;
import sushi.application.pages.user.LoginPage;
import sushi.esper.SushiEsper;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
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

public class SushiApplication extends WebApplication {

	private SushiEsper sushiEsper;
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

		sushiEsper = SushiEsper.getInstance();
//		initializeEventTypesForShowCase();
		SushiAggregation.getInstance();
		
		setAuthorizationStrategy();
		
	}

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

	private void mountImages() {
		ResourceReference alignmentImage = new PackageResourceReference(ImageReference.class, "alignment.jpg");
		mountResource("/images/alignment", alignmentImage);
		
		ResourceReference eventStreamImage = new PackageResourceReference(ImageReference.class, "eventStream.jpg");
		mountResource("/images/eventStream", alignmentImage);
		
		ResourceReference groupImage = new PackageResourceReference(ImageReference.class, "group.jpg");
		mountResource("/images/group", alignmentImage);
		
		ResourceReference processImage = new PackageResourceReference(ImageReference.class, "process.jpg");
		mountResource("/images/process", alignmentImage);
	}

	private void initializeEventTypesForShowCase() {
		List<SushiAttribute> attributes;
		SushiEventType eventType;
		SushiAttribute rootAttribute1 = new SushiAttribute("Containernummer", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute2 = new SushiAttribute("TimeDifference", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute rootAttribute3 = new SushiAttribute("ATA", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute4 = new SushiAttribute("ETA", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute6 = new SushiAttribute("Timestamp End", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute7 = new SushiAttribute("Driver", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute8 = new SushiAttribute("Location", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute9 = new SushiAttribute("Truck", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute10 = new SushiAttribute("Last Stop", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute11 = new SushiAttribute("Action", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute12 = new SushiAttribute("Timestamp Second Location", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute13 = new SushiAttribute("Origin", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute14 = new SushiAttribute("Destination", SushiAttributeTypeEnum.STRING);
		SushiAttribute rootAttribute15 = new SushiAttribute("Truck Usage Start", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute16 = new SushiAttribute("Truck Usage End", SushiAttributeTypeEnum.DATE);
		SushiAttribute rootAttribute17 = new SushiAttribute("Distance in km", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute rootAttribute18 = new SushiAttribute("Time approx", SushiAttributeTypeEnum.INTEGER);
		SushiAttribute rootAttribute19 = new SushiAttribute("Total distance in km", SushiAttributeTypeEnum.INTEGER);
		
		if (SushiEventType.findByTypeName("ScheduledArrivalEvent") == null) {
			attributes = Arrays.asList(rootAttribute1);
			eventType = new SushiEventType("ScheduledArrivalEvent", attributes, "ETA seavessel");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("ActualArrivalEvent") == null) {
			attributes = Arrays.asList(rootAttribute1);
			eventType = new SushiEventType("ActualArrivalEvent", attributes, "ATA seavessel");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("TimeDiffMoreThan24H") == null) {
			attributes = Arrays.asList(rootAttribute1, rootAttribute2, rootAttribute3, rootAttribute4);
			eventType = new SushiEventType("TimeDiffMoreThan24H", attributes, "ATA seavessel");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("truckUsage") == null) {
			attributes = Arrays.asList(rootAttribute7, rootAttribute9, rootAttribute11);
			eventType = new SushiEventType("truckUsage", attributes, "Timestamp");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("obuEvent") == null) {
			attributes = Arrays.asList(rootAttribute7, rootAttribute8);
			eventType = new SushiEventType("obuEvent", attributes, "Timestamp");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("truckUsageInterval") == null) {
			attributes = Arrays.asList(rootAttribute6, rootAttribute7, rootAttribute9);
			eventType = new SushiEventType("truckUsageInterval", attributes, "Timestamp Begin");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("enrichedObuEvent") == null) {
			attributes = Arrays.asList(rootAttribute15, rootAttribute9, rootAttribute7, rootAttribute8);
			eventType = new SushiEventType("enrichedObuEvent", attributes, "Timestamp");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("DrivenRoute") == null) {
			attributes = Arrays.asList(rootAttribute12, rootAttribute13, rootAttribute14, rootAttribute15, rootAttribute7, rootAttribute9, rootAttribute17);
			eventType = new SushiEventType("DrivenRoute", attributes, "Timestamp First Location");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("TruckRoute") == null) {
			attributes = Arrays.asList(rootAttribute13, rootAttribute14, rootAttribute17, rootAttribute18);
			eventType = new SushiEventType("TruckRoute", attributes, "Import time");
			Broker.send(eventType);
		}
		
		if (SushiEventType.findByTypeName("TraveledDistance") == null) {
			attributes = Arrays.asList(rootAttribute15, rootAttribute16, rootAttribute9, rootAttribute7, rootAttribute19);
			eventType = new SushiEventType("TraveledDistance", attributes, "Time of Detection");
			Broker.send(eventType);
		}
		
//		if (SushiEventType.findByTypeName("tenTruckStops") == null) {
//			attributes = Arrays.asList(rootAttribute7, rootAttribute9, rootAttribute10);
//			eventType = new SushiEventType("tenTruckStops", attributes, "First Stop");
//			Broker.send(eventType);
//		}
	}

	public SushiEsper getSushiEsper() {
		return sushiEsper;
	}

	public void setSushiEsper(SushiEsper sushiEsper) {
		this.sushiEsper = sushiEsper;
	}
	
	 /**
     * configure all resource bundles (css and js)
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