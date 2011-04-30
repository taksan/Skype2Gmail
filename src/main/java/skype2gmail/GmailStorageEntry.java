package skype2gmail;

import gmail.GmailMessage;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import skype.MessageBodyBuilder;
import skype.SkypeChat;
import skype.SkypeChatDateFormat;
import skype.SkypeChatMessage;
import skype.SkypeChatSetter;
import skype.SkypeUser;
import skype.StorageEntry;
import skype2disk.SkypeChatSetterVisitor;

public class GmailStorageEntry implements StorageEntry, SkypeChatSetterVisitor {

	private final SkypeChat chat;
	private final SkypeChatDateFormat skypeChatDateFormat;
	private final GmailFolder storeFolder;
	private final MessageBodyBuilder messageBodyBuilder = new MessageBodyBuilder();
	private final GmailMessageFactory gmailMessageFactory;
	private GmailMessage storedGmailMessage;
	private SkypeUser chatAuthor;

	public GmailStorageEntry(
			GmailFolder storeFolder, 
			SkypeChat chat,
			SkypeChatDateFormat skypeChatDateFormat,
			GmailMessageFactory gmailMessageFactory) 
	{
		this.storeFolder = storeFolder;
		this.chat = chat;
		this.skypeChatDateFormat = skypeChatDateFormat;
		this.gmailMessageFactory = gmailMessageFactory;
	}
	
	GmailMessage getMessage() {
		return this.storedGmailMessage;
	}

	@Override
	public void store(SkypeChatSetter content) {
		storedGmailMessage = gmailMessageFactory.factory();
		content.accept(this);
		
		storedGmailMessage.setBody(messageBodyBuilder.getMessageBody());
	}

	@Override
	public void save() {
		storeFolder.deleteMessageBasedOnId(storedGmailMessage.getChatId()); 
		storeFolder.appendMessage(storedGmailMessage);
	}

	@Override
	public void setLastModificationTime(Date time) {
		if (storedGmailMessage == null) {
			throw new IllegalStateException("You must store the message before invoking setLastModificationTime");
		}
		storedGmailMessage.setDate(skypeChatDateFormat.format(time));
		storedGmailMessage.setSentDate(time);
	}

	@Override
	public SkypeChat getChat() {
		return chat;
	}

	@Override
	public void visitChatAuthor(SkypeUser chatAuthor) {
		this.chatAuthor = chatAuthor;
		String fromUser = String.format("%s <%s>", chatAuthor.getDisplayName(), chatAuthor.getUserId());
		storedGmailMessage.setFrom(fromUser);
	}

	@Override
	public void visitChatId(String id) {
		storedGmailMessage.setChatId(id);
	}

	@Override
	public void visitDate(Date time) {
		
	}

	@Override
	public void visitBodySignature(String bodySignature) {
		storedGmailMessage.setBodySignature(bodySignature);
	}

	@Override
	public void visitMessagesSignatures(String signatures) {
		storedGmailMessage.setMessagesSignatures(signatures);
	}

	@Override
	public void visitTopic(String topic) {
		storedGmailMessage.setSubject(topic);
	}

	private Set<String> visitedPosters = new HashSet<String>();
	@Override
	public void visitPoster(SkypeUser skypeUser) {
		storedGmailMessage.addPoster(skypeUser);
		if (visitedPosters.contains(skypeUser.getUserId())) {
			return;
		}
		visitedPosters.add(skypeUser.getUserId());
		if (this.chatAuthor.getUserId().equals(skypeUser.getUserId())) {
			return;
		}
		storedGmailMessage.addRecipient(skypeUser);
	}

	@Override
	public void visitMessage(SkypeChatMessage aChatMessage) {
		messageBodyBuilder.appendMessage(aChatMessage);
	}

	@Override
	public void visitLastModifiedDate(Date time) {
		this.setLastModificationTime(time);
	}
}
