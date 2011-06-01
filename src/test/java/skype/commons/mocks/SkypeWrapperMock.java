package skype.commons.mocks;

import skypeapi.wrappers.ChatWrapper;
import skypeapi.wrappers.ProfileWrapper;
import skypeapi.wrappers.SkypeWrapper;

import com.skype.SkypeException;

public class SkypeWrapperMock implements SkypeWrapper {

	private final ProfileWrapper profile;
	private String applicationName;
	private ChatWrapperMock[] allChats;
	private ChatWrapperMock[] recentChats;

	public SkypeWrapperMock() {
		profile = new ProfileWrapperMock();
		
	}

	@Override
	public ChatWrapper[] getAllRecentChats() throws SkypeException {
		return recentChats;
	}

	@Override
	public ChatWrapper[] getAllChats() throws SkypeException {
		return allChats;
	}

	@Override
	public ProfileWrapper getProfile() {
		return profile;
	}

	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getApplicationName() {
		return this.applicationName;
	}

	public void setAllChats(ChatWrapperMock... chats) {
		this.allChats = chats;
	}

	public void setRecentChats(ChatWrapperMock... chats) {
		this.recentChats = chats;
	}

}
