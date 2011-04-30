package skype2gmail;

import gmail.GmailMessage;

public class GmailMessageFactoryMock implements GmailMessageFactory {

	@Override
	public GmailMessage factory() {
		return new GmailMessageMock();
	}

}
