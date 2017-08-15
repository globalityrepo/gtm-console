package br.com.globality.gtm.console.util;

import java.util.Properties;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;

@Stateless
public class EmailManager {
	 
	@Inject
	private Logger log;
	
	public boolean sendMail(String emailTo, String subject, String message) {

		try {			
			
			final String EMAIL_FROM  = "gtm.noreply@globality.com.br";
			final String PWD_FROM    = "P@ssw0rd";
			
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.globality.com.br");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "587");
			props.put("mail.debug", "false");
			props.put("mail.smtp.ssl.enable", "false");
			
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(EMAIL_FROM, PWD_FROM);
						}
					});
						
			Message msg = new MimeMessage(session);
			
			InternetAddress from = new InternetAddress("GTM - Global Transaction Manager <" + EMAIL_FROM + ">");
			msg.setFrom(from);

			InternetAddress[] toAddresses = new InternetAddress[1];
			toAddresses[0] = new InternetAddress(emailTo);
			msg.setRecipients(Message.RecipientType.TO, toAddresses);

			msg.setSubject(MimeUtility.encodeText(subject, "UTF-8", "Q"));
			msg.setContent(message, "text/html; charset=UTF-8");
			Transport.send(msg);

			return true;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		
	}

}
