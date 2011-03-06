package skype.mocks;

import java.util.Date;

import skype.SkypeChat;
import skype.SkypeChatMessage;
import skype.StorageEntry;

public class StorageEntryMock implements StorageEntry {

	private final SkypeStorageMock skypeStorageMock;
	private final StringBuilder stringBuilder;
	private Date lastModificationTime;

	public StorageEntryMock(SkypeStorageMock skypeStorageMock, SkypeChat chat) {
		this.skypeStorageMock = skypeStorageMock;
		this.stringBuilder = new StringBuilder();
		this.stringBuilder.append("@StorageEntryMock: ------\n");
		this.stringBuilder.append("chatId:");
		this.stringBuilder.append(chat.getId());
		this.stringBuilder.append(", topic:");
		this.stringBuilder.append(chat.getTopic());
		this.stringBuilder.append(", date:");
		this.stringBuilder.append(SkypeChatMessage.dateFormat.format(chat.getTime()));
		this.stringBuilder.append("\n");
	}

	@Override
	public void write(String content) {
		stringBuilder.append(content);
		stringBuilder.append("\n");
	}

	@Override
	public void save() {
		stringBuilder.append("Last modified:");
		stringBuilder.append(SkypeChatMessage.dateFormat.format(this.lastModificationTime));
		this.stringBuilder.append("\n");
		String stringfiedContent = stringBuilder.toString();
		this.skypeStorageMock.addEntryResult(stringfiedContent);
	}

	@Override
	public void setLastModificationTime(Date time) {
		this.lastModificationTime = time;
	}

}
