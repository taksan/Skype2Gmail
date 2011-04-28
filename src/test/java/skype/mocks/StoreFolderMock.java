package skype.mocks;

import java.util.HashMap;
import java.util.Map;

import gmail.GmailFolder;
import gmail.GmailMessage;
import skype2gmail.GmailStoreFolder;

public class StoreFolderMock implements GmailStoreFolder, GmailFolder {

	
	private final Map<String, GmailMessage> messageList;

	public StoreFolderMock() {
		messageList = new HashMap<String, GmailMessage>();
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
		messageList.put(gmailMessage.getChatId(), gmailMessage);
	}

	@Override
	public GmailMessage[] getMessages() {
		return messageList.values().toArray(new GmailMessage[0]);
	}

	@Override
	public void deleteMessageBasedOnId(String chatId) {
		messageList.remove(chatId);
	}

	@Override
	public void close() {
	}
}
