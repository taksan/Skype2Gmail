package skypeapi.wrappers;

import skype.commons.UserWrapper;

import com.skype.SkypeException;
import com.skype.User;

public class UserWrapperImpl implements UserWrapper {
	
	public static UserWrapperImpl wrap(User user) {
		return new UserWrapperImpl(user);
	}

	private final User user;

	public UserWrapperImpl(User user) {
		this.user = user;
		
	}

	@Override
	public String getId() {
		return user.getId();
	}

	@Override
	public String getFullName() throws SkypeException {
		return user.getFullName();
	}

}
