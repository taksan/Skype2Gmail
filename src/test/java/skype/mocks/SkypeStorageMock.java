package skype.mocks;

import com.google.inject.Inject;

import skype.ChatContentBuilderFactory;
import skype.SkypeChat;
import skype.SkypeStorage;
import skype.StorageEntry;

public class SkypeStorageMock implements SkypeStorage {
	final StringBuilder result = new StringBuilder();
	private final ChatContentBuilderFactory chatContentBuilderFactoryMock;

	@Inject
	public SkypeStorageMock(ChatContentBuilderFactory chatContentBuilderFactoryMock) {
		this.chatContentBuilderFactoryMock = chatContentBuilderFactoryMock;
	}

	@Override
	public StorageEntry newEntry(SkypeChat chat) {
		return new StorageEntryMock(this, chat, this.chatContentBuilderFactoryMock);
	}

	public void addEntryResult(String result) {
		this.result.append(result);
	}
	
	@Override
	public String toString() {
		return "@SkypeStorageMock:\n" + this.result.toString();
	}

}
