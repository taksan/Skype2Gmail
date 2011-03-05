package skype2gmail;

import org.junit.Test;

public class Skype2GmailTest {
	@Test
	public void testHappyDay()
	{
		Skype2Gmail skypeToGmailRecorder = new Skype2Gmail("","");
		skypeToGmailRecorder.record();
	}

}
