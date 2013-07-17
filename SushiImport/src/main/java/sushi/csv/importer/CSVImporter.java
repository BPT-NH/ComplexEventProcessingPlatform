package sushi.csv.importer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sushi.event.SushiEvent;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;
import sushi.excel.importer.FileNormalizer;
import sushi.excel.importer.SushiImportEvent;

/**
 * Adapter for CSV Files 
 */
public class CSVImporter extends FileNormalizer{
	
	private static final long serialVersionUID = 1L;
	private char maskChar = '"';
	private char separator = ';';

	@Override
	public List<String> getColumnTitlesFromFile(String filePath) {
		String line;
		String[] split;
		try {
			FileReader file = new FileReader(filePath);
			BufferedReader data = new BufferedReader(file);

			line = data.readLine();
			//if seperator is defined replace default seperator (;) and move one row forward
			if (line.startsWith("sep=")){
				split = line.split("=");
				separator = split[1].charAt(0);
				line = data.readLine();
			}
			return getElementsFromLine(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SushiImportEvent> importEventsForPreviewFromFile(String filePath, List<String> selectedColumnTitles) {
		String seperator = ";";
		String line;
		String[] split;
		List<String> lineElements;
		List<SushiImportEvent> eventList = new ArrayList<SushiImportEvent>();
		List<Integer> selectedColumnIndexes = new ArrayList<Integer>();
		List<String> columnTitles = getColumnTitlesFromFile(filePath);
		String timestampName = getTimestampColumn(columnTitles);
		
		try {
			FileReader file = new FileReader(filePath);
			BufferedReader data = new BufferedReader(file);

			line = data.readLine();
			// if separator is defined replace default separator (;) and move one row forward
			if (line.startsWith("sep=")){
				split = line.split("=");
				separator = split[1].charAt(0);
				line = data.readLine();
			}
			lineElements = getElementsFromLine(line);
			for(int i = 0; i < lineElements.size(); i++){
				if(selectedColumnTitles.contains(lineElements.get(i))) selectedColumnIndexes.add(i);
			}
			while ((line = data.readLine()) != null) {
				lineElements = getElementsFromLine(line);
				eventList.add(generateImportEventFromRow(lineElements, columnTitles, selectedColumnIndexes, timestampName));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eventList;
	}
	
	@Override
	public List<SushiEvent> importEventsFromFile(String filePath, List<SushiAttribute> selectedAttributes, String timestamp) {
		String seperator = ";";
		String line;
		String[] split;
		List<String> lineElements;
		List<SushiEvent> eventList = new ArrayList<SushiEvent>();
		List<Integer> selectedColumnIndexes = new ArrayList<Integer>();
		List<String> columnTitles = getColumnTitlesFromFile(filePath);
		List<String> selectedColumnTitles = new ArrayList<String>();
		int timeStampColumnIndex = columnTitles.indexOf(timestamp);
		
		for (SushiAttribute attribute : selectedAttributes) {
			selectedColumnTitles.add(attribute.getName());
		}
		
		try {
			FileReader file = new FileReader(filePath);
			BufferedReader data = new BufferedReader(file);

			line = data.readLine();
			//if seperator is defined replace default seperator (;) and move one row forward
			if (line.contains("sep")){
				split = line.split("=");
				seperator = split[1];
				line = data.readLine();
			}
			split = line.split(seperator);
			for(int i = 0; i < split.length; i++){
				if(selectedColumnTitles.contains(split[i])) selectedColumnIndexes.add(i);
			}
			while ((line = data.readLine()) != null) {
				split = line.split(seperator);
				lineElements = getElementsFromLine(line);
				eventList.add(generateEventFromRow(lineElements, columnTitles, selectedAttributes, selectedColumnIndexes, timeStampColumnIndex));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return eventList;
	}
	
	private SushiImportEvent generateImportEventFromRow(List<String> rowSplit, List<String> columnTitles, List<Integer> selectedColumnIndexes, String timestampName) {
		Date timestamp = null;
		SimpleDateFormat sdfToDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		int timestampColumnIndex = columnTitles.indexOf(timestampName);
		if(timestampColumnIndex > -1){
			try {
				timestamp = sdfToDate.parse(rowSplit.get(timestampColumnIndex));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		SushiMapTree<String, Serializable> values = generateValueTree(rowSplit, columnTitles, selectedColumnIndexes, timestampColumnIndex);
		SushiImportEvent event = new SushiImportEvent(timestamp, values, timestampName, new Date());
		return event;
		
	}

	private SushiEvent generateEventFromRow(List<String> rowSplit, List<String> columnTitles, List<SushiAttribute> selectedAttributes, List<Integer> selectedColumnIndexes, int timeStampColumnIndex) {
		//Default value
		Date timestamp = new Date();
		SimpleDateFormat sdfToDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		if(timeStampColumnIndex > -1){
			try {
				timestamp = sdfToDate.parse(rowSplit.get(timeStampColumnIndex));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	
		SushiMapTree<String, Serializable> values = generateValueTree(rowSplit, columnTitles, selectedAttributes, selectedColumnIndexes, timeStampColumnIndex);
		SushiEvent event = new SushiEvent(timestamp, values);
		return event;
	}
	
	private SushiMapTree<String, Serializable> generateValueTree(List<String> rowSplit, List<String> columnTitles, List<Integer> selectedColumnIndexes, int timeStampColumnIndex) {
		SushiMapTree<String, Serializable> values = new SushiMapTree<String, Serializable>();
		for(int i : selectedColumnIndexes){
			if(i != timeStampColumnIndex){
				values.addRootElement(columnTitles.get(i), rowSplit.get(i));
			}
		}
		return values;
	}

	private SushiMapTree<String, Serializable> generateValueTree(List<String> rowSplit, List<String> columnTitles, List<SushiAttribute> selectedAttributes, List<Integer> selectedColumnIndexes, int timeStampColumnIndex) {
		SushiMapTree<String, Serializable> values = new SushiMapTree<String, Serializable>();
		SushiAttributeTypeEnum attributeType = null;
		for (int i : selectedColumnIndexes) {
			if (i != timeStampColumnIndex) {
				String attributeName = columnTitles.get(i);
				for (SushiAttribute attribute : selectedAttributes) {
					if (attribute.getName().equals(attributeName)) {
						attributeType = attribute.getType();
					}
				}
				Serializable attributeValue = rowSplit.get(i);
				if (attributeType != null) {
					if (attributeType.equals(SushiAttributeTypeEnum.DATE)) {
						SimpleDateFormat sdfToDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
						try {
							attributeValue = sdfToDate.parse(rowSplit.get(i));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else if (attributeType.equals(SushiAttributeTypeEnum.INTEGER)) {
						attributeValue = Integer.parseInt(rowSplit.get(i));
					} else {
						attributeValue = rowSplit.get(i);
					}
				}
				values.addRootElement(attributeName, attributeValue);
			}
		}
		return values;
	}
	
	private List<String> getElementsFromLine(String line){
		List<String> elements = new ArrayList<String>();
		boolean entryMasked = false;
		char lastCharacter;
		char currentCharacter = 0;
		StringBuilder currentElement = new StringBuilder();
		for(int i = 0; i < line.length(); i++){
			lastCharacter = currentCharacter;
			currentCharacter = line.charAt(i);
//			Wenn ein Element mit einem Maskierungszeichen beginnt, ist es maskiert
			if(currentCharacter == maskChar && currentElement.length() == 0){
				entryMasked = true;
			} else if(currentCharacter==separator){
				if(!entryMasked || lastCharacter == maskChar){
					String currentElementString = currentElement.toString();
					if(entryMasked){
						currentElementString = currentElement.substring(1, (currentElement.length() -1));
					}
					elements.add(currentElementString);
					currentElement = new StringBuilder();
					currentCharacter = 0;
					continue;
				}
			} 
			if(currentCharacter==maskChar && lastCharacter==maskChar){
				currentCharacter = 0;
				continue;
			}
			currentElement.append(currentCharacter);
		}
		//Letztes Element auch noch ausgeben
		String currentElementString = currentElement.toString();
		if(entryMasked){
			currentElementString = currentElement.substring(1, (currentElement.length() -1));
		}
		elements.add(currentElementString);
		//TODO: Maskierungszeichen und Whitespaces entfernen
		return elements;
	}

		

}
