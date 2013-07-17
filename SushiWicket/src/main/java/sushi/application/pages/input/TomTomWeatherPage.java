package sushi.application.pages.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sushi.application.components.form.WarnOnExitForm;
import sushi.application.pages.AbstractSushiPage;
import sushi.csv.importer.CSVImporter;
import sushi.edifact.importer.EdifactImporter;
import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelImporter;
import sushi.excel.importer.FileNormalizer;
import sushi.xml.importer.XMLParser;
import sushi.xml.importer.XMLParsingException;

public class TomTomWeatherPage extends AbstractSushiPage {

	private static final long serialVersionUID = 1L;

	private SushiStreamProcessingAdapter sushiEsper = SushiStreamProcessingAdapter.getInstance();
	private AjaxButton activateWeather;
	private AjaxButton activateTomTom;
	private AjaxButton deactivateWeather;
	private AjaxButton deactivateTomTom;
	private RadioChoice weatherOnOff;
	private RadioChoice trafficOnOff;
	private static final List<String> namingButtons = Arrays.asList(new String[] { "on", "off"});
	private static final Map<String, Boolean> mapChoiceToBool;
	private static final Map<Boolean, String> mapBoolToChoice;
    static {
        Map<String, Boolean> aMap = new HashMap<String, Boolean>();
        aMap.put("on", true);
        aMap.put("off", false);
        mapChoiceToBool = Collections.unmodifiableMap(aMap);
        Map<Boolean, String> aMap2 = new HashMap<Boolean, String>();
        aMap2.put(true, "on");
        aMap2.put(false, "off");
        mapBoolToChoice = Collections.unmodifiableMap(aMap2);
    }
    
    
	private String selectedTrafficChoice = mapBoolToChoice.get(SushiStreamProcessingAdapter.getInstance().isActivatedTomTomAdapter());
	private String selectedWeatherChoice = mapBoolToChoice.get(SushiStreamProcessingAdapter.getInstance().isActivatedWeatherAdapter());

	@SuppressWarnings("unchecked")
	public TomTomWeatherPage() {
		super();

		WarnOnExitForm form = new WarnOnExitForm("form") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
			}
		};

		form.setMultiPart(true);
		// uploadForm.setMaxSize(Bytes.megabytes(2));

			
//			@Override
//			public void onSubmit(){
//				super.onSubmit();
//				System.out.println("##############################################################");
//				SushiEsper.getInstance().setActivatedWeatherAdapter(true);
//			}
		form.add(weatherOnOff = new RadioChoice<String>("weatherOnOff", new PropertyModel<String>(this, "selectedWeatherChoice"), namingButtons));
		form.add(trafficOnOff = new RadioChoice<String>("trafficOnOff", new PropertyModel<String>(this, "selectedTrafficChoice"), namingButtons));
		form.add(new AjaxButton("save"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form){
				super.onSubmit(target, form);
				SushiStreamProcessingAdapter.getInstance().setActivatedWeatherAdapter(mapChoiceToBool.get(selectedWeatherChoice));
				SushiStreamProcessingAdapter.getInstance().setActivatedTomTomAdapter(mapChoiceToBool.get(selectedTrafficChoice));
			}
		});
		add(form);
		
	}
}
