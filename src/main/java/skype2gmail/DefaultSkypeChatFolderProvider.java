package skype2gmail;

public class DefaultSkypeChatFolderProvider implements SkypeChatFolderProvider {

	@Override
	public String getFolder() {
		return "Skype-Chats";
	}
}