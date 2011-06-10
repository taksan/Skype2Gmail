package skype.commons.mocks;

import org.apache.commons.lang.NotImplementedException;

import skype2gmail.Skype2GmailConfigContents;
import utils.Maybe;

import com.google.inject.AbstractModule;

public class Skype2GmailConfigContentsMock implements Skype2GmailConfigContents {

	private boolean disableSynchingWithRecents = false;
	private String username;
	private String password;
	private Class<? extends AbstractModule> currentRecorder;

	@Override
	public Maybe<String> getUserName() {
		return new Maybe<String>(username);
	}

	@Override
	public void setUserName(String u) {
		this.username = u;
	}

	@Override
	public Maybe<String> getPassword() {
		return new Maybe<String>(password);
	}

	@Override
	public void setPassword(String p) {
		password = p;
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

	@Override
	public Class<? extends AbstractModule> getSelectedRecorder() {
		return currentRecorder;
	}

	@Override
	public void setSelectedRecorderModule(
			Class<? extends AbstractModule> recorderModuleClass) {
		currentRecorder = recorderModuleClass;
	}

}
