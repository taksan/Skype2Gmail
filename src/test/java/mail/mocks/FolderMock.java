package mail.mocks;


import java.util.HashMap;
import java.util.Map;

import javax.mail.search.HeaderTerm;
import javax.mail.search.SearchTerm;

import mail.SkypeMailFolder;
import mail.SkypeMailMessage;

import skype.SkypeChat;
import skype2gmail.FolderIndex;
import skype2gmail.mocks.SkypeMailMessageMock;

public class FolderMock implements SkypeMailFolder {

	
	private final Map<String, SkypeMailMessage> messageList;
	private String mockIndex;

	public FolderMock() {
		this(null);
	}
	
	public FolderMock(String mockIndex) {
		this.mockIndex = mockIndex;
		messageList = new HashMap<String, SkypeMailMessage>();
	}

	@Override
	public void appendMessage(SkypeMailMessage gmailMessage) {
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
	public SkypeMailMessage retrieveMessageEntryFor(SkypeChat skypeChat) {
		SkypeMailMessage[] storedMessages = this.getMessages();
		for (SkypeMailMessage message : storedMessages) {
			String chatId = message.getChatId();
			if (skypeChat.getId().equals(chatId)) {
				return message;
			}
		}
		return null;
	}
	
	public SkypeMailMessage[] getMessages() {
		return messageList.values().toArray(new SkypeMailMessage[0]);
	}

	@Override
	public SkypeMailMessage retrieveSingleMessageMatchingSearchTerm(SearchTerm st) {
		if (st == null || !st.equals(FolderIndex.CHAT_INDEX_SEARCH_TERM)) {
			throw new RuntimeException("Search term to retrieve index must be FolderIndex.CHAT_INDEX_SEARCH_TERM");
		}
		HeaderTerm ht = (HeaderTerm)st;
		if (ht.getHeaderName().equals(FolderIndex.INDEX_HEADER_NAME)) {
			SkypeMailMessage gmailMessage = messageList.get("INDEX");
			if (gmailMessage != null) {
				return gmailMessage;
			}
		}
		SkypeMailMessageMock gmailMessageMock = new SkypeMailMessageMock();
		gmailMessageMock.setMockBody(mockIndex);
		return gmailMessageMock;
	}

	@Override
	public void replaceMessageMatchingTerm(SearchTerm chatIndexSearchTerm,
			SkypeMailMessage replacementMessage) {
		mockIndex = replacementMessage.getBody();
		messageList.put("INDEX", replacementMessage);
	}
}
