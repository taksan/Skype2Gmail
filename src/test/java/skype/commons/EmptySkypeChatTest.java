package skype.commons;

import org.junit.Assert;
import org.junit.Test;

import skype.mocks.SkypeChatMock;
import utils.SimpleLoggerProvider;

public class EmptySkypeChatTest {
	@Test
	public void testFields() {
		EmptySkypeChat subject = new  EmptySkypeChat(new SimpleLoggerProvider());
		Assert.assertEquals("", subject.getBodySignature());
		Assert.assertEquals("", subject.getId());
		Assert.assertEquals("", subject.getTopic());
		Assert.assertEquals(subject.time, subject.getTime());
		
		SkypeChat skypeChat = new SkypeChatMock("foo", null, null, new String[0]);
		SkypeChat mergedChat = subject.merge(skypeChat);
		
		Assert.assertTrue(skypeChat == mergedChat);
	}
}
