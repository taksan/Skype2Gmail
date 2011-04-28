package skype2gmail;

import gmail.GmailFolder;
import gmail.GmailMessage;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.mail.Session;

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
	private final Session session;
	private final SkypeChatDateFormat skypeChatDateFormat;
	private final GmailStoreFolder storeFolder;
	private final MessageBodyBuilder messageBodyBuilder = new MessageBodyBuilder();
	private GmailMessage gmailMessage;
	private SkypeUser chatAuthor;

	public GmailStorageEntry(
			SessionProvider sessionProvider,
			GmailStoreFolder storeFolder, 
			SkypeChat chat,
			SkypeChatDateFormat skypeChatDateFormat) 
	{
		this.storeFolder = storeFolder;
		this.session = sessionProvider.getInstance();
		this.chat = chat;
		this.skypeChatDateFormat = skypeChatDateFormat;
	}
	
	GmailMessage getMessage() {
		return this.gmailMessage;
	}

	@Override
	public void store(SkypeChatSetter content) {
		gmailMessage = new GmailMessage(session);
		content.accept(this);
		
		gmailMessage.setBody(messageBodyBuilder.getMessageBody());
	}

	@Override
	public void save() {
		GmailFolder root = storeFolder.getFolder();
		root.deleteMessageBasedOnId(gmailMessage.getChatId()); 
		root.appendMessage(gmailMessage);
	}

	@Override
	public void setLastModificationTime(Date time) {
		if (gmailMessage == null) {
			throw new IllegalStateException("You must store the message before invoking setLastModificationTime");
		}
		gmailMessage.setDate(skypeChatDateFormat.format(time));
		gmailMessage.setSentDate(time);
	}

	@Override
	public SkypeChat getChat() {
		return chat;
	}

	@Override
	public void visitChatAuthor(SkypeUser chatAuthor) {
		this.chatAuthor = chatAuthor;
		String fromUser = String.format("%s <%s>", chatAuthor.getDisplayName(), chatAuthor.getUserId());
		gmailMessage.setFrom(fromUser);
	}

	@Override
	public void visitChatId(String id) {
		gmailMessage.setChatId(id);
	}

	@Override
	public void visitDate(Date time) {
		
	}

	@Override
	public void visitBodySignature(String bodySignature) {
		gmailMessage.setBodySignature(bodySignature);
	}

	@Override
	public void visitMessagesSignatures(String signatures) {
		gmailMessage.setMessagesSignatures(signatures);
	}

	@Override
	public void visitTopic(String topic) {
		gmailMessage.setSubject(topic);
	}

	private Set<String> visitedPosters = new HashSet<String>();
	@Override
	public void visitPoster(SkypeUser skypeUser) {
		gmailMessage.addPoster(skypeUser);
		if (visitedPosters.contains(skypeUser.getUserId())) {
			return;
		}
		visitedPosters.add(skypeUser.getUserId());
		if (this.chatAuthor.getUserId().equals(skypeUser.getUserId())) {
			return;
		}
		gmailMessage.addRecipient(skypeUser);
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
