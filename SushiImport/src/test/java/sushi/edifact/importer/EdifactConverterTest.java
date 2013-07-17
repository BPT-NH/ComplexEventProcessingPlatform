package sushi.edifact.importer;


import java.io.File;

import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import sushi.FileUtils;
import sushi.edifact.importer.EdifactImporter;

public class EdifactConverterTest {
	
	@Test
	public void convertFileBerman() throws Exception {
		String path = "src/test/resources/EdifactFiles/1_BERMAN.txt";
		parseFile(path);	}
	
	@Test
	public void convertFileIFtmcs() throws Exception {
		String path = "src/test/resources/EdifactFiles/2_IFTMCS.txt";
		parseFile(path);	}
	
	@Test
	public void convertFileCoprar() throws Exception {
		String path = "src/test/resources/EdifactFiles/3_COPRAR.txt";
		parseFile(path);	}

	@Test
	public void convertFileCoarri() throws Exception {
		String path = "src/test/resources/EdifactFiles/5_COARRI.txt";
		parseFile(path);
	}
	
	@Test
	public void convertFileCopino() throws Exception {
		String path = "src/test/resources/EdifactFiles/6_COPINO.txt";
		parseFile(path);
	}
	
	private void parseFile(String path) throws Exception {
		String outPutpath = path.substring(0,path.indexOf(".")) + ".xml";
		StreamResult result = EdifactImporter.getInstance().convertEdiFileToXML(path);
		FileUtils.writeResultToFile(result, outPutpath);
		System.out.println("Edifact-File <" + path + "> converted to <" + path + ">.");
		File output = new File(outPutpath);
		assertTrue("Output was not created", output.exists());
        output.delete();
        System.out.println("deleted " + output.getAbsolutePath());
	}
	
	@Test
	public void convertAll() throws Exception {
		//convert all testfiles
		File folder = new File("src/test/resources/EdifactFiles");
		for (final File fileEntry : folder.listFiles()) {
		     if (fileEntry.isFile() && fileEntry.getPath().endsWith(".txt"))  {
		       	String path = folder.getPath() + "/" + fileEntry.getName();
				parseFile(path);
		     }
		}
	}

}
