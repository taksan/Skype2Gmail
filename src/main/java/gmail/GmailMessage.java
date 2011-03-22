package gmail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeUtility;

import skype.SkypeUser;

public class GmailMessage {
	private MimeMessage mimeMessage;
	public static final String X_MESSAGES_SIGNATURES = "X-SKYPE-MESSAGES-SIGNATURES";
	public static final String X_BODY_SIGNATURE = "X-SKYPE-BODY-SIGNATURE";
	public static final String X_MESSAGE_ID = "X-SKYPE-MESSAGE-ID";
	public static final String X_SKYPE_POSTERS = "X_SKYPE_POSTERS";
	
	public GmailMessage(Session session) {
		this.mimeMessage = new MimeMessage(session);
	}
	
	public GmailMessage(MimeMessage message) {
		this.mimeMessage = message;
	}

	public MimeMessage getMimeMessage() {
		return mimeMessage;
	}

	public void setFrom(String chatAuthor) {
		try {
			mimeMessage.setFrom(new InternetAddress(chatAuthor));
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

	public void setBody(String messageBody) {
		try {
			mimeMessage.setText(messageBody, "UTF-8");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getChatId() {
		return getFirstHeaderOrNull(GmailMessage.X_MESSAGE_ID);
	}
	
	public String getBodySignature() {
		return getFirstHeaderOrNull(GmailMessage.X_BODY_SIGNATURE);
	}

	public String getTopic() {
		try {
			return this.mimeMessage.getSubject();
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getBody() {
		try {
			return (String) mimeMessage.getContent();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void setChatId(String id) {
		this.setHeader(GmailMessage.X_MESSAGE_ID, id);
	}

	public void setBodySignature(String bodySignature) {
		this.setHeader(GmailMessage.X_BODY_SIGNATURE, bodySignature);
	}

	public void setMessagesSignatures(String signatures) {
		this.setHeader(GmailMessage.X_MESSAGES_SIGNATURES, signatures);
	}

	public Date getDate() {
		try {
			return mimeMessage.getSentDate();
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public String [] getMessagesSignatures() {
		String signatures = this.getFirstHeaderOrNull(X_MESSAGES_SIGNATURES);
		return signatures.split(",");
	}

	public String [] getUsers() {
		return this.getHeader(X_SKYPE_POSTERS);
	}

	public void addPoster(SkypeUser skypeUser) {
		try {
			this.mimeMessage.addHeader(X_SKYPE_POSTERS, skypeUser.getPosterHeader());
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public InternetAddress[] getFrom() {
		try {
			return (InternetAddress[]) mimeMessage.getFrom();
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public InternetAddress[] getRecipients(javax.mail.Message.RecipientType to) {
		try {
			return (InternetAddress[]) mimeMessage.getRecipients(to);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public void setDate(String timeAsString) {
		try {
			mimeMessage.setHeader("Date", timeAsString);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private String getFirstHeaderOrNull(String headerName) {
		String[] header = this.getHeader(headerName);
		if(header == null)
			return null;
		return header[0];
	}

	public void delete() {
		try {
			mimeMessage.setFlag(Flags.Flag.DELETED, true);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	

	private void setHeader(String headerField, String value) {
		try {
			mimeMessage.setHeader(headerField, MimeUtility.encodeText(value));
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private String[] getHeader(String headerName) {
		try {
			String[] headers = this.mimeMessage.getHeader(headerName);
			List<String> decodedHeaders = new LinkedList<String>();
			for (String aHeader : headers) {
				decodedHeaders.add(MimeUtility.decodeText(aHeader));
			}
			
			return decodedHeaders.toArray(new String[0]);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public void setSentDate(Date time) {
		try {
			mimeMessage.setSentDate(time);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
