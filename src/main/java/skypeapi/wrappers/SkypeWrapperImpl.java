package skypeapi.wrappers;

import java.util.LinkedList;

import skype.exceptions.ApplicationException;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.connector.Connector;

public class SkypeWrapperImpl implements SkypeWrapper {

	@Override
	public ChatWrapper[] getAllRecentChats() throws SkypeException {
		return wrapChatArray(Skype.getAllRecentChats());
	}

	@Override
	public ChatWrapper[] getAllChats() throws SkypeException {
		return wrapChatArray(Skype.getAllChats());
	}

	@Override
	public ProfileWrapper getProfile() {
		return ProfileWrapperImpl.wrap(Skype.getProfile());
	}

	@Override
	public boolean isRunning() {
		try {
			return Skype.isRunning();
		} catch (SkypeException e) {
			throw new ApplicationException(e);
		}
	}
	
	@Override
	public void setApplicationName(String applicationName) {
		Connector.getInstance().setApplicationName(applicationName);
	}
	
	private ChatWrapper[] wrapChatArray(Chat[] chats) {
		LinkedList<ChatWrapper> chatList = new LinkedList<ChatWrapper>();
		for (Chat chat : chats) {
			chatList.add(ChatWrapperImpl.wrap(chat));
		}
		return chatList.toArray(new ChatWrapper[0]);
	}
}