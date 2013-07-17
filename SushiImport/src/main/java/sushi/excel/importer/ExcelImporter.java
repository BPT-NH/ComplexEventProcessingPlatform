package sushi.excel.importer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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
 * This class imports events from an excel file.
 */
public class ExcelImporter extends FileNormalizer implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates an {@link HSSFWorkbook} from an excel file.
	 */
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

	/**
	 * Returns the column titles from the given workbook sheet.
	 */
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
	
	/**
	 * Generates a event for preview from the given row. The difference to the normal creation is the treatment of the date. Import events have 
	 * @param actualRow
	 * @param selectedColumnTitles
	 * @param columnTitles
	 * @param timestampName
	 */
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
	
	/**
	 * Generates a event from the given row.
	 * @param actualRow
	 * @param selectedColumnTitles
	 * @param selectedAttributes
	 * @param columnTitles
	 * @param timeStampColumnIndex
	 */
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
	
	/**
	 * Returns true, if a {@link HSSFRow} has no values.
	 * @param actualRow
	 */
	private boolean isRowEmpty(HSSFRow actualRow) {
		boolean emptyRow = true;
		Iterator<Cell> cellIterator = actualRow.cellIterator(); 
		while(cellIterator.hasNext()){
			Cell actualCell = cellIterator.next();
			emptyRow = (actualCell.getCellType() != Cell.CELL_TYPE_BLANK) ? false : true;
		}
		return emptyRow;
	}
	
	/**
	 * Returns a new {@link SushiMapTree} from the given row with the values for the selected columns.
	 * @param actualRow
	 * @param selectedColumnTitles
	 * @param columnTitles
	 * @param timeStampColumnIndex
	 * @return
	 */
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
	
	/**
	 * Returns a new {@link SushiMapTree} from the given row with the values for the selected columns.
	 * It is possible to specify the selected {@link SushiAttribute}s.
	 * @param actualRow
	 * @param selectedColumnTitles
	 * @param columnTitles
	 * @param timeStampColumnIndex
	 * @return
	 */
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
