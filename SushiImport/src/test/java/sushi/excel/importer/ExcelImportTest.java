package sushi.excel.importer;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import sushi.event.SushiEvent;
import sushi.event.attribute.SushiAttribute;
import sushi.excel.importer.ExcelNormalizer;
import sushi.excel.importer.SushiImportEvent;
import sushi.util.SushiTestHelper;

public class ExcelImportTest {
	
	private static String filePath = System.getProperty("user.dir")+"/src/test/resources/Kino.xls";
	private static String cinemaSchedulePath = System.getProperty("user.dir")+"/src/test/resources/CinemaSchedule.xls";
	private static String cinemaScheduleWithoutTimestampPath = System.getProperty("user.dir")+"/src/test/resources/CinemaScheduleWithoutTime.xls";
	private static String formattedFilePathText = "/src/test/resources/Test-Case-Formatting-Text.xls";
	private static String formattedFilePathDateShort = "/src/test/resources/Test-Case-Formatting-Date-short.xls";
	private static String formattedFilePathDateLong = "/src/test/resources/Test-Case-Formatting-Date-long.xls";
	private static String emptyFilePath = System.getProperty("user.dir")+ "/src/test/resources/EmptySheet.xls";
	
	@Test
	public void testExtractionOfColumnTitles() {
		ExcelNormalizer excelNormalizer = new ExcelNormalizer();
		ArrayList<String> columnTitles = excelNormalizer.getColumnTitlesFromFile(filePath);
		assertTrue("Timestamp column could not be read.", columnTitles.contains("Timestamp"));
		assertTrue("Location column could not be read.", columnTitles.contains("Location"));
		assertTrue("Rating column could not be read.", columnTitles.contains("Rating"));
	}
	
	@Test
	public void testExtractionOfEvents() {
		ExcelNormalizer excelNormalizer = new ExcelNormalizer();
		List<String> columnTitles = excelNormalizer.getColumnTitlesFromFile(filePath);
		List<SushiAttribute> attributes = SushiTestHelper.createAttributes(columnTitles);
		List<SushiEvent> events = excelNormalizer.importEventsFromFile(filePath, attributes);
		assertTrue("Not the right number of events imported.", events.size() == 999);
		// 12: 30.10.2012 20:46	1 yellow
		SushiEvent testEvent1 = events.get(10);
		assertTrue("Date of event 1 does not match.", testEvent1.getTimestamp().compareTo(new Date(2012 - 1900, 9, 30, 20, 46, 58)) == 0);
		//ID is set when saving in DB
		//assertTrue("ID of event 1 (" + testEvent1.getID() +") does not match.", testEvent1.getID() == 10);
		System.out.println(testEvent1.getValues());
		assertTrue("Location of event 1 does not match.", testEvent1.getValues().get("Location").equals(1));
		assertTrue("Rating of event 1 is not right.", testEvent1.getValues().get("Rating").equals("yellow"));
		//28: 30.10.2012 20:27	1	red
		SushiEvent testEvent2 = events.get(26);
		assertTrue("Date of event 2 does not match.", testEvent2.getTimestamp().compareTo(new Date(2012 - 1900, 9, 30, 20, 27, 58)) == 0);
		//ID is set when saving in DB
		//assertTrue("ID of event 2 (" + testEvent2.getID() +") does not match.", testEvent2.getID() == 26);
		assertTrue("Rating of event 2 is not right.", testEvent2.getValues().get("Rating").equals("red"));
		
	}

	@Test
	public void testColumnSelection() {
		ExcelNormalizer excelNormalizer = new ExcelNormalizer();
//		ArrayList<String> columnTitles = excelNormalizer.getColumnTitlesFromXLS(filePath);
		ArrayList<String> columnTitles = new ArrayList<String>();
		columnTitles.add("Timestamp");
		columnTitles.add("Location");
		List<SushiAttribute> attributes = SushiTestHelper.createAttributes(columnTitles);
		List<SushiEvent> events = excelNormalizer.importEventsFromFile(filePath, attributes);
		assertTrue("Not the right number of events imported.", events.size() == 999);
		//12: 30.10.2012 20:46	1	yellow
		SushiEvent testEvent1 = events.get(10);
		assertTrue("Date of event 1 does not match.", testEvent1.getTimestamp().compareTo(new Date(2012 - 1900, 9, 30, 20, 46, 58)) == 0);
		//ID is set when saving in DB
		//assertTrue("ID of event 1 (" + testEvent1.getID() +") does not match.", testEvent1.getID() == 10);
		//28: 30.10.2012 20:27	1	red
		SushiEvent testEvent2 = events.get(26);
		assertTrue("Date of event 2 does not match.", testEvent2.getTimestamp().compareTo(new Date(2012 - 1900, 9, 30, 20, 27, 58)) == 0);
		//ID is set when saving in DB
		//assertTrue("ID of event 2 (" + testEvent2.getID() +") does not match.", testEvent2.getID() == 26);
	}
	
	@Test
	public void testBuggyExcelFile() {
		ExcelNormalizer excelNormalizer = new ExcelNormalizer();
		List<String> columnTitles = excelNormalizer.getColumnTitlesFromFile(cinemaSchedulePath);
		List<SushiAttribute> attributes = SushiTestHelper.createAttributes(columnTitles);
		List<SushiEvent> events = excelNormalizer.importEventsFromFile(cinemaSchedulePath, attributes);
		assertTrue("Not the right number of events imported. Should be 12, but were " + events.size(), events.size() == 12);
		System.out.println(events.get(0));
	}
	
	//TODO
	@Test
	public void testFormattedCellsText(){
//		ExcelNormalizer excelNormalizer = new ExcelNormalizer();
//		ArrayList<String> columnTitles = excelNormalizer.getColumnTitlesFromXLS(formattedFilePathText);
//		excelNormalizer.importEventsFromXLS(filePath, excelNormalizer.getColumnTitlesFromXLS(filePath), 1);
//		ArrayList<SushiEvent> events = excelNormalizer.importEventsFromXLS(cinemaSchedulePath, columnTitles);
//		//Timestamp of cell formatted Text || 12.09.2011  05:00:00
//		assertTrue("date formatted Text was not read correctly, timestamp was " + events.get(0).getTimestamp(), events.get(0).getTimestamp().compareTo(new Date(2011 - 1900, 9, 12, 05, 00, 00))==0);
	}
	
	//TODO
	@Test
	public void testFormattedCellsDateShort(){
//		ExcelNormalizer excelNormalizer = new ExcelNormalizer();
//		ArrayList<String> columnTitles = excelNormalizer.getColumnTitlesFromXLS(formattedFilePathDateShort);
//		excelNormalizer.importEventsFromXLS(filePath, excelNormalizer.getColumnTitlesFromXLS(filePath), 1);
//		ArrayList<SushiEvent> events = excelNormalizer.importEventsFromXLS(cinemaSchedulePath, columnTitles);
//		//Timestamp of cell formatted Date(short) || 12.09.2011  05:00:00
//		assertTrue("date formatted Date(short) was not read correctly, timestamp was " + events.get(0).getTimestamp(), events.get(0).getTimestamp().compareTo(new Date(2011 - 1900, 9, 12, 05, 00, 00))==0);
	}
	
	//TODO
	@Test
	public void testFormattedCellsDateLong(){
//		ExcelNormalizer excelNormalizer = new ExcelNormalizer();
//		ArrayList<String> columnTitles = excelNormalizer.getColumnTitlesFromXLS(formattedFilePathDateLong);
//		excelNormalizer.importEventsFromXLS(filePath, excelNormalizer.getColumnTitlesFromXLS(filePath), 1);
//		ArrayList<SushiEvent> events = excelNormalizer.importEventsFromXLS(cinemaSchedulePath, columnTitles);
//		//Timestamp of cell formatted Date(long) || 12.09.2011  05:00:00
//		assertTrue("date formatted Date(long) was not read correctly, timestamp was " + events.get(0).getTimestamp(), events.get(0).getTimestamp().compareTo(new Date(2011 - 1900, 9, 12, 05, 00, 00))==0);
	}
	
	@Test
	public void testExtractionOfImportEvent(){
		//Importieren mit SushiImportEvent von Events mit TimeStamp
		ExcelNormalizer excelNormalizer = new ExcelNormalizer();
		List<String> columnTitles = excelNormalizer.getColumnTitlesFromFile(cinemaSchedulePath);
		List<SushiImportEvent> events = excelNormalizer.importEventsForPreviewFromFile(cinemaSchedulePath, columnTitles);
		assertTrue("Not the right number of events imported. Should be 12, but were " + events.size(), events.size() == 12);
		System.out.println("Event: " + events.get(0));
		//Importieren mit SushiImportEvent von Events ohne TimeStamp
		List<String> columnTitlesWithoutTimestamp = excelNormalizer.getColumnTitlesFromFile(cinemaScheduleWithoutTimestampPath);
		List<SushiImportEvent> eventsWithoutTimestamp = excelNormalizer.importEventsForPreviewFromFile(cinemaScheduleWithoutTimestampPath, columnTitlesWithoutTimestamp);
		assertTrue("Not the right number of events imported. Should be 12, but were " + eventsWithoutTimestamp.size(), eventsWithoutTimestamp.size() == 12);
		System.out.println("Event: " + eventsWithoutTimestamp.get(0));
	}
	
	@Test
	public void testEmptySheet(){
		ExcelNormalizer excelNormalizer = new ExcelNormalizer();
		List<String> columnTitles = excelNormalizer.getColumnTitlesFromFile(emptyFilePath);
		List<SushiImportEvent> events = excelNormalizer.importEventsForPreviewFromFile(emptyFilePath, columnTitles);
		assertTrue(events.isEmpty());
	}

}
