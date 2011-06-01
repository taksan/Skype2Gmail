package skypeapi.wrappers;


import com.google.inject.ImplementedBy;
import com.skype.SkypeException;

@ImplementedBy(SkypeWrapperImpl.class)
public interface SkypeWrapper {

	ChatWrapper[] getAllRecentChats() throws SkypeException;
	ChatWrapper[] getAllChats()  throws SkypeException;

	ProfileWrapper getProfile();
	boolean isRunning();
	void setApplicationName(String applicationName);
}
