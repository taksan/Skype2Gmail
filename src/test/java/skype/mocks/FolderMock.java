package skype.mocks;

import gmail.GmailMessage;

import java.util.HashMap;
import java.util.Map;

import skype.SkypeChat;
import skype2gmail.GmailFolder;

public class FolderMock implements GmailFolder {

	
	private final Map<String, GmailMessage> messageList;
	private final String mockIndex;

	public FolderMock() {
		this(null);
	}
	
	public FolderMock(String mockIndex) {
		this.mockIndex = mockIndex;
		messageList = new HashMap<String, GmailMessage>();
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
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
	public GmailMessage retrieveMessageEntryFor(SkypeChat skypeChat) {
		GmailMessage[] storedMessages = this.getMessages();
		for (GmailMessage message : storedMessages) {
			String chatId = message.getChatId();
			if (skypeChat.getId().equals(chatId)) {
				return message;
			}
		}
		return null;
	}
	
	public GmailMessage[] getMessages() {
		return messageList.values().toArray(new GmailMessage[0]);
	}

	@Override
	public String retrieveIndexFromMail() {
		return mockIndex;
	}
}
