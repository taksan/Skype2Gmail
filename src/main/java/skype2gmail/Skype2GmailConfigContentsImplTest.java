package skype2gmail;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import skype2disk.Skype2GmailConfigDir;
import skype2disk.mocks.BasePathMock;
import utils.Maybe;

public class Skype2GmailConfigContentsImplTest {
	private BasePathMock basePath;
	private Skype2GmailConfigDir mockConfigDir;

	@Before
	public void before()
	{
		basePath = new BasePathMock();
		mockConfigDir = new Skype2GmailConfigDir(basePath);
	}
	
	@After
	public void after() throws IOException {
		FileUtils.deleteDirectory(new File(basePath.getPath()));
	}
	
	@Test
	public void testDefaultValues() throws IOException {
		Skype2GmailConfigContentsImpl configContentsImpl = new Skype2GmailConfigContentsImpl(mockConfigDir);
		
		Maybe<String> username = configContentsImpl.getUserName();
		Assert.assertEquals(null, username.unbox());
		
		Maybe<String> password = configContentsImpl.getPassword();
		Assert.assertEquals(null, password.unbox());
		
		
		boolean outputVerbose = configContentsImpl.isOutputVerbose();
		Assert.assertTrue("Default verbosity should be true", outputVerbose);
		
		boolean syncWithRecentsDisabled = configContentsImpl.isSyncWithRecentsDisabled();
		Assert.assertFalse("Default syncWithRecentsDisabled should be false", syncWithRecentsDisabled);
	}
	
	@Test
	public void testValuesReadFromFile() throws IOException {
		Skype2GmailConfigContentsImpl configContentsImpl = new Skype2GmailConfigContentsImpl(mockConfigDir);
		File configFile = mockConfigDir.getConfigFile();
		FileUtils.writeStringToFile(configFile, 
				"#Skype2Gmail configuration\n" + 
				"#Fri May 06 21:21:51 BRT 2011\n" + 
				"gmail.user=fee\n" + 
				"gmail.password=baz\n" +
				"skype.neverSyncWithRecentChats=true\n" +
				"verbosity=none");
		
		Maybe<String> username = configContentsImpl.getUserName();
		Assert.assertEquals("fee", username.unbox());
		
		Maybe<String> password = configContentsImpl.getPassword();
		Assert.assertEquals("baz", password.unbox());
		
		boolean outputVerbose = configContentsImpl.isOutputVerbose();
		Assert.assertFalse("Verbosity should not be enabled", outputVerbose);
		
		boolean syncWithRecentsDisabled = configContentsImpl.isSyncWithRecentsDisabled();
		Assert.assertTrue("Sync with recents chats should be disabled", syncWithRecentsDisabled);
	}
	
	@Test
	public void testSetValues() throws IOException {
		Skype2GmailConfigContentsImpl configContentsImpl = new Skype2GmailConfigContentsImpl(mockConfigDir);
		
		configContentsImpl.setUserName("foo");
		Maybe<String> username = configContentsImpl.getUserName();
		Assert.assertEquals("foo", username.unbox());
		
		configContentsImpl.setPassword("bar");
		Maybe<String> password = configContentsImpl.getPassword();
		Assert.assertEquals("bar", password.unbox());
		
		File configFile = mockConfigDir.getConfigFile();
		String contents = getContentsWithoutComments(configFile);
		
		String expected = 
			"gmail.user=foo\n" + 
			"gmail.password=bar";
		Assert.assertEquals(
				expected, 
				contents);
	}

	private String getContentsWithoutComments(File configFile)
			throws IOException {
		String contents = FileUtils.readFileToString(configFile);
		String contentsWithoutCommentLines = contents.replaceAll("(?m)#.*$", "");
		return contentsWithoutCommentLines.trim();
	}
}
