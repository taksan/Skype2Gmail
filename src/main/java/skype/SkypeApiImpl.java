package skype;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeApiImpl implements SkypeApi {

	private static final Logger LOGGER = Logger.getLogger(SkypeApiImpl.class);
	@Override
	public SkypeChat[] getAllChats() {
		try {
			Skype.getAllRecentChats();
			
			Chat[] allRecentChats = Skype.getAllChats();
			
			LOGGER.info(String.format("Found %d chats.", allRecentChats.length));
			
			List<SkypeChat> skypeChats = new LinkedList<SkypeChat>();
			for (Chat chat : allRecentChats) {
				skypeChats.add(new SkypeChatImpl(chat));
			}

			return skypeChats.toArray(new SkypeChat[0]);
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

}
