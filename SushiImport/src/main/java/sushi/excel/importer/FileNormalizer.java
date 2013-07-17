package sushi.excel.importer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sushi.event.SushiEvent;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;

/**
 * This class centralizes common methods for the import of files.
 */
public abstract class FileNormalizer implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Returns the titles as list for files with tabular content.
	 * @param filePath
	 * @return
	 */
	public abstract List<String> getColumnTitlesFromFile(String filePath);
	
	/**
	 * Imports events from a file for a preview to decide, which parts of the data should be user for the event creation.
	 * @param filePath
	 * @param selectedColumnTitles
	 * @return
	 */
	public abstract List<SushiImportEvent> importEventsForPreviewFromFile(String filePath, List<String> selectedColumnTitles);
	
	/**
	 * Imports data from a file and creates events from this data.
	 * @param filePath
	 * @param selectedAttributes
	 * @return
	 * @throws IllegalArgumentException
	 */
	public List<SushiEvent> importEventsFromFile(String filePath, List<SushiAttribute> selectedAttributes) throws IllegalArgumentException {
		String timeStampColumn = getTimestampColumnFromAttribute(selectedAttributes);
		return importEventsFromFile(filePath, selectedAttributes, timeStampColumn);
	}
	
	/**
	 * Imports data from a file and creates events from this data.
	 * @param filePath
	 * @param selectedAttributes
	 * @param timestamp
	 * @return
	 */
	public abstract List<SushiEvent> importEventsFromFile(String filePath, List<SushiAttribute> selectedAttributes, String timestamp);
	
	/**
	 * Tries to identify a timestamp from the given attributes.
	 * @param attributes
	 * @return
	 */
	protected String getTimestampColumnFromAttribute(List<SushiAttribute> attributes) {
		ArrayList<String> columnTitles = new ArrayList<String>();
		for (SushiAttribute attribute : attributes) {
			if (attribute.getType() == SushiAttributeTypeEnum.DATE) {
				columnTitles.add(attribute.getName());
			}
		}
		return getTimestampColumn(columnTitles);
	}
	
	/**
	 * Tries to identify a timestamp column from the given attributes.
	 * @param columnTitles
	 * @return
	 */
	protected String getTimestampColumn(List<String> columnTitles) {
		for (int i = 0; i < columnTitles.size(); i++) {
			String actualColumnTitle = columnTitles.get(i);
			for (TimeStampNames columnName : TimeStampNames.values()) {
				if (columnName.toString().equals(actualColumnTitle.toUpperCase())) {
					return actualColumnTitle;
				}
			}
		}
		return null;
	}
}
