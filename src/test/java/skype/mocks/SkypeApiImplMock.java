package skype.mocks;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import skype.commons.SkypeApi;
import skype.commons.SkypeApiChatVisitor;
import skype.commons.SkypeChat;
import skype.commons.SkypeUser;

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
		for (SkypeChat skypeChat : chats) {
			visitor.visit(skypeChat);
		}
	}

	@Override
	public SkypeUser getCurrentUser() {
		throw new NotImplementedException();
	}
}
