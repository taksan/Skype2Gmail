package skype2disk;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import skype.SkypeChat;
import skype.mocks.SkypeApiMock;
import testutils.IOHelper;

public class FileSystemStorageTest {

	@Test
	public void simpleTest() throws IOException {
		File baseDir = IOHelper.createTempDirOrCry();
		try {
			FileSystemStorage fileSystemStorage = new FileSystemStorage(baseDir.toString());
			
			SkypeChat chat = SkypeApiMock.produceChatMock("#42;$foo","moe","joe");
			FileSystemStorageEntry newEntry = fileSystemStorage.newEntry(chat);
			
			File entryFile = newEntry.getFile();
			
			Assert.assertEquals(baseDir.toString(), entryFile.getParent());
			Assert.assertEquals("42foo", entryFile.getName());
			newEntry.write("foo");
			newEntry.save();
			
			String contents = FileUtils.readFileToString(entryFile);
			Assert.assertEquals("foo", contents);
		}finally {
			baseDir.delete();
		}
	}

}
