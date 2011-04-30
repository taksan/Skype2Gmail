package skype;

import org.apache.commons.lang.NotImplementedException;

import skype2gmail.Skype2GmailConfigContents;
import utils.Maybe;

public class Skype2GmailConfigContentsMock implements Skype2GmailConfigContents {

	private boolean disableSynchingWithRecents = false;

	@Override
	public Maybe<String> getUserName() {
		throw new NotImplementedException();
	}

	@Override
	public void setUserName(String u) {
		throw new NotImplementedException();
	}

	@Override
	public Maybe<String> getPassword() {
		throw new NotImplementedException();
	}

	@Override
	public void setPassword(String p) {
		throw new NotImplementedException();
	}

	@Override
	public boolean isOutputVerbose() {
		throw new NotImplementedException();
	}

	public void setSyncWithRecentsDisabled(boolean b) {
		disableSynchingWithRecents = b;
	}

	@Override
	public boolean isSyncWithRecentsDisabled() {
		return disableSynchingWithRecents;
	}

}
