package skype2gmail;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import mail.SkypeMailFolder;
import mail.SkypeMailMessage;
import mail.skypemail.SkypeMailMessageFactory;
import skype.commons.MessageBodyBuilder;
import skype.commons.SkypeChat;
import skype.commons.SkypeChatDateFormat;
import skype.commons.SkypeChatMessage;
import skype.commons.SkypeChatSetter;
import skype.commons.SkypeUser;
import skype.commons.StorageEntry;
import skype2disk.SkypeChatSetterVisitor;

public class MailStorageEntry implements StorageEntry, SkypeChatSetterVisitor {

	private final SkypeChat chat;
	private final SkypeChatDateFormat skypeChatDateFormat;
	private final SkypeMailFolder storeFolder;
	private final MessageBodyBuilder messageBodyBuilder = new MessageBodyBuilder();
	private final SkypeMailMessageFactory gmailMessageFactory;
	private SkypeMailMessage storedGmailMessage;
	private SkypeUser chatAuthor;

	public MailStorageEntry(
			SkypeMailFolder storeFolder, 
			SkypeChat chat,
			SkypeChatDateFormat skypeChatDateFormat,
			SkypeMailMessageFactory gmailMessageFactory) 
	{
		this.storeFolder = storeFolder;
		this.chat = chat;
		this.skypeChatDateFormat = skypeChatDateFormat;
		this.gmailMessageFactory = gmailMessageFactory;
	}
	
	SkypeMailMessage getMessage() {
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
