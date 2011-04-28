package skype2gmail;

import gmail.GmailMessage;

import java.util.Date;

import skype.SkypeChat;
import skype.SkypeUser;
import skype.TimeSortedMessages;
import skype.UsersSortedByUserId;

public class LazySkypeChat implements SkypeChat {

	private final SkypeChat baseChat;
	private final GmailMessage previousChatMessage;
	private final GmailMessageChatParser gmailMessageChatParser;
	private SkypeChat actualPreviousChat = null;

	public LazySkypeChat(SkypeChat baseChat, GmailMessage previousChatMessage,
			GmailMessageChatParser gmailMessageChatParser) {
		this.baseChat = baseChat;
		this.previousChatMessage = previousChatMessage;
		this.gmailMessageChatParser = gmailMessageChatParser;
	}
	
	@Override
	public String getId() {
		return baseChat.getId();
	}
	
	@Override
	public String getBodySignature() {
		return previousChatMessage.getBodySignature();
	}

	@Override
	public String getTopic() {
		return previousChatMessage.getTopic();
	}

	@Override
	public Date getTime() {
		return previousChatMessage.getDate();
	}

	@Override
	public SkypeUser getChatAuthor() {
		return getActualPreviousChat().getChatAuthor();
	}

	@Override
	public SkypeChat merge(SkypeChat skypeChat) {
		return getActualPreviousChat().merge(skypeChat);
	}	

	@Override
	public TimeSortedMessages getChatMessages() {
		return getActualPreviousChat().getChatMessages();
	}

	@Override
	public UsersSortedByUserId getPosters() {
		return getActualPreviousChat().getPosters();
	}

	@Override
	public Date getLastModificationTime() {
		return getActualPreviousChat().getLastModificationTime();
	}

	private SkypeChat getActualPreviousChat() {
		if (actualPreviousChat == null) {
			actualPreviousChat = gmailMessageChatParser.parse(previousChatMessage); 
		}
		return actualPreviousChat;
	}

}
