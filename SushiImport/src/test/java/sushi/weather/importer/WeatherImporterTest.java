package sushi.weather.importer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.xml.importer.XMLParsingException;


public class WeatherImporterTest {

	private DWDImporter weatherImporter;

	@Before
	public void setUp() throws Exception {
		weatherImporter = new DWDImporter();
	}
	
	@Test
	public void testDoesNotCreateTheSameEventsTwice() throws XMLParsingException, IOException{
		// initial generation of events
		weatherImporter.getNewWeatherEvents();
		ArrayList<SushiEvent> events = weatherImporter.getNewWeatherEvents();
		assertTrue("should generate 0 Events, but generated " + events.size(), events.size() == 0);
	}
	
	@Test
	public void testDeletesOldFiles() throws IOException, XMLParsingException{
		File testFile = new File(System.getProperty("user.dir") + "/bin/weatherXML/testFile.xml");
		testFile.createNewFile();
		System.out.println(testFile.getAbsolutePath());
		
		File downloadFolder = new File(System.getProperty("user.dir") + "/bin/weatherXML/");
		boolean fileExist = false;
		for (File fileInDLFolder : downloadFolder.listFiles()){
			if (fileInDLFolder.getName().equals("testFile.xml")) fileExist = true;
		}
		assertTrue("testfile was not correctly created", fileExist);
		weatherImporter.getNewWeatherEvents();
		
		for (File fileInDLFolder : downloadFolder.listFiles()){
			if (fileInDLFolder.getName().equals("testFile.xml")) throw new AssertionFailedError("file was not deleted");
		}
	}
}
