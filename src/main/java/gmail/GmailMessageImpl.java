package gmail;

import java.io.IOException;
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

import skype.MessageProcessingException;
import skype.SkypeUser;

public class GmailMessageImpl implements GmailMessageInterface {
	private MimeMessage mimeMessage;
	private HeaderCodec headerCodec = new HeaderCodec();
	
	public GmailMessageImpl(Session session) {
		this.mimeMessage = new MimeMessage(session);
	}

	public GmailMessageImpl(MimeMessage message) {
		this.mimeMessage = message;
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#getMimeMessage()
	 */
	@Override
	public MimeMessage getMimeMessage() {
		return mimeMessage;
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#setFrom(java.lang.String)
	 */
	@Override
	public void setFrom(String chatAuthor) {
		try {
			mimeMessage.setFrom(new InternetAddress(chatAuthor));
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#setSubject(java.lang.String)
	 */
	@Override
	public void setSubject(String topic) {
		try {
			mimeMessage.setSubject(topic);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#addRecipient(skype.SkypeUser)
	 */
	@Override
	public void addRecipient(SkypeUser skypeUser) {
		try {
			mimeMessage.addRecipient(RecipientType.TO, 
					new InternetAddress(skypeUser.getMailAddress()));
		} catch (AddressException e) {
			throw new MessageProcessingException(e);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#setBody(java.lang.String)
	 */
	@Override
	public void setBody(String messageBody) {
		try {
			mimeMessage.setText(messageBody, "UTF-8");
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#getChatId()
	 */
	@Override
	public String getChatId() {
		return getFirstHeaderOrNull(GmailMessageInterface.X_MESSAGE_ID);
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#getBodySignature()
	 */
	@Override
	public String getBodySignature() {
		return getFirstHeaderOrNull(GmailMessageInterface.X_BODY_SIGNATURE);
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#getTopic()
	 */
	@Override
	public String getTopic() {
		try {
			return this.mimeMessage.getSubject();
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#getBody()
	 */
	@Override
	public String getBody() {
		try {
			return (String) mimeMessage.getContent();
		} catch (IOException e) {
			throw new MessageProcessingException(e);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#setChatId(java.lang.String)
	 */
	@Override
	public void setChatId(String id) {
		this.setHeader(GmailMessageInterface.X_MESSAGE_ID, id);
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#setBodySignature(java.lang.String)
	 */
	@Override
	public void setBodySignature(String bodySignature) {
		this.setHeader(GmailMessageInterface.X_BODY_SIGNATURE, bodySignature);
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#setMessagesSignatures(java.lang.String)
	 */
	@Override
	public void setMessagesSignatures(String signatures) {
		this.setHeader(GmailMessageInterface.X_MESSAGES_SIGNATURES, signatures);
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#getDate()
	 */
	@Override
	public Date getDate() {
		try {
			return mimeMessage.getSentDate();
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#getMessagesSignatures()
	 */
	@Override
	public String[] getMessagesSignatures() {
		String signatures = this.getFirstHeaderOrNull(X_MESSAGES_SIGNATURES);
		return signatures.split(",");
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#getPosters()
	 */
	@Override
	public String[] getPosters() {
		return this.getHeader(X_SKYPE_POSTERS);
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#addPoster(skype.SkypeUser)
	 */
	@Override
	public void addPoster(SkypeUser skypeUser) {
		this.addHeader(X_SKYPE_POSTERS, skypeUser.getPosterHeader());
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#getFrom()
	 */
	@Override
	public InternetAddress[] getFrom() {
		try {
			return (InternetAddress[]) mimeMessage.getFrom();
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#getRecipients(javax.mail.Message.RecipientType)
	 */
	@Override
	public InternetAddress[] getRecipients(javax.mail.Message.RecipientType to) {
		try {
			return (InternetAddress[]) mimeMessage.getRecipients(to);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#setDate(java.lang.String)
	 */
	@Override
	public void setDate(String timeAsString) {
		try {
			mimeMessage.setHeader("Date", timeAsString);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	private String getFirstHeaderOrNull(String headerName) {
		String[] header = this.getHeader(headerName);
		if (header == null)
			return null;
		return header[0];
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#delete()
	 */
	@Override
	public void delete() {
		try {
			mimeMessage.setFlag(Flags.Flag.DELETED, true);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	private void setHeader(String headerField, String value) {
		try {
			mimeMessage.setHeader(headerField, headerCodec.encodeText(value));
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		} 
	}

	private void addHeader(String headerField, String value) {
		try {
			mimeMessage.addHeader(headerField, headerCodec.encodeText(value));
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		} 
	}

	private String[] getHeader(String headerName) {
		try {
			String[] headers = this.mimeMessage.getHeader(headerName);
			List<String> decodedHeaders = new LinkedList<String>();
			for (String aHeader : headers) {
				decodedHeaders.add(headerCodec.decodeText(aHeader));
			}

			return decodedHeaders.toArray(new String[0]);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		} 
	}

	/* (non-Javadoc)
	 * @see gmail.GmailMessageInterface#setSentDate(java.util.Date)
	 */
	@Override
	public void setSentDate(Date time) {
		try {
			mimeMessage.setSentDate(time);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}
}
