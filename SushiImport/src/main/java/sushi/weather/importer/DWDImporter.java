package sushi.weather.importer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import sushi.FileUtils;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.xml.importer.XMLParser;
import sushi.xml.importer.XMLParsingException;
import sushi.xml.importer.XSDParser;

public class DWDImporter {

	static String server = "ftp-outgoing2.dwd.de";
	static int port = 21;
	static String user = "DWD USERNAME";
	static String pass = "DWD USER PW";
	private FTPClient ftpClient;
	private File downloadFolder;
	private File[] localFiles;
	// files in this list are not up to date and can be removed
	private HashSet<String> filesToDeleteAfterSync;
	// files in this list are new and need to be processed as events
	private HashSet<String> newFiles;
	
	public static void main(String[] args) throws IOException, XMLParsingException {
		// remote stuff
		DWDImporter importer = new DWDImporter();
//		importer.downloadFilesFromFTPPathForLocation(remoteFolderPD, downloadFolder.getCanonicalPath(), "GDSX");
		System.out.println("new weatherevents: " + importer. getNewWeatherEvents());
		System.out.println("downloaded!");
		importer.disconnect();
	}
	
	/**
	 * generates events from dwd ftp server
	 * generate just events for the newest, unseen events 
	 */
	public ArrayList<SushiEvent> getNewWeatherEvents() throws XMLParsingException, IOException{
		this.init(); //to setup connection and reset lists and stuff
		syncAllWeatherWarningsExampleUseCase();
		ArrayList<SushiEvent> events = new ArrayList<SushiEvent>();
		for (File file: downloadFolder.listFiles()){
			// only the new files are processed into events
			if (newFiles.contains(file.getName())) {
				events.add(XMLParser.generateEventFromXML(file.getCanonicalPath(), getXSDFilePath()));
			}
		}
		deleteOutDatedFiles();
		return events;
	}
	
	/**
	 * disconnects a running connection
	 * setup fields for sync usage 
	 */
	private void init() throws SocketException, IOException{
		disconnect();
		ftpClient = new FTPClient();
		ftpClient.connect(server, port);
		ftpClient.login(user, pass);
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		// create folder
		downloadFolder = new File(System.getProperty("user.dir")+"/bin/weatherXML/");
		downloadFolder.mkdirs();
		localFiles = downloadFolder.listFiles();
		filesToDeleteAfterSync = new HashSet<String>();
		for (File file : localFiles){
			filesToDeleteAfterSync.add(file.getName());
		}
		newFiles = new HashSet<String>();
	}
	
	/**
	 * returns the Eventtype generated from the XSD (located in getXSDFilePath()) 
	 */
	public SushiEventType getWeatherEventtype() throws XMLParsingException{
		return XSDParser.generateEventTypeFromXSD(getXSDFilePath(), FileUtils.getFileNameWithoutExtension(getXSDFilePath())); 
	}

	/**
	 * call this after you are finished to close connection 
	 */
	public void disconnect() throws IOException{
		if (ftpClient != null && ftpClient.isConnected()) {
			ftpClient.logout();
			ftpClient.disconnect();
		}
	}

	/**
	 * sync local downloadfolder with Warnings from Example Usecase, 
	 * delete local data which isn't any longer on ftp, 
	 * do not reload existing files which are local and on ftp, 
	 * import new files, 
	 * returns a list of new files
	 */
	private void syncAllWeatherWarningsExampleUseCase() throws IOException{
		// Hansestadt Hamburg
		syncFilesFromFTPPathForLocation("gds/specials/warnings/xml/HA/", "HHXX");
		// Kreis Herzogtum Lauenburg
		syncFilesFromFTPPathForLocation("gds/specials/warnings/xml/HA/", "RZXX");
		// LK Ludwigslust-Parchim - West 
		syncFilesFromFTPPathForLocation("gds/specials/warnings/xml/PD/", "LPWX");
		// Prignitz
		syncFilesFromFTPPathForLocation("gds/specials/warnings/xml/PD/", "PRXX");
		// Kreis Ostprignitz-Ruppin
		syncFilesFromFTPPathForLocation("gds/specials/warnings/xml/PD/", "OPRX");
		// Oberhavel
		syncFilesFromFTPPathForLocation("gds/specials/warnings/xml/PD/", "OHVX");
		// Berlin
		syncFilesFromFTPPathForLocation("gds/specials/warnings/xml/PD/", "BXXX");
	}

	/**
	 * syncs the local dwd folder with the remote ftp server, 
	 * deletes local data which no longer exist on ftp, 
	 * does nothing with files which are local and on ftp, 
	 * downloads new files, 
	 * @return list of new files 
	 */
	private void syncFilesFromFTPPathForLocation(String ftpPathFolder, String dwdLocationAbreviation) throws IOException{
		// list files on the ftp server on in the given folder location
		FTPFile[] remoteFiles = ftpClient.listFiles(ftpPathFolder);
		for (FTPFile remoteFile : remoteFiles){
			// check if the file is for the given location and not already downloaded
			
			if (nameMatch(remoteFile.getName(), dwdLocationAbreviation) && !filesToDeleteAfterSync.contains(remoteFile.getName())){
				// file representation of the file on the local system
				File downloadfile = new File(downloadFolder.getCanonicalPath() + "/" + remoteFile.getName());
				OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadfile));
				// download the actual file
				ftpClient.retrieveFile(ftpPathFolder + remoteFile.getName(), outputStream);
				outputStream.close();
				System.out.println("downloaded warnings from:  " + remoteFile.getName());
				// save file to new file list
				newFiles.add(remoteFile.getName());
			} else if (nameMatch(remoteFile.getName(), dwdLocationAbreviation)) {
				// remote file is already on local folder and do not have to get deleted
				filesToDeleteAfterSync.remove(remoteFile.getName());
			}
		}	 
	}
	
	/**
	 * 
	 * @return XSD File for Validation
	 */
	public String getXSDFilePath(){
		String path = getClass().getResource("").toString();
		path = path.substring(0, path.indexOf("target")) + "xsd-definitions/legend_warnings_CAP.xsd";
		return path;
	}

	/**
	 * 
	 * @param fileName Filename on the FTP server like Z_CAP_C_EDZW_20130430115427_OMEDES_WWHA81_HHXX.xml
	 * @param locationAbreviation DWD Abreviation for the location. like 'HHXX' for Hansestadt Hamburg
	 */
	private boolean nameMatch(String fileName, String locationAbreviation){
		return fileName.matches("^.+" + locationAbreviation +"\\.xml$");
	}
	
	private void  deleteOutDatedFiles(){
		// clean folder
		for (File localFile: downloadFolder.listFiles()){
			if (filesToDeleteAfterSync.contains(localFile.getName())){
				System.out.println("deleted weather event file " + localFile.getName());
				localFile.delete();
			}
		}
	}
}