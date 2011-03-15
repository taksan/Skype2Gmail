package skype;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import skype.mocks.SkypeChatMock;
import testutils.DigestProviderForTestFactory;
import testutils.SkypeChatHelper;
import utils.DigestProvider;

public class SkypeChatImplTest {
	@Test
	public void testBodyContentId() {
		final SkypeChatMock chat = SkypeChatHelper.createSkypeTestEnvironment();
		
		final DigestProvider digestProvider = DigestProviderForTestFactory.getInstance();
		final SkypeChatImpl skypeChatImpl = new SkypeChatImpl(
				digestProvider, 
				chat.getId(), 
				chat.getTime(), 
				chat.getTopic(), 
				chat.getMembersIds(), 
				chat.getChatMessages());
		
		final String chatContentId = skypeChatImpl.getBodySignature();
		final String expected="14#35ff9458ed508c794ad0c309e12902f452cdc25cd5991bc2422cd51f7c4272fd";
		Assert.assertEquals(expected, chatContentId);
		
		final Date lastModificationTime = skypeChatImpl.getLastModificationTime();
		final String actualTime = SkypeChatMessage.chatDateFormat.format(lastModificationTime);
		final String expectedTime = "2011/03/21 15:18:16";
		Assert.assertEquals(expectedTime , actualTime);
	}
}
