package sushi.csv.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import sushi.csv.importer.CSVExporter;
import sushi.csv.importer.CSVNormalizer;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.util.SushiTestHelper;

public class CSVImportTest {

	
	private static String filePath = System.getProperty("user.dir")+"/src/test/resources/Kino.csv";
	private static String filePath2 = System.getProperty("user.dir")+"/src/test/resources/Kino2.csv";
	private static String filePathSmallKino = System.getProperty("user.dir")+"/src/test/resources/KinoSmall.csv";
	
	
	@Test
	public void testExtractionOfColumnTitles() {
		CSVNormalizer csvNormalizer = new CSVNormalizer();
		List<String> columnTitles = csvNormalizer.getColumnTitlesFromFile(filePath);
		assertTrue("Timestamp column could not be read.", columnTitles.contains("Timestamp"));
		assertTrue("Location column could not be read.", columnTitles.contains("Location"));
		assertTrue("Rating column could not be read.", columnTitles.contains("Rating"));
		
		List<String> columnTitles2 = csvNormalizer.getColumnTitlesFromFile(filePath2);
		assertTrue("Timestamp column could not be read.", columnTitles.contains("Timestamp"));
		assertTrue("Location column could not be read.", columnTitles.contains("Location"));
		assertTrue("Rating column could not be read.", columnTitles.contains("Rating"));
	}
	
	@Test
	public void testExtractionOfEvents() {
		CSVNormalizer csvNormalizer = new CSVNormalizer();
		List<String> columnTitles = csvNormalizer.getColumnTitlesFromFile(filePath);
		List<SushiAttribute> attributes = SushiTestHelper.createAttributes(columnTitles);
		List<SushiEvent> events = csvNormalizer.importEventsFromFile(filePath, attributes);
		assertTrue("Not the right number of events imported. Number should be 999 but was " + events.size(), events.size() == 999);
		
//		ArrayList<String> columnTitles2 = csvNormalizer.getColumnTitlesFromFile(filePath2);
//		ArrayList<SushiEvent> events2 = csvNormalizer.importEventsFromFile(filePath2, columnTitles2);
//		assertTrue("Not the right number of events imported. Number should be 999 but was " + events2.size(), events2.size() == 999);
	}
	
	@Test
	public void testImportAndExport() {
		CSVNormalizer csvNormalizer = new CSVNormalizer();
		List<String> columnTitles = csvNormalizer.getColumnTitlesFromFile(filePath);
		List<SushiAttribute> attributes = SushiTestHelper.createAttributes(columnTitles);
		List<SushiEvent> events = csvNormalizer.importEventsFromFile(filePath, attributes);
		SushiEventType eventType = new SushiEventType("Kino1", attributes, "Timestamp");
		eventType.save();
		for (SushiEvent e : events) {
			e.setEventType(eventType);
		}
		SushiEvent.save(events);
		CSVExporter exporter = new CSVExporter();
		exporter.generateExportFile(eventType, events);
	}
	
	@Test
	public void testExtractionOfMeanEvents() {
		CSVNormalizer csvNormalizer = new CSVNormalizer();
		List<String> columnTitles = csvNormalizer.getColumnTitlesFromFile(filePathSmallKino);
		assertTrue("Timestamp column could not be read.", columnTitles.contains("Timestamp"));
		assertTrue("Location column could not be read.", columnTitles.contains("Location"));
		assertTrue("Rating column could not be read.", columnTitles.contains("Rating"));
		List<SushiAttribute> attributes = SushiTestHelper.createAttributes(columnTitles);
		List<SushiEvent> events = csvNormalizer.importEventsFromFile(filePathSmallKino, attributes);
		assertTrue("Not the right number of events imported. Number should be 3 but was " + events.size(), events.size() == 3);
		
		SushiEvent event1 = events.get(0);
		assertEquals("re;d;", event1.getValues().get("Rating"));
		SushiEvent event2 = events.get(1);
		assertEquals("yel\"\"\"low", event2.getValues().get("Rating"));
		SushiEvent event3 = events.get(2);
		assertEquals("red", event3.getValues().get("Rating"));
		assertEquals(1, event3.getValues().get("Location"));
	}
	
}
