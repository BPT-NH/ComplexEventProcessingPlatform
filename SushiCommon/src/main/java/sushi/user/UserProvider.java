package sushi.user;

import sushi.util.HashUtil;


/**
 * @author micha
 *
 */
public class UserProvider {
	
	public static boolean isNameAlreadyInUse(String name){
		if(SushiUser.findByName(name).isEmpty()){
			return false;
		} else {
			return true;
		}
	}
	
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
