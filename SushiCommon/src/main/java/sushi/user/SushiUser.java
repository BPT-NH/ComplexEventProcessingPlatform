package sushi.user;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.bpmn.element.BPMNProcess;
import sushi.notification.SushiNotification;
import sushi.notification.SushiNotificationRule;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;
import sushi.util.HashUtil;

/**
 * @author micha
 *
 */
@Entity
@Table(name = "SushiUser")
public class SushiUser extends Persistable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "PASSWORD")
	private String passwordHash;
	
	@Column(name = "MAIL")
	private String mail;
		
	/*
	 * Default Constructor for JPA
	 */
	public SushiUser(){
		
	}
	
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

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	//JPA-Methods
	
	/**
	 * Finds all users in the database.
	 * @return
	 */
	public static List<SushiUser> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiUser t");
		return q.getResultList();
	}
	
	private static List<SushiUser> findByAttribute(String columnName, String value){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiUser WHERE " + columnName + " = '" + value + "'", SushiUser.class);
		return query.getResultList();
	}
	
	public static SushiUser findByID(int ID){
		List<SushiUser> list = SushiUser.findByAttribute("ID", new Integer(ID).toString());
		if(list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public static List<SushiUser> findByName(String name){
		return SushiUser.findByAttribute("NAME", name);
	}
	
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
