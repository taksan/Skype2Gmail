package skype.commons.mocks;

import skypeapi.wrappers.ProfileWrapper;

public class ProfileWrapperMock implements ProfileWrapper {

	private String fullname="mock-fullname";

	@Override
	public String getId() {
		return "mock-id";
	}

	@Override
	public String getFullName() {
		return fullname;
	}

	public void setFullname(String f) {
		fullname = f;
	}

}
