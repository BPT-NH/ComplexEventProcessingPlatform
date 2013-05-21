package sushi.application.pages.monitoring.visualisation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.ui.slider.AjaxSlider;

import sushi.application.pages.eventrepository.eventtypeeditor.model.EventTypeNamesProvider;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.visualisation.SushiChartTypeEnum;
import sushi.visualisation.SushiSimpleChartOptions;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;

public class ChartConfigurationPanel extends Panel{
	
	private static final long serialVersionUID = 1L;
		
	private DropDownChoice<String> eventTypeSelect;
	private DropDownChoice<SushiAttribute> attributeSelect;
	private EventTypeNamesProvider eventTypeNameProvider = new EventTypeNamesProvider();
	List<SushiAttribute> attributes = new ArrayList<SushiAttribute>();
	private VisualisationPage parentPage;
	private String selectedEventTypeName = "";
	private SushiAttribute selectedAttribute = null;
	private String chartTitle = "";
	private List<SushiChartTypeEnum> chartTypes = Arrays.asList(SushiChartTypeEnum.values());
	private IModel<SushiChartTypeEnum> chartType = Model.of(SushiChartTypeEnum.BAR);
	private TextField<String> chartTitleInput;
	private DropDownChoice<SushiChartTypeEnum> chartTypeSelect;

	private ChartConfigurationPanel panel;
	
	private NotificationPanel feedbackPanel;
	private SushiEventType selectedEventType;
	private Form<Void> layoutForm;
	private AjaxSlider slider;
	private Integer sliderValue = 1;
	private Label sliderLabel;
	private WebMarkupContainer sliderContainer;

	public ChartConfigurationPanel(String id, VisualisationPage visualisationPage){
		super(id);
		this.panel = this;
		
		this.parentPage = visualisationPage;
		
		layoutForm = new Form<Void>("layoutForm");
		add(layoutForm);
		addFeedbackPanel(layoutForm);
		
		layoutForm.add(addChartTitleInput());
		layoutForm.add(addChartTypeSelect());
		layoutForm.add(addEventTypeSelect());
		updateAttributes();
		layoutForm.add(addAttributeSelect());
		
		sliderContainer = new WebMarkupContainer("sliderDiv") {
			public boolean isVisible() {
		        return isSliderVisible();
			};
		};
		sliderContainer.setOutputMarkupPlaceholderTag(true);
		layoutForm.add(sliderContainer);
		sliderContainer.add(addSlider());
		sliderContainer.add(addSliderLabel());
		
		addButtonsToForm(layoutForm);
	}
	
	private Component addSlider() {		
	
		slider = new AjaxSlider("slider", 1, 100) {
			public boolean isVisible() {
		        return isSliderVisible();
		    }
		};
		slider.setOutputMarkupPlaceholderTag(true);
		slider.setAjaxStopEvent(new AjaxSlider.ISliderAjaxEvent() {
    		
    		private static final long serialVersionUID = 1L;

    		public void onEvent(AjaxRequestTarget target, AjaxSlider slider1, int value, int[] values) {
				sliderValue = value;
				sliderLabel.detach();
				target.add(sliderLabel);
    		}    		
			
    	});
		return slider;
	}
	
	private Component addSliderLabel() {
		sliderLabel = new Label("sliderLabel", new PropertyModel<Integer>(this, "sliderValue")) {
			public boolean isVisible() {
		        return isSliderVisible();
			}
		};
		sliderLabel.setOutputMarkupPlaceholderTag(true);
//		sliderLabel.add( new AjaxFormComponentUpdatingBehavior( "onchange") {
//
//	        @Override
//	        protected void onUpdate(AjaxRequestTarget target) {
//				slider.setValue(sliderValue);
//				slider.detach();
//				add(slider);
//				System.out.println(slider.getValue());
//				//TODO: This does change the value but not the position of the handle and resutls in an error.
//	        }
//	    });
		return sliderLabel;
	}

	public void updateSlider() {
		if (isSliderInvisible()) {
			return;
		}
		long min = SushiEvent.getMinOfAttributeValue(selectedAttribute.getName(), selectedEventType);
		long max = SushiEvent.getMaxOfAttributeValue(selectedAttribute.getName(), selectedEventType);
		slider.setMax(Math.abs(max) + Math.abs(min));
		System.out.println("updateTrue: " + sliderContainer.isVisible());
		sliderContainer.detach();
	}
	
	public boolean isSliderVisible() {
		return (selectedAttribute != null && chartType != null && selectedAttribute.getType()==SushiAttributeTypeEnum.INTEGER && chartType.getObject() == SushiChartTypeEnum.BAR);
	}

	public boolean isSliderInvisible() {
		return !isSliderVisible();
	}

	
	private void addFeedbackPanel(Form<Void> layoutForm) {
		feedbackPanel = new NotificationPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
        layoutForm.add(feedbackPanel);
	}
	
	private Component addEventTypeSelect() {
		eventTypeSelect = new DropDownChoice<String>("eventTypeSelect", new PropertyModel<String>(this, "selectedEventTypeName"), eventTypeNameProvider);
		eventTypeSelect.setOutputMarkupId(true);
		
		eventTypeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 

			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				selectedEventType = SushiEventType.findByTypeName(selectedEventTypeName);
				updateAttributes();
				target.add(attributeSelect);
			}
		});
		return eventTypeSelect;
	}

	private Component addChartTypeSelect() {
		chartTypeSelect = new DropDownChoice<SushiChartTypeEnum>("chartTypeSelect", chartType, chartTypes);
		chartTypeSelect.setOutputMarkupId(true);
		
		chartTypeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 

			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				updateAttributes();
				updateSlider();
				target.add(attributeSelect);
				target.add(sliderContainer);
			}
		});
		return chartTypeSelect;
	}

	
	private Component addAttributeSelect() {
		updateAttributes();
		attributeSelect = new DropDownChoice<SushiAttribute>("attributeSelect", new PropertyModel<SushiAttribute>(this, "selectedAttribute"), attributes);
		attributeSelect.setOutputMarkupId(true);
		attributeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 
			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				updateSlider();
				target.add(sliderContainer);
			}
		});

		return attributeSelect;
	}
	
	private void updateAttributes() {
		attributes.clear();
		//attribute für eventtyp sammeln
		if(selectedEventType != null){
			if (chartType.getObject() == null) {
				attributes.addAll(selectedEventType.getValueTypes());
			} else if (chartType.getObject() ==  SushiChartTypeEnum.BAR) {
				//BarChart only for string or Integer
				for (SushiAttribute attribute : selectedEventType.getValueTypes()) {
					if (attribute.getType() == SushiAttributeTypeEnum.STRING || attribute.getType() == SushiAttributeTypeEnum.INTEGER)
						attributes.add(attribute);
				}
			} else if (chartType.getObject() == SushiChartTypeEnum.SPLATTER) {
				//SplatterChart only integer
				for (SushiAttribute attribute : selectedEventType.getValueTypes()) {
					if (attribute.getType() == SushiAttributeTypeEnum.INTEGER)
						attributes.add(attribute);
				}
			}
		}
	}
	
	private Component addChartTitleInput() {
		chartTitleInput = new TextField<String>("chartTitleInput", new PropertyModel<String>(this, "chartTitle"));
		return chartTitleInput;
	}
	
	private void addButtonsToForm(Form<Void> layoutForm) {
		
	    AjaxButton createButton = new AjaxButton("createButton") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				panel.getFeedbackPanel().setVisible(true);
				boolean error = false;
				
				if (chartTitle == null) {
					panel.getFeedbackPanel().error("Chart needs a title!");
					panel.getFeedbackPanel().setVisible(true);
					target.add(panel.getFeedbackPanel());
					error = true;
				};
				if(selectedEventType == null){
					panel.getFeedbackPanel().error("eventType must be chosen!"); 
					panel.getFeedbackPanel().setVisible(true);
					target.add(panel.getFeedbackPanel());
					error = true;
				};
				if (chartType == null) {
					panel.getFeedbackPanel().error("Choose a chart type!");
					panel.getFeedbackPanel().setVisible(true);
					target.add(panel.getFeedbackPanel());
					error = true;
				};
				if (selectedAttribute == null) {
					panel.getFeedbackPanel().error("Choose an attribute!");
					panel.getFeedbackPanel().setVisible(true);
					target.add(panel.getFeedbackPanel());
					error = true;
				};
				
				if (error == false) {
					//create new Chart configuration
					String attributeName = selectedAttribute.getAttributeExpression();
					SushiAttributeTypeEnum attributeType = selectedAttribute.getType();
					SushiSimpleChartOptions newOptions = new SushiSimpleChartOptions(selectedEventType, attributeName, attributeType, chartTitle, chartType.getObject(), sliderValue);
					newOptions.save();
					VisualisationPage visualisation = parentPage;
					visualisation.getOptions().detach();
					target.add(visualisation.listview.getParent());
					//close this Panel
					visualisation.addChartModal.close(target);
				};
				};
			};
		
			layoutForm.add(createButton);
	}
	
		public NotificationPanel getFeedbackPanel() {
			return feedbackPanel;
		} 

}
