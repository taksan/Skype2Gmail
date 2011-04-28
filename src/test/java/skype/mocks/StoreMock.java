package skype.mocks;

import gmail.GmailMessageImpl;
import gmail.GmailMessageInterface;

import java.util.HashMap;
import java.util.Map;

import skype.SkypeChat;
import skype2gmail.GmailFolder;

public class StoreMock implements GmailFolder {

	
	private final Map<String, GmailMessageInterface> messageList;

	public StoreMock() {
		messageList = new HashMap<String, GmailMessageInterface>();
	}

	@Override
	public void appendMessage(GmailMessageInterface gmailMessage) {
		messageList.put(gmailMessage.getChatId(), gmailMessage);
	}

	@Override
	public void deleteMessageBasedOnId(String chatId) {
		messageList.remove(chatId);
	}

	@Override
	public void close() {
	}

	@Override
	public GmailMessageInterface retrieveMessageEntryFor(SkypeChat skypeChat) {
		GmailMessageInterface[] storedMessages = this.getMessages();
		for (GmailMessageInterface message : storedMessages) {
			String chatId = message.getChatId();
			if (skypeChat.getId().equals(chatId)) {
				return message;
			}
		}
		return null;
	}
	
	public GmailMessageInterface[] getMessages() {
		return messageList.values().toArray(new GmailMessageImpl[0]);
	}
}
