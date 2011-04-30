package mail;

import java.util.Date;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import skype.SkypeUser;

public interface SkypeMailMessage {

	public static final String X_MESSAGES_SIGNATURES = "X-SKYPE-MESSAGES-SIGNATURES";
	public static final String X_BODY_SIGNATURE = "X-SKYPE-BODY-SIGNATURE";
	public static final String X_MESSAGE_ID = "X-SKYPE-MESSAGE-ID";
	public static final String X_SKYPE_POSTERS = "X_SKYPE_POSTERS";
	public static final String X_SKYPE_2_GMAIL_INDEX = "X_SKYPE_2_GMAIL_INDEX";

	public abstract MimeMessage getMimeMessage();

	public abstract void setFrom(String chatAuthor);

	public abstract void setSubject(String topic);

	public abstract void addRecipient(SkypeUser skypeUser);

	public abstract void setBody(String messageBody);

	public abstract String getChatId();

	public abstract String getBodySignature();

	public abstract String getTopic();

	public abstract String getBody();

	public abstract void setChatId(String id);

	public abstract void setBodySignature(String bodySignature);

	public abstract void setMessagesSignatures(String signatures);

	public abstract Date getDate();

	public abstract String[] getMessagesSignatures();

	public abstract String[] getPosters();

	public abstract void addPoster(SkypeUser skypeUser);

	public abstract InternetAddress[] getFrom();

	public abstract InternetAddress[] getRecipients(
			javax.mail.Message.RecipientType to);

	public abstract void setDate(String timeAsString);

	public abstract void delete();

	public abstract void setSentDate(Date time);

	public abstract void setCustomHeader(String indexHeaderName, String indexHeaderValue);

}