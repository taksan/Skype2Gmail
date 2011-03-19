package skype2gmail;

import gmail.GmailFolder;
import gmail.GmailMessage;

import java.util.Date;

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
	private GmailMessage gmailMessage;
	private final SkypeChatDateFormat skypeChatDateFormat;
	private final RootFolderProvider rootFolderProvider;
	private final MessageBodyBuilder messageBodyBuilder = new MessageBodyBuilder();

	public GmailStorageEntry(
			SessionProvider sessionProvider,
			RootFolderProvider rootFolderProvider, 
			SkypeChat chat,
			SkypeChatDateFormat skypeChatDateFormat) 
	{
		this.rootFolderProvider = rootFolderProvider;
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
		
		gmailMessage.setText(messageBodyBuilder.getMessageBody());
	}

	@Override
	public void save() {
		GmailFolder root = rootFolderProvider.getInstance();
		root.appendMessage(gmailMessage);
	}

	@Override
	public void setLastModificationTime(Date time) {
		if (gmailMessage == null) {
			throw new IllegalStateException("You must store the message before invoking setLastModificationTime");
		}
	}

	@Override
	public SkypeChat getChat() {
		return chat;
	}

	@Override
	public void visitChatAuthor(String chatAuthor) {
		gmailMessage.setFrom(chatAuthor);
	}

	@Override
	public void visitChatId(String id) {
		gmailMessage.setHeader(GmailStorage.X_MESSAGE_ID, id);
	}

	@Override
	public void visitDate(Date time) {
		
	}

	@Override
	public void visitBodySignature(String bodySignature) {
		gmailMessage.setHeader(GmailStorage.X_BODY_SIGNATURE, bodySignature);
	}

	@Override
	public void visitMessagesSignatures(String signatures) {
		gmailMessage.setHeader(GmailStorage.X_MESSAGES_SIGNATURES, signatures);
	}

	@Override
	public void visitTopic(String topic) {
		gmailMessage.setSubject(topic);
	}

	private boolean wasSenderVisited = false;
	@Override
	public void visitPoster(SkypeUser skypeUser) {
		if (!wasSenderVisited && !skypeUser.isCurrentUser()) {
			wasSenderVisited = true;
		}
		else {
			gmailMessage.addRecipient(skypeUser);
		}
	}

	@Override
	public void visitMessage(SkypeChatMessage aChatMessage) {
		messageBodyBuilder.appendMessage(aChatMessage);
	}

	@Override
	public void visitLastModifiedDate(Date time) {
		gmailMessage.setHeader("Date", skypeChatDateFormat.format(time));
	}
}
