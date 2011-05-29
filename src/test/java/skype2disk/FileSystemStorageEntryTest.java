package skype2disk;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import skype.commons.SkypeChat;
import skype.commons.SkypeChatSetter;
import skype.mocks.SkypeApiMock;
import testutils.DateHelper;
import testutils.IOHelper;

public class FileSystemStorageEntryTest {
	
	@Test
	public void testModificationTime() throws IOException {
		final SkypeChat skypeChat = SkypeApiMock.produceChatMock("#foo;$bar", "joe", "moe");
		
		final File baseDir = IOHelper.createTempDirOrCry();
		try {
			final FileSystemStorageEntry fileSystemStorageEntry = new FileSystemStorageEntry(skypeChat,baseDir);
			fileSystemStorageEntry.store(new SkypeChatSetter(skypeChat));
			
			Date lastModTime = DateHelper.makeDate(2011, 1, 2, 3, 42, 43);
			fileSystemStorageEntry.setLastModificationTime(lastModTime);
			fileSystemStorageEntry.save();
			
			final File writtenFile = fileSystemStorageEntry.getGeneratedFile();
			
			Assert.assertEquals(baseDir.toString(), writtenFile.getParent());
			Assert.assertEquals("foobar", writtenFile.getName());
			
			final long lastModified = writtenFile.lastModified();
			final long expectedModTimeInMillis = lastModTime.getTime();
			Assert.assertEquals(expectedModTimeInMillis, lastModified);
		}
		finally {
			FileUtils.deleteDirectory(baseDir);
		}
	}
}
