package mail;

import junit.framework.Assert;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Test;

import skype.commons.mocks.Skype2GmailConfigContentsMock;
import skype2gmail.Skype2GmailConfigContents;

public class UserConfigBasedProviderTest {

	private static final String USER_READ_FROM_SHELL = "TYPED_USERNAME";
	private static final String PASSWORD_READ_FROM_SHELL = "TYPED_PASSWORD";

	@Test
	public void testPromptWithoutPreviousConfigAndAcceptingToStorePassword() {
		Skype2GmailConfigContents configContents = new Skype2GmailConfigContentsMock();
		
		UserShell shellMock = new UserShellMock();
		UserConfigBasedProvider userConfigBasedProvider = new UserConfigBasedProvider(configContents, shellMock);
		
		Assert.assertEquals(USER_READ_FROM_SHELL, userConfigBasedProvider.getUser());
		Assert.assertEquals(PASSWORD_READ_FROM_SHELL, userConfigBasedProvider.getPassword());
		
		Assert.assertEquals(PASSWORD_READ_FROM_SHELL, configContents.getPassword().unbox());
		
		Assert.assertEquals(USER_READ_FROM_SHELL, userConfigBasedProvider.getUser());
		Assert.assertEquals(PASSWORD_READ_FROM_SHELL, userConfigBasedProvider.getPassword());
	}
	
	@Test
	public void testPromptWithoutPreviousConfigAndNotAcceptingToStorePassword() {
		Skype2GmailConfigContents configContents = new Skype2GmailConfigContentsMock();
		
		UserShellMock shellMock = new UserShellMock();
		UserConfigBasedProvider userConfigBasedProvider = new UserConfigBasedProvider(configContents, shellMock);
		shellMock.setDoNotAcceptPassword();
		
		Assert.assertEquals(USER_READ_FROM_SHELL, userConfigBasedProvider.getUser());
		Assert.assertEquals(PASSWORD_READ_FROM_SHELL, userConfigBasedProvider.getPassword());
		
		Assert.assertNull("Password should not have been stored", configContents.getPassword().unbox());
		
	}
	
	@Test
	public void testPromptWithPreviousConfig() {
		Skype2GmailConfigContents configContents = new Skype2GmailConfigContentsMock();
		String expectedUsername = "PREVIOUS_CONFIGURED_USERNAME";
		configContents.setUserName(expectedUsername);
		String expectedPassword = "PREVIOUS_CONFIGURED_PASSWORD";
		configContents.setPassword(expectedPassword);
		
		UserShellMock shellMock = new UserShellMock();
		UserConfigBasedProvider userConfigBasedProvider = new UserConfigBasedProvider(configContents, shellMock);
		
		Assert.assertEquals(expectedUsername, userConfigBasedProvider.getUser());
		Assert.assertEquals(expectedPassword, userConfigBasedProvider.getPassword());
	}
	
	private final class UserShellMock implements UserShell {
		private String acceptPasswordString = "y";
		private boolean mailPromptWasRequestedAlready = false;
		private boolean passwordPromptWasRequestedAlready = false;

		@Override
		public String prompt(String message) {
			if (message.equals("Type the mail username: ")) {
				if (mailPromptWasRequestedAlready) {
					throw new IllegalStateException("The user config based provider should have cached the username, and not request twice");
				}
				mailPromptWasRequestedAlready = true;
				return USER_READ_FROM_SHELL;
			}
			if (message.equals("Would you like to store the password?[y/n] ")) {
				return acceptPasswordString;
			}
			throw new NotImplementedException();
		}

		public void setDoNotAcceptPassword() {
			acceptPasswordString = "f";
		}

		@Override
		public String promptPassword(String message) {
			if (passwordPromptWasRequestedAlready) {
				throw new IllegalStateException(
						"The user config based provider should " +
						"have cached the password, and not request twice");
			}
			passwordPromptWasRequestedAlready = true;
			return PASSWORD_READ_FROM_SHELL;
		}

		@Override
		public void displayWarning(String message) {
		}
	}
}
