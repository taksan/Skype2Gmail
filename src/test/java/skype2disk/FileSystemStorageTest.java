package skype2disk;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import skype.commons.SkypeChat;
import skype.commons.SkypeChatSetter;
import skype.commons.SkypeHistoryCli;
import skype.mocks.PreviousSkypeChatMock;
import skype.mocks.SkypeApiMock;
import skype2disk.mocks.BasePathMock;
import testutils.IOHelper;

public class FileSystemStorageTest {
	File baseTmpDir = IOHelper.createTempDirOrCry();
	Skype2GmailConfigDir skype2GmailConfigDir = new Skype2GmailConfigDir(new BasePathMock());

	@After
	public void tearDown() throws IOException {
		FileUtils.deleteDirectory(baseTmpDir);
	}

	@Test
	public void creationAndRetrievalTest() throws IOException {
		String[] args = new String[]{"-historyOutputDir", baseTmpDir.getCanonicalPath()};
		SkypeHistoryCli options = new SkypeHistoryCli(args, skype2GmailConfigDir);
		
		verifyOptions(options);
	}
	
	@Test
	public void createWithoutArguments_ShouldUserDefaultPath() {
		SkypeHistoryCli options = new SkypeHistoryCli(new String[0], skype2GmailConfigDir);
		verifyOptions(options);
	}

	private void verifyOptions(SkypeHistoryCli options) {
		SkypeChat chat = SkypeApiMock.produceChatMock("#42;$foo","moe","joe");
		FileSystemStorage fileSystemStorage = new FileSystemStorage(null, mockContentParser(), options);
		FileSystemStorageEntry newEntry = fileSystemStorage.newEntry(chat);
		newEntry.store(new SkypeChatSetter(chat));
		newEntry.save();
		
		FileSystemStorageEntry previousEntry = fileSystemStorage.retrievePreviousEntryFor(chat);
		SkypeChat storedChat = previousEntry.getChat();
		
		Assert.assertEquals( "<Previous Entry>", storedChat.toString() );
		
		Assert.assertEquals(FileSystemStorage.class.getSimpleName(), fileSystemStorage.getSyncId());
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
