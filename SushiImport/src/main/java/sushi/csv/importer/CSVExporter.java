package sushi.csv.importer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.event.collection.SushiMapTree;

public class CSVExporter {

	public File generateExportFile(SushiEventType eventType, List<SushiEvent> events){
		//create file
		if(eventType.isHierarchical()){
			return null;
		}
		File file = new File(System.getProperty("user.dir")+"/src/test/resources/" + eventType.getTypeName() + "export.csv");
		try {
			FileWriter writer = new FileWriter(file ,false);
			List<String> attributeNames = eventType.getAttributeKeysFromMap();
			writer.write(eventType.getTimestampName() + ";");
			for(String attributeName : attributeNames){
				writer.write(attributeName + ";");
			}
			for(SushiEvent event : events){
				writer.write(System.getProperty("line.separator"));
				writer.write(event.getTimestamp() + ";");
				SushiMapTree<String, Serializable> tree = event.getValues();
				for(String attributeName : attributeNames){
					writer.write(tree.getValueOfAttribute(attributeName)+";");
				}
			}
			//write stream to file
			writer.flush();
	       // close the stream
	       writer.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// define separator
		//sep=;
		// read column titles from event type
		List<String> attributeNames = eventType.getAttributeKeysFromMap();
		//read values of the chosen events
		for(SushiEvent e : events){
			SushiMapTree<String, Serializable> values = e.getValues();
//			values.ge
		}
		return file;
	}
	
	
}
