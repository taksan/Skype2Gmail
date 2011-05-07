package skype2disk;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import skype.SkypeChat;
import skype.SkypeChatSetter;
import skype.SkypeHistoryCli;
import skype.mocks.PreviousSkypeChatMock;
import skype.mocks.SkypeApiMock;
import skype2disk.mocks.BasePathMock;
import testutils.IOHelper;

public class FileSystemStorageTest {

	@Test
	public void creationAndRetrievalTest() throws IOException {
		File baseTmpDir = IOHelper.createTempDirOrCry();
		
		String[] args = new String[]{"-historyOutputDir", baseTmpDir.getCanonicalPath()};
		Skype2GmailConfigDir skype2GmailConfigDir = new Skype2GmailConfigDir(new BasePathMock());
		SkypeHistoryCli options = new SkypeHistoryCli(args, skype2GmailConfigDir);
		try {
			SkypeChat chat = SkypeApiMock.produceChatMock("#42;$foo","moe","joe");
			FileSystemStorage fileSystemStorage = new FileSystemStorage(null, mockContentParser(), options);
			FileSystemStorageEntry newEntry = fileSystemStorage.newEntry(chat);
			newEntry.store(new SkypeChatSetter(chat));
			newEntry.save();
			
			FileSystemStorageEntry previousEntry = fileSystemStorage.retrievePreviousEntryFor(chat);
			SkypeChat storedChat = previousEntry.getChat();
			
			Assert.assertEquals( "<Previous Entry>", storedChat.toString() );
		}finally {
			FileUtils.deleteDirectory(baseTmpDir);
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
