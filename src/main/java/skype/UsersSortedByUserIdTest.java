package skype;

import junit.framework.Assert;

import org.junit.Test;

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
		
		UsersSortedByUserId members2 = new UsersSortedByUserId();
		SkypeUserImpl skypeUser4 = new SkypeUserImpl("joe", "JOE", false);
		members2.add(skypeUser4);		
		SkypeUserImpl skypeUser5 = new SkypeUserImpl("boe", "BOE", false);
		members2.add(skypeUser5);
		
		members.addAll(members2);
		
		Assert.assertEquals(3, members.size());
	}
}
