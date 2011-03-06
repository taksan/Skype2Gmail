package skype;

import java.util.LinkedList;
import java.util.List;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeApiImpl implements SkypeApi {

	@Override
	public SkypeChat[] getAllChats() {
		try {
			Chat[] allRecentChats = Skype.getAllChats();
			
			List<SkypeChat> skypeChats = new LinkedList<SkypeChat>();
			for (Chat chat : allRecentChats) {
				skypeChats.add(new SkypeChatImpl(chat));
			}

			return skypeChats.toArray(new SkypeChat[0]);
		} catch (SkypeException e) {
			throw new IllegalStateException(e);
		}
	}

}
