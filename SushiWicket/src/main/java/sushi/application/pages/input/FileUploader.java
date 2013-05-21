package sushi.application.pages.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sushi.application.pages.AbstractSushiPage;
import sushi.csv.importer.CSVNormalizer;
import sushi.edifact.importer.EdifactParser;
import sushi.esper.SushiEsper;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.eventhandling.Broker;
import sushi.excel.importer.ExcelNormalizer;
import sushi.excel.importer.FileNormalizer;
import sushi.xml.importer.XMLParser;
import sushi.xml.importer.XMLParsingException;

public class FileUploader extends AbstractSushiPage {

	private static final long serialVersionUID = 1L;

	private SushiEsper sushiEsper = SushiEsper.getInstance();
	private FileUploadField fileUpload;

	public FileUploader() {
		super();

		Form<Void> uploadForm = new Form<Void>("uploadForm") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				final FileUpload uploadedFile = fileUpload.getFileUpload();
				if (uploadedFile != null) {
					String uploadFolder;

					if (System.getProperty("os.name").contains("Windows")) {
						uploadFolder = "C:\\temp\\";
					}
					else {
						uploadFolder = "/tmp/";
					}

					String fileName = uploadedFile.getClientFileName();
					File newFile = new File(uploadFolder + fileName);

					if (newFile.exists()) {
						newFile.delete();
					}

					try {
						newFile.createNewFile();
						uploadedFile.writeTo(newFile);
//						info("Saved file: " + fileName);
					} catch (IOException e) {
						error("Error: File could not be saved.");
					}

					int index = fileName.lastIndexOf('.');
//					String fileNameWithoutExtension = fileName.substring(0,index);
					String fileExtension = fileName.substring(index + 1, fileName.length());
					if (fileExtension.toLowerCase().contains("xls") || fileExtension.toLowerCase().contains("csv")) { 
						PageParameters pageParameters = new PageParameters();
						pageParameters.add("filePath", newFile.getAbsolutePath());
						if (noEventTypesFound(pageParameters)) {
							System.out.println("no matching types");
							setResponsePage(ExcelEventTypeCreator.class, pageParameters);
						} else {
							setResponsePage(ExcelEventTypeMatcher.class, pageParameters);
						}
					} else if(fileExtension.toLowerCase().contains("xsd")){
						PageParameters pageParameters = new PageParameters();
						pageParameters.add("filePath", newFile.getAbsolutePath());
						setResponsePage(XSDEventTypeCreator.class, pageParameters);
					} else if(fileExtension.toLowerCase().contains("xml")){
						SushiEvent uploadedEvent;
						try {
							uploadedEvent = XMLParser.generateEventFromXML(newFile.getAbsolutePath());
							Broker.send(uploadedEvent);
							info("Saved event from : " + fileName);
						} catch (XMLParsingException e) {
							error(e.getMessage());
						}
					} else if(fileExtension.toLowerCase().contains("txt") || fileExtension.toLowerCase().contains("edi")){
						SushiEvent uploadedEvent;
						try {
							uploadedEvent = EdifactParser.getInstance().generateEventFromEdifact(newFile.getAbsolutePath());
							if (!sushiEsper.isEventType(uploadedEvent.getEventType())) {
								Broker.send(uploadedEvent.getEventType());
							}
							Broker.send(uploadedEvent);
							info("Saved event from : " + fileName);
						} catch (XMLParsingException e) {
							error(e.getMessage());
						} catch (Exception e) {
							error(e.getMessage());
						}
					}

				} else {
					error("File not found");
				}
			}
		};

		uploadForm.setMultiPart(true);
		// uploadForm.setMaxSize(Bytes.megabytes(2));

		uploadForm.add(fileUpload = new FileUploadField("fileUpload"));
		add(uploadForm);
	}

	public static boolean noEventTypesFound(PageParameters pageParameters) {
		FileNormalizer fileNormalizer;
		String fileName = pageParameters.get("filePath").toString();
		int index = fileName.lastIndexOf('.');
		String fileExtension = fileName.substring(index + 1, fileName.length());

		fileNormalizer = (fileExtension.toLowerCase().contains("xls")) ? new ExcelNormalizer() : new CSVNormalizer();
		
		List<String> attributeNames = fileNormalizer.getColumnTitlesFromFile(pageParameters.get("filePath").toString());
		List<String> trimmedAttributeNames = new ArrayList<String>();
		for (String attributeName : attributeNames) {
			trimmedAttributeNames.add(attributeName.replace(" +", "_"));
		}
		return SushiEventType.findMatchingEventTypes(trimmedAttributeNames, ExcelEventTypeCreator.GENERATED_TIMESTAMP_COLUMN_NAME).isEmpty();
	}
}
