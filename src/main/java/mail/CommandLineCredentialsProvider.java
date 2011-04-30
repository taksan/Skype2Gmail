package mail;

import java.io.Console;

import skype.ApplicationException;
import skype2gmail.UserAuthProvider;

public class CommandLineCredentialsProvider implements UserAuthProvider {
	private String username;
	private String password;

	@Override
	public String getUser() {
		if (username == null) {
			System.out.print("Username: ");
			username = getConsoleOrCry().readLine();
		}
		return username;
	}

	@Override
	public String getPassword() {
		if (password == null) {
			System.out.print("Password: ");
			password = new String(getConsoleOrCry().readPassword());
		}
		return password;
	}
	
	private Console getConsoleOrCry() {
		Console console = System.console();
		if (console == null) {
			throw new ApplicationException(
					"Could not read from console because there's no console. " +
					"You may get past this problem setting gmail.username and " +
					"gmail.password manually in your configuration."
					);
		}
		return console;
	}
}
