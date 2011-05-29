package skype.commons;

import junit.framework.Assert;

import org.junit.Test;

public class EmptySkypeMessageTest {
	@Test
	public void testCreationAndThatInvocationsCausesException() {
		SkypeChatMessage subject = EmptySkypeMessage.produce();
		
		try {
			subject.getSenderId();
			Assert.fail("Should have thrown an exception!");
		} catch(SkypeMessageParsingException e) {
			Assert.assertEquals("Empty message produced? Impossible", e.getMessage());
		}
	}
}
