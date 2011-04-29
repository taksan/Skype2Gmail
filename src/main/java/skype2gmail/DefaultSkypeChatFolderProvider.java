package skype2gmail;

public class DefaultSkypeChatFolderProvider implements SkypeChatFolderProvider {

	@Override
	public String getFolderName() {
		return "Skype-Chats";
	}
}
