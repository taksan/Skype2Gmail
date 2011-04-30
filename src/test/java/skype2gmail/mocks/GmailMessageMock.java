package skype2gmail.mocks;

import gmail.GmailMessage;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.NotImplementedException;

import skype.SkypeChat;
import skype.SkypeUser;
import skype.UsersSortedByUserId;
import skype2gmail.SessionProviderImpl;

public class GmailMessageMock implements GmailMessage {

	private final SkypeChat previousSkypeChat;
	private String mockBody;
	private MimeMessage mimeMessage;

	public GmailMessageMock(SkypeChat previousSkypeChat) {
		this.previousSkypeChat = previousSkypeChat;
		this.mimeMessage = new MimeMessage(new SessionProviderImpl().getInstance());
		try {
			this.mimeMessage.setHeader(GmailMessage.X_MESSAGE_ID, previousSkypeChat.getId());
			this.mimeMessage.setContent("", "text/plain");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public GmailMessageMock() {
		this.previousSkypeChat  = null;
	}

	public void setMockBody(String mockBody) {
		this.mockBody = mockBody;
	}

	@Override
	public String[] getPosters() {
		List<String> posterIds = new LinkedList<String>();
		UsersSortedByUserId posters = previousSkypeChat.getPosters();
		for (SkypeUser skypeUser : posters) {
			posterIds.add(skypeUser.getUserId());
		}
		
		String[] postersArray = posterIds.toArray(new String[0]);
		Arrays.sort(postersArray);
		return postersArray;
	}
	

	@Override
	public MimeMessage getMimeMessage() {
		return mimeMessage;
	}

	@Override
	public void setFrom(String chatAuthor) {
		throw new NotImplementedException();
	}

	@Override
	public void setSubject(String topic) {
		throw new NotImplementedException();
	}

	@Override
	public void addRecipient(SkypeUser skypeUser) {
		throw new NotImplementedException();
	}

	@Override
	public void setBody(String messageBody) {
		this.mockBody = messageBody;
	}

	@Override
	public String getChatId() {
		return this.previousSkypeChat.getId();
	}

	@Override
	public String getBodySignature() {
		throw new NotImplementedException();
	}

	@Override
	public String getTopic() {
		throw new NotImplementedException();
	}

	@Override
	public String getBody() {
		return mockBody;
	}

	@Override
	public void setChatId(String id) {
		throw new NotImplementedException();
	}

	@Override
	public void setBodySignature(String bodySignature) {
		throw new NotImplementedException();
	}

	@Override
	public void setMessagesSignatures(String signatures) {
		throw new NotImplementedException();
	}

	@Override
	public Date getDate() {
		throw new NotImplementedException();
	}

	@Override
	public String[] getMessagesSignatures() {
		throw new NotImplementedException();
	}

	@Override
	public void addPoster(SkypeUser skypeUser) {
		throw new NotImplementedException();
	}

	@Override
	public InternetAddress[] getFrom() {
		throw new NotImplementedException();
	}

	@Override
	public InternetAddress[] getRecipients(RecipientType to) {
		throw new NotImplementedException();
	}

	@Override
	public void setDate(String timeAsString) {
		throw new NotImplementedException();
	}

	@Override
	public void delete() {
		throw new NotImplementedException();
	}

	@Override
	public void setSentDate(Date time) {
		throw new NotImplementedException();
	}

	@Override
	public void setCustomHeader(String indexHeaderName, String indexHeaderValue) {
	}
}
