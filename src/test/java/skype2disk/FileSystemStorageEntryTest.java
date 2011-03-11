package skype2disk;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import skype.ChatContentBuilder;
import skype.SkypeChat;
import skype.mocks.SkypeApiMock;
import testutils.DateHelper;
import testutils.IOHelper;

public class FileSystemStorageEntryTest {
	
	@Test
	public void testGenerationAndModificationTime() throws IOException {
		SkypeChat skypeChat = SkypeApiMock.produceChatMock("#foo;$bar", "joe", "moe");
		
		File baseDir = IOHelper.createTempDirOrCry();
		try {
			FileSystemStorageEntry fileSystemStorageEntry = new FileSystemStorageEntry(skypeChat,baseDir);
			
			fileSystemStorageEntry.write(skypeChat);
			Date lastModTime = DateHelper.makeDate(2011, 1, 2, 3, 42, 43);
			
			fileSystemStorageEntry.setLastModificationTime(lastModTime);
			fileSystemStorageEntry.save();
			
			File writtenFile = fileSystemStorageEntry.getFile();
			long lastModified = writtenFile.lastModified();
			
			long expectedModTimeInMillis = lastModTime.getTime();
			Assert.assertEquals(expectedModTimeInMillis, lastModified);
			
			final ChatContentBuilder contentBuilder = new FileDumpContentBuilderFactory().produce(skypeChat);
			final String expected = contentBuilder.getContent();
			
			final String actual = FileUtils.readFileToString(writtenFile);
			
			Assert.assertEquals(expected, actual);
		}
		finally {
			baseDir.delete();
		}
	}
}
