package skype2gmail;

import com.google.inject.Singleton;

@Singleton
public class DefaultSkypeChatFolderProvider implements SkypeChatFolderProvider {

	@Override
	public String getFolderName() {
		return "Skype-Chats";
	}
}
