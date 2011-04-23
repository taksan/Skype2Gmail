package skype;

import org.junit.Test;

import junit.framework.Assert;

public class UsersSortedByUserIdTest {
	@Test
	public void testAdd()
	{
		UsersSortedByUserId members = new UsersSortedByUserId();
		
		SkypeUserImpl skypeUser1 = new SkypeUserImpl("joe", "JOE", false);
		members.add(skypeUser1);
		SkypeUserImpl skypeUser2 = new SkypeUserImpl("joe", "JOE", false);
		members.add(skypeUser2);
		SkypeUserImpl skypeUser3 = new SkypeUserImpl("joe", "JOE BANANA", false);
		members.add(skypeUser3);		
		
		Assert.assertEquals(2, members.size());
	}
}
