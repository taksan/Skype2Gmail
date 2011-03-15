package skype.mocks;

import java.util.Date;

import skype.ChatContentBuilder;
import skype.ChatContentBuilderFactory;
import skype.SkypeChat;
import skype.SkypeChatMessage;
import skype.StorageEntry;

public class StorageEntryMock implements StorageEntry, Comparable<StorageEntryMock> {

	private final StringBuilder stringBuilder;
	private Date lastModificationTime;
	private final ChatContentBuilderFactory chatContentBuilderFactory;
	private final SkypeChat chat;

	public StorageEntryMock(SkypeChat chat, ChatContentBuilderFactory chatContentBuilderFactory) {
		this.chat = chat;
		this.chatContentBuilderFactory = chatContentBuilderFactory;
		this.stringBuilder = new StringBuilder();
		this.stringBuilder.append("@StorageEntryMock: ------\n");
		this.stringBuilder.append("chatId:");
		this.stringBuilder.append(chat.getId());
		this.stringBuilder.append(", topic:");
		this.stringBuilder.append(chat.getTopic());
		this.stringBuilder.append(", date:");
		this.stringBuilder.append(SkypeChatMessage.chatDateFormat.format(chat.getTime()));
		this.stringBuilder.append("\n");
	}

	@Override
	public void store(SkypeChat content) {
		ChatContentBuilder produce = this.chatContentBuilderFactory.produce(content);
		stringBuilder.append(produce.getContent());
		stringBuilder.append("\n");
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
}
