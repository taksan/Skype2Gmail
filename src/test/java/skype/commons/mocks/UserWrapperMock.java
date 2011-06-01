package skype.commons.mocks;

import com.skype.SkypeException;

import skype.commons.UserWrapper;

public class UserWrapperMock implements UserWrapper {

	private String fullname;
	private String id;

	public UserWrapperMock(String aId, String aFullname) {
		id = aId;
		fullname = aFullname;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getFullName() throws SkypeException {
		return fullname;
	}

}
