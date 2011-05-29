package skype2gmail;


import java.util.Date;

import mail.SkypeMailMessage;
import skype.commons.SkypeChat;
import skype.commons.SkypeUser;
import skype.commons.TimeSortedMessages;
import skype.commons.UsersSortedByUserId;

public class LazySkypeChat implements SkypeChat {

	private final SkypeChat newChat;
	private final SkypeMailMessage previousChatMailMessage;
	private final MailMessageChatParserInterface gmailMessageChatParser;
	private SkypeChat actualPreviousChat = null;

	public LazySkypeChat(SkypeChat newChat, 
			SkypeMailMessage previousChatMessage,
			MailMessageChatParserInterface gmailMessageChatParser) {
		this.newChat = newChat;
		this.previousChatMailMessage = previousChatMessage;
		this.gmailMessageChatParser = gmailMessageChatParser;
	}
	
	@Override
	public String getId() {
		return newChat.getId();
	}
	
	@Override
	public String getBodySignature() {
		return previousChatMailMessage.getBodySignature();
	}

	@Override
	public String getTopic() {
		return previousChatMailMessage.getTopic();
	}

	@Override
	public Date getTime() {
		return previousChatMailMessage.getDate();
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
			actualPreviousChat = gmailMessageChatParser.parse(previousChatMailMessage); 
		}
		return actualPreviousChat;
	}

}
