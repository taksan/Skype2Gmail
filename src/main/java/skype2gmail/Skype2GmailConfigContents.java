package skype2gmail;

import utils.Maybe;

public interface Skype2GmailConfigContents {
	public Maybe<String> getUserName();
	public void setUserName(String u);
	
	public Maybe<String> getPassword();
	public void setPassword(String p);
	
	public boolean isOutputVerbose();
	public boolean isSyncWithRecentsDisabled();
	
	
}
