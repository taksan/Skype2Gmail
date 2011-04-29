package skype2gmail;

import gmail.GmailMessage;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.NotImplementedException;

import skype.SkypeChat;
import skype.SkypeUser;
import skype.UsersSortedByUserId;

public class GmailMessageMock implements GmailMessage {

	private final SkypeChat previousSkypeChat;

	public GmailMessageMock(SkypeChat previousSkypeChat) {
		this.previousSkypeChat = previousSkypeChat;
		
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
		throw new NotImplementedException();
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
		throw new NotImplementedException();
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
		throw new NotImplementedException();
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

}
