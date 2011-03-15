package skype;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeApiImpl implements SkypeApi {

	private static final Logger LOGGER = Logger.getLogger(SkypeApiImpl.class);
	private final SkypeChatFactory chatFactory;
	private Chat[] allChats;
	
	@Inject
	public SkypeApiImpl(SkypeChatFactory chatFactory) {
		this.chatFactory = chatFactory;
	}
	
	@Override
	public boolean isRunning() {
		try {
			return Skype.isRunning();
		} catch (SkypeException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void accept(SkypeApiChatVisitor visitor) {
		try {
			Chat[] allChatsArray = getAllChats();
			
			LOGGER.info(String.format("Found %d chats.", allChatsArray.length));
		}catch(SkypeException e) {
			throw new IllegalStateException(e);
		}
		for (Chat chat : allChats) {
			final SkypeChat skypeChat = chatFactory.produce(chat);
			visitor.visit(skypeChat);
		}
	}

	private Chat[] getAllChats() throws SkypeException {
		if (allChats != null)
			return allChats;
		allChats = Skype.getAllChats();
		return allChats;
	}

}
