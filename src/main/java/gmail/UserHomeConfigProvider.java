package gmail;

import skype2gmail.UserAuthProvider;

import com.google.inject.Inject;

public class UserHomeConfigProvider implements UserAuthProvider {
	
	private Skype2GmailConfigContents skype2GmailConfigContents;

	@Inject
	public UserHomeConfigProvider(Skype2GmailConfigContents configContents){
		skype2GmailConfigContents = configContents;
	}

	@Override
	public String getUser() {
		return skype2GmailConfigContents.getProperty("gmail.user");
	}

	@Override
	public String getPassword() {
		return skype2GmailConfigContents.getProperty("gmail.password");
	}

}
