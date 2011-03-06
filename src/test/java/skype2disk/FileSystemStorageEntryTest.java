package skype2disk;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import junit.framework.Assert;

import skype.SkypeChat;
import skype.mocks.SkypeApiMock;
import skype.mocks.SkypeChatMock;
import testutils.DateHelper;
import testutils.IOHelper;

public class FileSystemStorageEntryTest {
	@Test
	public void testModificationTime() {
		SkypeChat skypeChat = SkypeApiMock.produceChatMock("#foo;$bar", "joe", "moe");
		
		File baseDir = IOHelper.createTempDirOrCry();
		try {
			FileSystemStorageEntry fileSystemStorageEntry = new FileSystemStorageEntry(skypeChat,baseDir);
			
			fileSystemStorageEntry.write("foo");
			Date lastModTime = DateHelper.makeDate(2011, 1, 2, 3, 42, 43);
			
			fileSystemStorageEntry.setLastModificationTime(lastModTime);
			fileSystemStorageEntry.save();
			
			File writtenFile = fileSystemStorageEntry.getFile();
			long lastModified = writtenFile.lastModified();
			
			long expectedModTimeInMillis = lastModTime.getTime();
			Assert.assertEquals(expectedModTimeInMillis, lastModified);
		}
		finally {
			baseDir.delete();
		}
	}

}
