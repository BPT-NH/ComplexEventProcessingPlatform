package sushi.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;


import sushi.notification.SushiNotificationForEvent;
import sushi.notification.SushiNotificationRule;
import sushi.notification.SushiNotificationRuleForEvent;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.util.HashUtil;

/**
 * This class represents users of the system. The users can be saved in and restored from the database.
 * @author micha
 */
@Entity
@Table(name = "SushiUser")
public class SushiUser extends Persistable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "PASSWORD")
	private String passwordHash;
	
	@Column(name = "MAIL")
	private String mail;
		
	/**
	 * Default Constructor for JPA
	 */
	public SushiUser(){
		
	}
	
	/**
	 * Constructor to create a new user with a name, a password and a mail address.
	 * @param name
	 * @param password
	 * @param mail
	 */
	public SushiUser(String name, String password, String mail){
		this.name = name;
		this.passwordHash = HashUtil.generateHash(password);
		this.mail = mail;
	}
	
	public String toString() {
		return name + "(" + mail + ")";
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the hashed password for the current user.
	 * @return
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Returns the mail adress for the current user.
	 * @return
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * Sets a new mail adress for the current user.
	 * @param mail
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	/**
	 * Finds all users in the database.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<SushiUser> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiUser t");
		return q.getResultList();
	}
	
	/**
	 * Returns all users with the given attribute and the corresponding value from the database.
	 * @param columnName
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<SushiUser> findByAttribute(String columnName, String value){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiUser WHERE " + columnName + " = '" + value + "'", SushiUser.class);
		return query.getResultList();
	}
	
	/**
	 * Returns the user with the given ID from the database, if any.
	 * @param ID
	 * @return
	 */
	public static SushiUser findByID(int ID){
		List<SushiUser> list = SushiUser.findByAttribute("ID", new Integer(ID).toString());
		if(list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns all users from the database with the given name.
	 * @param name
	 * @return
	 */
	public static List<SushiUser> findByName(String name){
		return SushiUser.findByAttribute("NAME", name);
	}
	
	/**
	 * Returns all users from the database with the given mail.
	 * @param mail
	 * @return
	 */
	public static List<SushiUser> findByMail(String mail){
		return SushiUser.findByAttribute("MAIL", mail);
	}
	
	/**
	 * Deletes this user from the database.
	 * @return 
	 */
	@Override
	public Persistable remove() {
		//remove notificationrules
		for (SushiNotificationRule notificationRule : SushiNotificationRule.findByUser(this)) notificationRule.remove();
		return (SushiUser) super.remove();
	}
	
	/**
	 * Deletes all users from the database.
	 */
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiUser");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}