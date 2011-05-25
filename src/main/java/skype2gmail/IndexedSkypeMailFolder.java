package skype2gmail;


import javax.mail.search.SearchTerm;

import mail.SkypeMailFolder;
import mail.SkypeMailMessage;
import skype.SkypeChat;

import com.google.inject.Inject;

public class IndexedSkypeMailFolder implements SkypeMailFolder {
	SkypeMailFolder nonIndexedGmailFolder;
	private FolderIndex folderIndex;
	
	@Inject
	public IndexedSkypeMailFolder(NonIndexGmailFolderProvider nonIndexedFolderProvider, FolderIndex folderIndex) {
		this.nonIndexedGmailFolder = nonIndexedFolderProvider.get();
		this.folderIndex = folderIndex;
	}

	@Override
	public void deleteMessageBasedOnId(String chatId) {
		this.nonIndexedGmailFolder.deleteMessageBasedOnId(chatId);
	}

	@Override
	public void appendMessage(SkypeMailMessage gmailMessage) {
		this.nonIndexedGmailFolder.appendMessage(gmailMessage);
	}

	@Override
	public void close() {
		this.folderIndex.save();
		this.nonIndexedGmailFolder.close();
	}

	@Override
	public SkypeMailMessage retrieveMessageEntryFor(SkypeChat skypeChat) {
		MailMessageIndexEntryFactoryImpl gmailMessageIndexEntryFactory = new MailMessageIndexEntryFactoryImpl();
		String previousChatSignature = folderIndex.getSignatureFor(skypeChat.getId());
		
		if (previousChatSignature == null) {
			folderIndex.addIndexFor(skypeChat);
			return nonIndexedGmailFolder.retrieveMessageEntryFor(skypeChat);
		}
		
		return gmailMessageIndexEntryFactory.produce(nonIndexedGmailFolder, skypeChat, previousChatSignature);
	}

	@Override
	public SkypeMailMessage retrieveSingleMessageMatchingSearchTerm(SearchTerm st) {
		return this.nonIndexedGmailFolder.retrieveSingleMessageMatchingSearchTerm(st);
	}

	@Override
	public void replaceMessageMatchingTerm(SearchTerm chatIndexSearchTerm, SkypeMailMessage replacementMessage) {
		this.nonIndexedGmailFolder.replaceMessageMatchingTerm(chatIndexSearchTerm, replacementMessage);
	}
}
