package skype2gmail;

import utils.Maybe;

import com.google.inject.AbstractModule;
import com.google.inject.ImplementedBy;

@ImplementedBy(Skype2GmailConfigContentsImpl.class)
public interface Skype2GmailConfigContents {
	public Maybe<String> getUserName();
	public void setUserName(String u);
	
	public Maybe<String> getPassword();
	public void setPassword(String p);
	
	public boolean isOutputVerbose();
	public boolean isSyncWithRecentsDisabled();
	public Class<? extends AbstractModule> getSelectedRecorder();
	public void setSelectedRecorderModule(Class<? extends AbstractModule> recorderModuleClass);
	
	
}
