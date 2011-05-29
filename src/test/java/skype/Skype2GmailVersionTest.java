package skype;
import junit.framework.Assert;

import org.junit.Test;

import skype.Skype2GmailVersion;


public class Skype2GmailVersionTest {
	@Test
	public void testGetVersion() {
		Skype2GmailVersion skype2GmailVersion = new Skype2GmailVersion();
		String version = skype2GmailVersion.getVersion();
		Assert.assertEquals("1.0-FOO", version);
		
		String versionMessage = skype2GmailVersion.getVersionMessage();
		Assert.assertEquals("Skype2Gmail version \"1.0-FOO\"", versionMessage);
	}
}
