package skype;

import junit.framework.Assert;

import org.junit.Test;

import skype.mocks.SkypeChatMock;
import testutils.SkypeChatHelper;

public class SkypeChatImplTest {
	@Test
	public void testBodyContentId() {
		SkypeChatMock chat = SkypeChatHelper.createSkypeTestEnvironment();
		
		SkypeChatImpl skypeChatImpl = new SkypeChatImpl(chat.getId(), chat.getTime(), chat.getTopic(), chat.getMembersIds(), 
				chat.getChatMessages());
		
		String chatContentId = skypeChatImpl.getChatContentId();
		final String expected="14#35ff9458ed508c794ad0c309e12902f452cdc25cd5991bc2422cd51f7c4272fd";
		Assert.assertEquals(expected, chatContentId);
	}
}
