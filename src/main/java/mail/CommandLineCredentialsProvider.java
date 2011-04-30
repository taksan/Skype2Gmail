package mail;

import skype2gmail.UserAuthProvider;

public class CommandLineCredentialsProvider implements UserAuthProvider {
	private String username;
	private String password;

	@Override
	public String getUser() {
		if (username == null) {
			System.out.print("Username: ");
			username = System.console().readLine();
		}
		return username;
	}

	@Override
	public String getPassword() {
		if (password == null) {
			System.out.print("Password: ");
			password = new String(System.console().readPassword());
		}
		System.out.println(password);
		return password;
	}

}
