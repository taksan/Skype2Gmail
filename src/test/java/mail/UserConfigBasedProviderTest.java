package mail;

import org.junit.Test;

import skype.Skype2GmailConfigContentsMock;
import skype2gmail.Skype2GmailConfigContents;

public class UserConfigBasedProviderTest {
	@Test
	public void testSimple() {
		Skype2GmailConfigContents configContents = new Skype2GmailConfigContentsMock();
		UserConfigBasedProvider userConfigBasedProvider = new UserConfigBasedProvider(configContents);
		
	}
}
