package skype.mocks;

import org.apache.commons.lang.NotImplementedException;

import skype.commons.SkypeUser;

public class EmptySkypeUser implements SkypeUser {

	@Override
	public String getUserId() {
		throw new NotImplementedException();
	}

	@Override
	public String getDisplayName() {
		throw new NotImplementedException();
	}

	@Override
	public boolean isCurrentUser() {
		throw new NotImplementedException();
	}

	@Override
	public String getMailAddress() {
		throw new NotImplementedException();
	}

	@Override
	public String getPosterHeader() {
		throw new NotImplementedException();
	}

	@Override
	public String toString() {
		return "";
	}
}
