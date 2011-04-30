package skype.mocks;

import gmail.GmailMessage;

import java.util.HashMap;
import java.util.Map;

import javax.mail.search.SearchTerm;

import skype.SkypeChat;
import skype2gmail.FolderIndex;
import skype2gmail.GmailFolder;
import skype2gmail.GmailMessageMock;

public class FolderMock implements GmailFolder {

	
	private final Map<String, GmailMessage> messageList;
	private String mockIndex;

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
	public GmailMessage retrieveFirstMessageMatchingSearchTerm(SearchTerm st) {
		if (st == null || !st.equals(FolderIndex.CHAT_INDEX_SEARCH_TERM)) {
			throw new RuntimeException("Search term to retrieve index must be FolderIndex.CHAT_INDEX_SEARCH_TERM");
		}
		GmailMessageMock gmailMessageMock = new GmailMessageMock();
		gmailMessageMock.setMockBody(mockIndex);
		return gmailMessageMock;
	}

	@Override
	public void replaceMessageMatchingTerm(SearchTerm chatIndexSearchTerm,
			GmailMessage replacementMessage) {
		mockIndex = replacementMessage.getBody();
	}
}
