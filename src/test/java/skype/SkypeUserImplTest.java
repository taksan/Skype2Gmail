package skype;

import org.junit.Test;

import junit.framework.Assert;

public class SkypeUserImplTest {
	@Test
	public void testInstantiation() {
		SkypeUserImpl skypeUser1 = new SkypeUserImpl("foo", "bar", false);
		Assert.assertEquals("foo", skypeUser1.getUserId());
		Assert.assertEquals("bar", skypeUser1.getDisplayName());
		Assert.assertEquals(false, skypeUser1.isCurrentUser());
		
		SkypeUserImpl skypeUser2 = new SkypeUserImpl("foo", null, false);
		Assert.assertEquals("foo", skypeUser2.getUserId());
		Assert.assertEquals("foo", skypeUser2.getDisplayName());
		Assert.assertEquals(false, skypeUser2.isCurrentUser());
		
		SkypeUserImpl skypeUser3 = new SkypeUserImpl("foo", "", false);
		Assert.assertEquals("foo", skypeUser3.getUserId());
		Assert.assertEquals("foo", skypeUser3.getDisplayName());
		Assert.assertEquals(false, skypeUser3.isCurrentUser());
	}
}
