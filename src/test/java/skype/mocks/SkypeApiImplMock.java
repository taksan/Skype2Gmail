package skype.mocks;

import java.util.LinkedList;
import java.util.List;

import skype.SkypeApi;
import skype.SkypeApiChatVisitor;
import skype.SkypeChat;

public class SkypeApiImplMock implements SkypeApi {

	private List<SkypeChat> chats;

	public SkypeApiImplMock(){
		chats = new LinkedList<SkypeChat>();
	}
	
	public void addChat(SkypeChat skypeChat) {
		chats.add(skypeChat);
	}
	
	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public void accept(SkypeApiChatVisitor visitor) {
//		final String[] users = new String[]{"joe", "moe"};
//		visitor.visit(SkypeChatHelper.createSkypeImplForTest("$foo#42;", "foo", users));
//		visitor.visit(SkypeChatHelper.createSkypeImplForTest("$foo#43;", "bazbar", users));
		
		for (SkypeChat skypeChat : chats) {
			visitor.visit(skypeChat);
		}
	}

}
