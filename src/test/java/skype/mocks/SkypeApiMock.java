package skype.mocks;

import java.util.LinkedList;
import java.util.List;

import skype.SkypeApi;
import skype.SkypeChat;

public class SkypeApiMock implements SkypeApi {
	private List<SkypeChatMock> mockChatList = new LinkedList<SkypeChatMock>();

	@Override
	public SkypeChat[] getAllRecentChats() {
		
		
		return mockChatList.toArray(new SkypeChatMock[0]);
	}
	
	public void addMockChat(SkypeChatMock mockChat){
		mockChatList.add(mockChat);
	}
}
