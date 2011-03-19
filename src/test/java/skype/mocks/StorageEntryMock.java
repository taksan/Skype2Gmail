package skype.mocks;

import java.util.Date;

import skype.SkypeChat;
import skype.SkypeChatMessage;
import skype.SkypeChatSetter;
import skype.SkypeUser;
import skype.StorageEntry;
import skype2disk.SkypeChatSetterVisitor;

public class StorageEntryMock implements StorageEntry, Comparable<StorageEntryMock>, SkypeChatSetterVisitor {

	private final StringBuilder stringBuilder;
	private Date lastModificationTime;
	private final SkypeChat chat;

	public StorageEntryMock(SkypeChat chat) {
		this.chat = chat;
		this.stringBuilder = new StringBuilder();
		this.stringBuilder.append("@StorageEntryMock: ------\n");
	}

	@Override
	public void store(SkypeChatSetter content) {
		content.accept(this);
	}

	@Override
	public void save() {
		stringBuilder.append("Last modified:");
		stringBuilder.append(SkypeChatMessage.chatDateFormat.format(this.lastModificationTime));
		this.stringBuilder.append("\n");
	}

	@Override
	public void setLastModificationTime(Date time) {
		this.lastModificationTime = time;
	}

	@Override
	public int compareTo(StorageEntryMock paramT) {
		return this.toString().compareTo(paramT.toString());
	}

	@Override
	public String toString() {
		return this.stringBuilder.toString();
	}

	public SkypeChat getChat() {
		return chat;
	}

	// SkypeChatSetterVisitor
	
	@Override
	public void visitChatAuthor(String chatAuthor) {
		stringBuilder.append("chatAuthor:"+chatAuthor+"\n");
	}

	@Override
	public void visitChatId(String id) {
		stringBuilder.append("chatId:"+id+"\n");
	}

	@Override
	public void visitDate(Date time) {
		String formattedTime = SkypeChatMessage.chatDateFormat.format(time);
		stringBuilder.append("Time:"+formattedTime+"\n");
	}

	@Override
	public void visitBodySignature(String bodySignature) {
		stringBuilder.append("Body Signature:"+bodySignature+"\n");
	}

	@Override
	public void visitMessagesSignatures(String signatures) {
		stringBuilder.append("Messages Signatures:"+signatures+"\n");
	}

	@Override
	public void visitTopic(String topic) {
		stringBuilder.append("Topic:"+topic+"\n");
	}

	@Override
	public void visitPoster(SkypeUser skypeUser) {
		stringBuilder.append("Poster:"+skypeUser+"\n");
	}

	@Override
	public void visitMessage(SkypeChatMessage skypeChatMessage) {
		stringBuilder.append(skypeChatMessage);
	}

	@Override
	public void visitLastModifiedDate(Date time) {
	}
}
