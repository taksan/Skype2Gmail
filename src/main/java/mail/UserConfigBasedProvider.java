package mail;

import skype2gmail.Skype2GmailConfigContents;
import skype2gmail.UserAuthProvider;
import utils.Maybe;

import com.google.inject.Inject;

public class UserConfigBasedProvider implements UserAuthProvider {
	
	private Skype2GmailConfigContents skype2GmailConfigContents;
	private CommandLineCredentialsProvider credentialsProvider;

	@Inject
	public UserConfigBasedProvider(Skype2GmailConfigContents configContents){
		skype2GmailConfigContents = configContents;
		credentialsProvider = new CommandLineCredentialsProvider();
	}

	@Override
	public String getUser() {
		Maybe<String> user = skype2GmailConfigContents.getProperty("gmail.user");
		if (user.unbox() == null) {
			String u = credentialsProvider.getUser();
			skype2GmailConfigContents.setProperty("gmail.user", u);
			return u;
		}
		return user.unbox();
	}

	@Override
	public String getPassword() {
		Maybe<String> password = skype2GmailConfigContents.getProperty("gmail.password");
		if (password.unbox() == null) {
			String p = credentialsProvider.getPassword();
			System.out.print("Would you like to store the password?[y/n] ");
			String answer = System.console().readLine();
			if (answer.equalsIgnoreCase("y")) {
				skype2GmailConfigContents.setProperty("gmail.password", p);
				System.out.println("Password stored as plain text in your configuration file!");
			}
			return p;
		}
		return password.unbox();
	}
}
