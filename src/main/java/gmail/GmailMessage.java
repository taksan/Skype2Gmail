package gmail;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import skype.SkypeUser;

public class GmailMessage {
	private MimeMessage mimeMessage;
	public GmailMessage(Session session) {
		mimeMessage = new MimeMessage(session);
	}
	
	public MimeMessage toMimeMessage() {
		return mimeMessage;
	}

	public void setFrom(String chatAuthor) {
		try {
			mimeMessage.setFrom(new InternetAddress(chatAuthor));
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void setHeader(String headerField, String id) {
		try {
			mimeMessage.setHeader(headerField, id);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void setSubject(String topic) {
		try {
			mimeMessage.setSubject(topic);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void addRecipient(SkypeUser skypeUser) {
		try {
			mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(skypeUser.getMailAddress()));
		} catch (AddressException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void setText(String messageBody) {
		try {
			mimeMessage.setText(messageBody);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
