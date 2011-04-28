package gmail;

import skype2gmail.UserAuthProvider;

import com.google.inject.Inject;

public class UserConfigBasedProvider implements UserAuthProvider {
	
	private Skype2GmailConfigContents skype2GmailConfigContents;

	@Inject
	public UserConfigBasedProvider(Skype2GmailConfigContents configContents){
		skype2GmailConfigContents = configContents;
	}

	@Override
	public String getUser() {
		return skype2GmailConfigContents.getProperty("gmail.user", true);
	}

	@Override
	public String getPassword() {
		return skype2GmailConfigContents.getProperty("gmail.password", true);
	}

}
