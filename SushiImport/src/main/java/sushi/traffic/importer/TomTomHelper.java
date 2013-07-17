package sushi.traffic.importer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Helper for Testing
 */
public class TomTomHelper {
	
	public static void main(String[] args) throws IOException {
		System.out.println(getStreetnumbersOfExampleUsecase());
	}
	
	public static ArrayList<String> getStreetnumbersOfExampleUsecase() throws IOException{
		ArrayList<String> streetnumbers = new ArrayList<String>();
		BufferedReader fileInput = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/src/main/resources/StreetNumbersOfExampleUseCase.txt"));
		ArrayList<String> streets = new ArrayList<String>();
		String readline;
		while ((readline = fileInput.readLine()) != null){
			streets.addAll(Arrays.asList(readline.split(";")));
		}
		return streets;
	}
}
