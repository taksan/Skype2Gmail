package mail;

import junit.framework.Assert;

import org.junit.Test;

public class EmptySkypeMailMessageTest {
	@Test
	public void testInvocationOfImplementedMethods() {
		SkypeMailMessage subject = EmptySkypeMailMessage.create();
		
		Assert.assertNull(subject.getBody());
		subject.delete();
	}
}
