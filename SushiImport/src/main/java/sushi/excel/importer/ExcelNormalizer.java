package sushi.excel.importer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import sushi.event.SushiEvent;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiMapTree;

/**
 * @author micha
 *
 */
public class ExcelNormalizer extends FileNormalizer implements Serializable {

	//TODO: Refactoren, import und importForPreview enthalten viel doppelten Code
	
	private HSSFWorkbook loadWorkbook(String fileName) {
		try {
			InputStream myxls = new FileInputStream(fileName);
			return new HSSFWorkbook(myxls);
		} catch (IOException e) {
			System.err.println("Workbook could not be load.");
			return null;
		}
	}
	@Override
	public ArrayList<String> getColumnTitlesFromFile(String fileName) {
		HSSFWorkbook workbook = this.loadWorkbook(fileName);
		HSSFSheet firstSheet = workbook.getSheetAt(0);
		return generateColumnTitlesFromSheet(firstSheet);
	}

	private ArrayList<String> generateColumnTitlesFromSheet(HSSFSheet sheet) {
		HSSFRow firstRow = sheet.getRow(0);
		if(firstRow != null){
			ArrayList<String> columnTitles = new ArrayList<String>();
			for(Cell currentCell : firstRow){
				columnTitles.add(currentCell.getStringCellValue().trim().replaceAll(" +","_").replaceAll("[^a-zA-Z0-9_]+",""));
			}
			return columnTitles;
		} else {
			return new ArrayList<String>();
		}
		
	}

	@Override
	public List<SushiImportEvent> importEventsForPreviewFromFile(String filePath, List<String> selectedColumnTitles) throws IllegalArgumentException {
		
		List<SushiImportEvent> eventList = new ArrayList<SushiImportEvent>();
		List<String> columnTitles = this.getColumnTitlesFromFile(filePath);
		if (!columnTitles.isEmpty()) {
			String timestampName = getTimestampColumn(selectedColumnTitles);
			Iterator<Row> rowIterator = getRowIterator(filePath);

			// Eliminate headline row
			rowIterator.next();
			
			while (rowIterator.hasNext()) {
				HSSFRow actualRow = (HSSFRow) rowIterator.next();
				if(!isRowEmpty(actualRow)){
					eventList.add(this.generateImportEventFromRow(actualRow, selectedColumnTitles, columnTitles, timestampName));
				}
			}
		}
		return eventList;
	}
	
	@Override
	public List<SushiEvent> importEventsFromFile(String filePath, List<SushiAttribute> selectedAttributes, String timestamp) {
		List<SushiEvent> eventList = new ArrayList<SushiEvent>();
		List<String> columnTitles = getColumnTitlesFromFile(filePath);
		List<String> selectedColumnTitles = new ArrayList<String>();
		if (!columnTitles.isEmpty()) {
			
			for (SushiAttribute attribute : selectedAttributes) {
				selectedColumnTitles.add(attribute.getName());
			}
			
			int timeStampColumnIndex = columnTitles.indexOf(timestamp);
			Iterator<Row> rowIterator = getRowIterator(filePath);
	
			// Eliminate headline row
			rowIterator.next();
			
			while (rowIterator.hasNext()) {
				HSSFRow actualRow = (HSSFRow) rowIterator.next();
				if (!isRowEmpty(actualRow)) {
					eventList.add(this.generateEventFromRow(actualRow, selectedColumnTitles, selectedAttributes, columnTitles, timeStampColumnIndex));
				}
			}
		}
		return eventList;
	}
	
	private SushiImportEvent generateImportEventFromRow(HSSFRow actualRow, List<String> selectedColumnTitles, List<String> columnTitles, String timestampName) {
		Date timestamp;
		int timestampColumnIndex = columnTitles.indexOf(timestampName);
		if (timestampColumnIndex > -1) {
			timestamp = actualRow.getCell(timestampColumnIndex).getDateCellValue();
		} else {
			timestamp = null;
		}
		SushiMapTree<String, Serializable> values = generateValueTree(actualRow, selectedColumnTitles, columnTitles, timestampColumnIndex);
		SushiImportEvent event = new SushiImportEvent(timestamp, values, timestampName, new Date());
		return event;
	}
	
	private SushiEvent generateEventFromRow(HSSFRow actualRow, List<String> selectedColumnTitles, List<SushiAttribute> selectedAttributes, List<String> columnTitles, int timeStampColumnIndex) {
		Date timestamp;
		//Falls kein TimeStamp gefunden wurde, wird die aktuelle Einlesezeit verwendet
		if(timeStampColumnIndex > -1){
			timestamp = actualRow.getCell(timeStampColumnIndex).getDateCellValue();
		}
		else{
			timestamp = new Date();
		}
		SushiMapTree<String, Serializable> values = generateValueTree(actualRow, selectedColumnTitles, selectedAttributes, columnTitles, timeStampColumnIndex);
		SushiEvent event = new SushiEvent(timestamp, values);
		return event;
	}
	
	private Iterator<Row> getRowIterator(String filePath) {
		HSSFWorkbook workbook = this.loadWorkbook(filePath);
		HSSFSheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = firstSheet.rowIterator();
		return rowIterator;
	}
	
	private boolean isRowEmpty(HSSFRow actualRow) {
		boolean emptyRow = true;
		Iterator<Cell> cellIterator = actualRow.cellIterator(); 
		while(cellIterator.hasNext()){
			Cell actualCell = cellIterator.next();
			emptyRow = (actualCell.getCellType() != Cell.CELL_TYPE_BLANK) ? false : true;
		}
		return emptyRow;
	}
	
	private SushiMapTree<String, Serializable> generateValueTree(HSSFRow actualRow, List<String> selectedColumnTitles, List<String> columnTitles, int timeStampColumnIndex) {
		SushiMapTree<String, Serializable> values = new SushiMapTree<String, Serializable>();
		Iterator<Cell> cellIterator = actualRow.cellIterator();
		while (cellIterator.hasNext()) {
			Cell actualCell = cellIterator.next();
			if (selectedColumnTitles.contains(columnTitles.get(actualCell.getColumnIndex())) && actualCell.getColumnIndex() != timeStampColumnIndex) {
				if (actualCell.getCellType() == Cell.CELL_TYPE_STRING) {
					values.put(columnTitles.get(actualCell.getColumnIndex()), actualCell.getStringCellValue());
				} else if (actualCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					values.put(columnTitles.get(actualCell.getColumnIndex()), Double.toString(actualCell.getNumericCellValue()));
				}
			}
		}
		return values;
	}
	
	private SushiMapTree<String, Serializable> generateValueTree(HSSFRow actualRow, List<String> selectedColumnTitles, List<SushiAttribute> selectedAttributes, List<String> columnTitles, int timeStampColumnIndex) {
		SushiMapTree<String, Serializable> values = new SushiMapTree<String, Serializable>();
		Iterator<Cell> cellIterator = actualRow.cellIterator();
		SushiAttributeTypeEnum attributeType = null;
		while (cellIterator.hasNext()) {
			Cell actualCell = cellIterator.next();
			String attributeName = columnTitles.get(actualCell.getColumnIndex());
			for (SushiAttribute attribute : selectedAttributes) {
				if (attribute.getName().equals(attributeName)) {
					attributeType = attribute.getType();
				}
			}
			if (selectedColumnTitles.contains(columnTitles.get(actualCell.getColumnIndex())) && actualCell.getColumnIndex() != timeStampColumnIndex) {
				Serializable attributeValue = null;
				if (attributeType != null) {
					if (attributeType.equals(SushiAttributeTypeEnum.DATE)) {
						attributeValue = actualCell.getDateCellValue();
					} else if (attributeType.equals(SushiAttributeTypeEnum.INTEGER)) {
						// TODO: support double/float
//						attributeValue = Math.round(actualCell.getNumericCellValue());
						attributeValue = new Integer((int) actualCell.getNumericCellValue());
					} else if (attributeType.equals(SushiAttributeTypeEnum.STRING)) {
						attributeValue = actualCell.getStringCellValue();
					}
				}
				values.addRootElement(attributeName, attributeValue);
			}
		}
		return values;
	}

}
