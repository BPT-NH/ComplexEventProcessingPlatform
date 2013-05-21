package sushi.excel.importer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sushi.event.SushiEvent;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;



public abstract class FileNormalizer implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public abstract List<String> getColumnTitlesFromFile(String filePath);
	
	public abstract List<SushiImportEvent> importEventsForPreviewFromFile(String filePath, List<String> selectedColumnTitles);
	
	public List<SushiEvent> importEventsFromFile(String filePath, List<SushiAttribute> selectedAttributes) throws IllegalArgumentException {
		String timeStampColumn = getTimestampColumnFromAttribute(selectedAttributes);
		return importEventsFromFile(filePath, selectedAttributes, timeStampColumn);
	}
	
	public abstract List<SushiEvent> importEventsFromFile(String filePath, List<SushiAttribute> selectedAttributes, String timestamp);
	
	protected String getTimestampColumnFromAttribute(List<SushiAttribute> attributes) {
		ArrayList<String> columnTitles = new ArrayList<String>();
		for (SushiAttribute attribute : attributes) {
			if (attribute.getType() == SushiAttributeTypeEnum.DATE) {
				columnTitles.add(attribute.getName());
			}
		}
		return getTimestampColumn(columnTitles);
	}
	
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
