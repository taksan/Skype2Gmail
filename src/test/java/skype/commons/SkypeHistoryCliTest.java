package skype.commons;
import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import skype.commons.SkypeHistoryCli;
import skype2disk.Skype2GmailConfigDir;
import skype2disk.mocks.BasePathMock;


public class SkypeHistoryCliTest {
	@Test
	public void testSyncToDiskAndOutDir() {
		String fakeHistoryDir = "foo";
		String[] args = new String[]{"-d","-out",fakeHistoryDir};
		Skype2GmailConfigDir skype2GmailConfigDir = null;
		SkypeHistoryCli cli = new SkypeHistoryCli(args, skype2GmailConfigDir);
		File historyDir = cli.getHistoryDir();
		Assert.assertEquals(fakeHistoryDir, historyDir.getName());
		Assert.assertTrue("Sync to disk option passed, but method is returning false", cli.isSyncToDisk());
		Assert.assertFalse("Sync to mail option not passed, but method is returning true", cli.isSyncToMail());
		Assert.assertFalse("Version option not passed, but method is returning true", cli.hasVersion());
		Assert.assertFalse("Help option not passed, but method is returning true", cli.hasHelp());
	}
	
	@Test
	public void testVersionAndMailOption() {
		String[] args = new String[]{"-m","-v", "-h"};
		BasePathMock basePath = new BasePathMock();
		Skype2GmailConfigDir skype2GmailConfigDir = new Skype2GmailConfigDir(basePath);
		SkypeHistoryCli cli = new SkypeHistoryCli(args, skype2GmailConfigDir);
		File historyDir = cli.getHistoryDir();
		String defaultHistoryDirectory = "history";
		Assert.assertEquals(defaultHistoryDirectory, historyDir.getName());
		Assert.assertFalse("Sync to disk option not passed, but method is returning true", cli.isSyncToDisk());
		Assert.assertTrue("Sync to mail option passed, but method is returning false", cli.isSyncToMail());
		Assert.assertTrue("Version option passed, but method is returning false", cli.hasVersion());
		Assert.assertTrue("Help option passed, but method is returning false", cli.hasHelp());
	}
}
