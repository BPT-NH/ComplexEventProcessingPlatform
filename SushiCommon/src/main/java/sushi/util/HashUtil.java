package sushi.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class generates hash values in MD5-format for a given string.
 * @author micha
 */
public class HashUtil {
	
	/**
	 * Generates a hashed string in MD5-format for the given string.
	 * @param input
	 * @return
	 */
	public static String generateHash(String input){
        MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(input.getBytes(),0, input.length());
	        return new BigInteger(1, messageDigest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("No such hash algorithm!");
		}
        
	}

}
