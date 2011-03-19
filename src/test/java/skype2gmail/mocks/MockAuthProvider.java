package skype2gmail.mocks;

import skype2gmail.UserAuthProvider;

public class MockAuthProvider implements UserAuthProvider {

	@Override
	public String getUser() {
		return "anhanga.tinhoso@gmail.com";
	}

	@Override
	public String getPassword() {
		return "nukekubi";
	}

}
