package skype2disk;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import skype.EmptySkypeChat;
import skype.SkypeChat;
import skype.mocks.PreviousSkypeChatMock;
import skype.mocks.SkypeApiMock;
import testutils.IOHelper;

public class FileSystemStorageTest {

	@Test
	public void creationAndRetrievalTest() throws IOException {
		File baseDir = IOHelper.createTempDirOrCry();
		try {
			SkypeChat chat = SkypeApiMock.produceChatMock("#42;$foo","moe","joe");
			
			FileSystemStorage fileSystemStorage = new FileSystemStorage(null, mockContentParser(), baseDir.toString());
			FileSystemStorageEntry newEntry = fileSystemStorage.newEntry(chat);
			newEntry.store(chat);
			newEntry.save();
			
			FileSystemStorageEntry previousEntry = fileSystemStorage.retrievePreviousEntryFor(chat);
			SkypeChat storedChat = previousEntry.getChat();
			
			Assert.assertEquals( "<Previous Entry>", storedChat.toString() );
		}finally {
			baseDir.delete();
		}
	}

	private FileDumpContentParser mockContentParser() {
		return new FileDumpContentParser() {
			
			@Override
			public SkypeChat parse(String fileContents) {
				return new PreviousSkypeChatMock();
			}
		};
	}
}
