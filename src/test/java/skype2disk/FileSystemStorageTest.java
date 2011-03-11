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
			newEntry.write(chat);
			newEntry.save();
			
			final String contents = FileUtils.readFileToString(entryFile);
			final String expected = 
					"Chat Content Code: content-id-mock\n" + 
					"Chat topic: FOO\n" + 
					"Chat [#42;$foo] at 2011/04/21 15:00:00\n" + 
					"Chat members: [moe,joe]\n" + 
					"Messages Ids: [17f4007f9024da870afae8e60f6635fd,49b2215b046c7df8f731b7a0f48416e1]\n" + 
					"[15:14:18] MOE: Hya\n" + 
					"[15:14:24] JOE: Howdy";
			Assert.assertEquals(expected, contents);
		}finally {
			baseDir.delete();
		}
	}

}
