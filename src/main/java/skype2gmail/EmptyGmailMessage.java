package skype2gmail;

import gmail.GmailMessage;

import java.util.Date;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.NotImplementedException;

import skype.SkypeUser;

public class EmptyGmailMessage implements GmailMessage {

	@Override
	public MimeMessage getMimeMessage() {
		throw new NotImplementedException();
	}
	

	@Override
	public String getBody() {
		return null;
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
		throw new NotImplementedException();
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
	public String[] getPosters() {
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
	}

	@Override
	public void setSentDate(Date time) {
		throw new NotImplementedException();
	}


	@Override
	public void setCustomHeader(String indexHeaderName, String indexHeaderValue) {
		throw new NotImplementedException();
	}


}
