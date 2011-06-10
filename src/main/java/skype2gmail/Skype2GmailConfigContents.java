package skype2gmail;

import skype.commons.Skype2StorageModuleCommons;
import utils.Maybe;

public interface Skype2GmailConfigContents {
	public Maybe<String> getUserName();
	public void setUserName(String u);
	
	public Maybe<String> getPassword();
	public void setPassword(String p);
	
	public boolean isOutputVerbose();
	public boolean isSyncWithRecentsDisabled();
	public Class<? extends Skype2StorageModuleCommons> getSelectedRecorder();
	public void setSelectedRecorderModule(Class<? extends Skype2StorageModuleCommons> recorderModuleClass);
	
	
}
