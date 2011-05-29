package mail.api;

import mail.UserShell;

public class CommandLineShell implements UserShell {

	@Override
	public String prompt(String message) {
		System.out.print(message);
		return System.console().readLine();
	}

	@Override
	public String promptPassword(String message) {
		System.out.print(message);
		char[] readPassword = System.console().readPassword();
		return new String(readPassword);
	}

	@Override
	public void displayWarning(String message) {
		System.out.print(message);
	}

}
