package gmail;

import org.apache.commons.lang.NotImplementedException;

import skype2gmail.UserAuthProvider;

public class UserHomeConfigProvider implements UserAuthProvider {

	@Override
	public String getUser() {
		throw new NotImplementedException();
	}

	@Override
	public String getPassword() {
		throw new NotImplementedException();
	}

}
