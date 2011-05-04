package skype2gmail.mocks;

import skype2gmail.UserCredentialsProvider;

public class MockCredentialsProvider implements UserCredentialsProvider {

	@Override
	public String getUser() {
		return "foo.bar@gmail.com";
	}

	@Override
	public String getPassword() {
		return "foobaz";
	}

}
