package sushi.email;

import java.io.*;
import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.imap.IMAPFolder;

/**
 * Utils for sending emails via google mail
 */
public class EmailUtils {

	public static String user = "INSERT YOUR googlemail";
	public static String pass = "INSERT YOUR EMAIL PW";

	public static void main(String[] args) throws MessagingException, IOException {
		Session session = EmailUtils.getGMailSession(user, pass);

		printTestInbox(session);
		sendTestMail(session);
	}

	private static void printTestInbox(Session session)
			throws MessagingException, IOException {
		Folder inbox = EmailUtils.openPop3InboxReadWrite( session );
		EmailUtils.printAllTextPlainMessages( inbox );
		EmailUtils.closeFolder( inbox );
	}
	
	public static boolean sendBP2013Mail(String recipient,	String subject, String message) {
		try {
			Session session = getGMailSession(user, pass);
			postMail(session, recipient, subject, message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void sendTestMail(Session session) throws MessagingException{
		EmailUtils.postMail(session, "bp2013w1@gmail.com", "Kurze Info", "test msg" );
	}

	public static Session getGMailSession(final String user, final String pass ) {
		final Properties props = new Properties();

		// Zum Empfangen
		props.setProperty("mail.store.protocol", "imaps");
		
		// Zum Senden
	    props.setProperty("mail.smtp.host", "smtp.gmail.com" );
	    props.setProperty("mail.smtp.auth", "true" );
	    props.setProperty("mail.smtp.port", "465" );
	    props.setProperty("mail.smtp.socketFactory.port", "465" );
	    props.setProperty("mail.smtp.socketFactory.class",
	                       "javax.net.ssl.SSLSocketFactory" );
	    props.setProperty("mail.smtp.socketFactory.fallback", "false" );

		return Session.getInstance(props, new javax.mail.Authenticator() {
			@Override protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pass);
			}
		});
	}
	
	/**
	 * opens inbox folder in read write modus
	 * returns inbox folder
	 */
	public static Folder openPop3InboxReadWrite( Session session ) throws MessagingException {
		Store store = session.getStore( "imaps" );
		store.connect("imap.googlemail.com",user, pass);

		IMAPFolder folder = (IMAPFolder) store.getFolder( "INBOX" );
		if(!folder.isOpen())
	          folder.open(Folder.READ_WRITE);
		return folder;
	}
	
	/**
	 * return trash folder in read write modus
	 */
	public static Folder openPop3TrashReadWrite( Session session ) throws MessagingException {
		Store store = session.getStore( "imaps" );
		store.connect("imap.googlemail.com",user, pass);

		IMAPFolder folder = (IMAPFolder) store.getFolder( "[Gmail]/Papierkorb" );
		if(!folder.isOpen())
	          folder.open(Folder.READ_WRITE);
		return folder;
	}

	/**
	 * closes folder 
	 */
	public static void closeFolder( Folder folder ) throws MessagingException {
		folder.close( false );
		folder.getStore().close();
	}

	/**
	 * sends email 
	 */
	public static void postMail( Session session, String recipient,	String subject, String message ) throws MessagingException	{
		Message msg = new MimeMessage( session );

		InternetAddress addressTo = new InternetAddress( recipient );
		msg.setRecipient( Message.RecipientType.TO, addressTo );

		msg.setSubject( subject );
		msg.setContent( message, "text/plain" );
		Transport.send( msg );
	}

	public static void printAllTextPlainMessages( Folder folder ) throws MessagingException, IOException {
		for ( Message m : folder.getMessages() ){
			System.out.println( "\nNachricht:" );
			System.out.println( "Von: " + Arrays.toString(m.getFrom()) );
			System.out.println( "Betreff: " + m.getSubject() );
			System.out.println( "Gesendet am: " + m.getSentDate() );
			System.out.println( "Content-Type: " +
					new ContentType( m.getContentType() ) );

			if ( m.isMimeType( "text/plain" ) )
				System.out.println( m.getContent() );
		}
	}

}
