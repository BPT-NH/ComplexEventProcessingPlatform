package sushi.user;

import sushi.util.HashUtil;


/**
 * This class manages the creation of new users.
 * @author micha
 */
public class UserProvider {
	
	/**
	 * Checks, if the given name is already used for another user.
	 * @param name
	 * @return
	 */
	public static boolean isNameAlreadyInUse(String name){
		if(SushiUser.findByName(name).isEmpty()){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Creates a new user with the given name, password and mail, if the user name is not already in use.
	 * @param name
	 * @param password
	 * @param mail
	 */
	public static void createUser(String name, String password, String mail){
		if(!isNameAlreadyInUse(name)){
			SushiUser user = new SushiUser(name, password, mail);
			user.save();
		} else {
			throw new RuntimeException("Name already in use.");
		}
	}
	
	public static SushiUser findUser(String name, String password){
		if(!SushiUser.findByName(name).isEmpty()){
			SushiUser user = SushiUser.findByName(name).get(0);
			if(user.getPasswordHash().equals(HashUtil.generateHash(password))){
				return user;
			}
		}
		return null;
	}
}