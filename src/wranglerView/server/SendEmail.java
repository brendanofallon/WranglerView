package wranglerView.server;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public final class SendEmail {

	public static boolean sendEMail(String to, 
									String from, 
									String subject, 
									String text, 
									final String username, 
									final String password) {
		Properties props = System.getProperties();
		String mailhost = "www.fastmail.fm";
		if (mailhost != null) 
			props.put("mail.smtp.host", mailhost);
		props.put("mail.user", "brendanofallon@fastmail.fm");
		props.put("mail.smtp.user", "brendanofallon@fastmail.fm");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.port", "465");

		try {
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		Message msg = new MimeMessage(session);
		if (from != null)
			msg.setFrom(new InternetAddress(from));

		msg.setRecipients(Message.RecipientType.TO, 
				InternetAddress.parse(to, false));

		msg.setSubject(subject);


		msg.setText(text);
		msg.setHeader("X-Mailer", "msgsend");
		msg.setSentDate(new Date());

		// send the thing off
		Transport.send(msg);
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
  
}