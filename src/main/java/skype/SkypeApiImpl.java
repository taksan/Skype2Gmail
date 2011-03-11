package skype;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeApiImpl implements SkypeApi {

	private static final Logger LOGGER = Logger.getLogger(SkypeApiImpl.class);
	private final SkypeChatFactory chatFactory;
	private final Chat[] allRecentChats;
	
	@Inject
	public SkypeApiImpl(SkypeChatFactory chatFactory) {
		this.chatFactory = chatFactory;
		try {
			allRecentChats = Skype.getAllChats();
			
			LOGGER.info(String.format("Found %d chats.", allRecentChats.length));
		}catch(SkypeException e) {
			throw new IllegalStateException(e);
		}
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
		for (Chat chat : allRecentChats) {
			final SkypeChat skypeChat = chatFactory.produce(chat);
			visitor.visit(skypeChat);
		}
	}

}
