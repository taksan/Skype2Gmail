package skype2gmail;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import skype2disk.Skype2DiskModule;
import skype2disk.Skype2GmailConfigDir;
import skype2disk.mocks.BasePathMock;
import utils.Maybe;

import com.google.inject.AbstractModule;

public class Skype2GmailConfigContentsImplTest {
	private BasePathMock basePath;
	private Skype2GmailConfigDir mockConfigDir;
	private Skype2GmailConfigContents configContents;
	
	@Test
	public void defaultUserName_ShouldBeNull() {
		Maybe<String> username = configContents.getUserName();
		Assert.assertEquals(null, username.unbox());
		
	}
	
	@Test
	public void defaultPassword_ShouldBeNull() {
		Maybe<String> password = configContents.getPassword();
		Assert.assertEquals(null, password.unbox());
	}

	@Test
	public void defaultOutputIsVerbose_ShouldBeTrue()
	{
		boolean outputVerbose = configContents.isOutputVerbose();
		Assert.assertTrue("Default verbosity should be true", outputVerbose);
	}
	
	@Test
	public void defaultSyncWithRecents_ShouldBeFalse() {
		boolean syncWithRecentsDisabled = configContents.isSyncWithRecentsDisabled();
		Assert.assertFalse("Default syncWithRecentsDisabled should be false", syncWithRecentsDisabled);
	}
	
	@Test
	public void defaultSelectedRecorder_ShouldBeSkype2GmailModule() {
		Class<?> selectedRecorder = configContents.getSelectedRecorder();
		Assert.assertEquals(Skype2GmailModule.class.getName(), selectedRecorder.getName());
	}
	
	@Test
	public void setSelectedRecorder_ShouldChangeStoredRecorder()
	{
		Class<? extends AbstractModule> newSelectedRecorder = Skype2DiskModule.class;
		configContents.setSelectedRecorderModule(newSelectedRecorder);
		Class<?> selectedRecorderModule = configContents.getSelectedRecorder();
		Assert.assertEquals(newSelectedRecorder, selectedRecorderModule);
	}
	
	@Test
	public void readValuesFromFile_PropertiesShouldHaveValuesInFile() throws IOException {
		File configFile = mockConfigDir.getConfigFile();
		FileUtils.writeStringToFile(configFile, 
				"#Skype2Gmail configuration\n" + 
				"#Fri May 06 21:21:51 BRT 2011\n" + 
				"gmail.user=fee\n" + 
				"gmail.password=baz\n" +
				"skype.neverSyncWithRecentChats=true\n" +
				"verbosity=none\n" +
				"skype2gmail.selectedRecorder=skype2disk.Skype2DiskModule");
		
		Maybe<String> username = configContents.getUserName();
		Assert.assertEquals("fee", username.unbox());
		
		Maybe<String> password = configContents.getPassword();
		Assert.assertEquals("baz", password.unbox());
		
		boolean outputVerbose = configContents.isOutputVerbose();
		Assert.assertFalse("Verbosity should not be enabled", outputVerbose);
		
		boolean syncWithRecentsDisabled = configContents.isSyncWithRecentsDisabled();
		Assert.assertTrue("Sync with recents chats should be disabled", syncWithRecentsDisabled);
		
		Class<? extends AbstractModule> expectedRecorder = Skype2DiskModule.class;
		Class<?> selectedRecorderModule = configContents.getSelectedRecorder();
		Assert.assertEquals(expectedRecorder, selectedRecorderModule);
	}
	
	@Test
	public void testSetValues() throws IOException {
		
		configContents.setUserName("foo");
		Maybe<String> username = configContents.getUserName();
		Assert.assertEquals("foo", username.unbox());
		
		configContents.setPassword("bar");
		Maybe<String> password = configContents.getPassword();
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
	

	@Before
	public void setup()
	{
		basePath = new BasePathMock();
		mockConfigDir = new Skype2GmailConfigDir(basePath);
		configContents = new Skype2GmailConfigContentsImpl(mockConfigDir);
	}
	
	@After
	public void teardown() throws IOException {
		FileUtils.deleteDirectory(new File(basePath.getPath()));
	}	

	private String getContentsWithoutComments(File configFile)
			throws IOException {
		String contents = FileUtils.readFileToString(configFile);
		String contentsWithoutCommentLines = contents.replaceAll("(?m)#.*$", "");
		return contentsWithoutCommentLines.trim();
	}
}
