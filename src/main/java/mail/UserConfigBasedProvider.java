package mail;

import skype2gmail.Skype2GmailConfigContents;
import skype2gmail.UserCredentialsProvider;
import utils.Maybe;

import com.google.inject.Inject;

public class UserConfigBasedProvider implements UserCredentialsProvider {
	
	private Skype2GmailConfigContents skype2GmailConfigContents;
	private UserShell userShell;
	private String passwordForThisSession;

	@Inject
	public UserConfigBasedProvider(Skype2GmailConfigContents configContents, UserShell userShell){
		skype2GmailConfigContents = configContents;
		this.userShell = userShell;
	}

	@Override
	public String getUser() {
		Maybe<String> user = skype2GmailConfigContents.getUserName();
		if (user.unbox() == null) {
			String u = userShell.prompt("Type the mail username: ");
			skype2GmailConfigContents.setUserName(u);
			return u;
		}
		return user.unbox();
	}

	@Override
	public String getPassword() {
		if (wasPasswordAlreadyRead())
			return passwordForThisSession;
		
		Maybe<String> password = skype2GmailConfigContents.getPassword();
		if (password.unbox() == null) {
			passwordForThisSession = readPasswordAndStoreInConfigurationFileIfUsersAccepts();
			return passwordForThisSession;
		}
		return password.unbox();
	}

	private String readPasswordAndStoreInConfigurationFileIfUsersAccepts() {
		String p = userShell.promptPassword("Type the mail password:");
		String answer = userShell.prompt("Would you like to store the password?[y/n] ");
		if (answer.equalsIgnoreCase("y")) {
			skype2GmailConfigContents.setPassword(p);
			userShell.displayWarning("Password stored as plain text in your configuration file!\n");
		}
		return p;
	}

	private boolean wasPasswordAlreadyRead() {
		return passwordForThisSession != null;
	}
}
