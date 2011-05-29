package skype.commons;


import org.junit.Assert;
import org.junit.Test;

public class UsersSortedByUserIdTest {
	@Test
	public void testAdd() {
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

	@Test
	public void testFind() {
		UsersSortedByUserId members = new UsersSortedByUserId();

		SkypeUserImpl skypeUser1 = new SkypeUserImpl("joe", "JOE", false);
		members.add(skypeUser1);
		SkypeUser joe = members.findByDisplayName("joe");
		Assert.assertEquals("JOE", joe.getDisplayName());
		
		SkypeUser foundUser = members.findByDisplayName("joe-does-not-exist");
		
		try {
			foundUser.getDisplayName();
			Assert.fail("Should have thrown an exception!");
		}
		catch (SkypeMessageParsingException e) {
			Assert.assertEquals("User joe-does-not-exist was found on chat, but was not among its posters!", e.getMessage());
		}
	}
}
