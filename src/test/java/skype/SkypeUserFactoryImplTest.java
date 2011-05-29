package skype;

import junit.framework.Assert;

import org.junit.Test;

import skype.mocks.SkypeApiMock;

public class SkypeUserFactoryImplTest {
	@Test
	public void testProduce() {
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.setCurrentUser(new SkypeUserImpl("moe","bar",true));
		
		SkypeUserFactoryImpl subject = new SkypeUserFactoryImpl(skypeApi);
		SkypeUser skypeUser = subject.produce("joe", "JOE MOE");
		
		Assert.assertEquals("JOE MOE", skypeUser.getDisplayName());
		Assert.assertEquals(false, skypeUser.isCurrentUser());
		
		SkypeUser currentUser = subject.produce("moe", "bar");
		Assert.assertEquals(true, currentUser.isCurrentUser());		
	}
}
