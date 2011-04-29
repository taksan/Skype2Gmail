package skype2gmail;

import gmail.GmailMessage;
import skype.SkypeChat;

import com.google.inject.Inject;

public class IndexedGmailFolder implements GmailFolder {
	GmailFolder nonIndexedGmailFolder;
	private FolderIndex folderIndex;
	
	@Inject
	public IndexedGmailFolder(NonIndexGmailFolderProvider nonIndexedFolderProvider, FolderIndex folderIndex) {
		this.nonIndexedGmailFolder = nonIndexedFolderProvider.get();
		this.folderIndex = folderIndex;
	}

	@Override
	public void deleteMessageBasedOnId(String chatId) {
		this.nonIndexedGmailFolder.deleteMessageBasedOnId(chatId);
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
		this.nonIndexedGmailFolder.appendMessage(gmailMessage);
	}

	@Override
	public void close() {
		this.folderIndex.save();
		this.nonIndexedGmailFolder.close();
	}

	@Override
	public GmailMessage retrieveMessageEntryFor(SkypeChat skypeChat) {
		GmailMessageIndexEntryFactoryImpl gmailMessageIndexEntryFactory = new GmailMessageIndexEntryFactoryImpl();
		String previousChatSignature = folderIndex.getSignatureFor(skypeChat.getId());
		
		if (previousChatSignature == null) {
			folderIndex.addIndexFor(skypeChat);
			return nonIndexedGmailFolder.retrieveMessageEntryFor(skypeChat);
		}
		
		return gmailMessageIndexEntryFactory.produce(nonIndexedGmailFolder, skypeChat, previousChatSignature);
	}

	@Override
	public String retrieveIndexFromMail() {
		return this.nonIndexedGmailFolder.retrieveIndexFromMail();
	}
}
