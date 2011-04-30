package skype2gmail.mocks;

import gmail.GmailMessage;
import skype2gmail.GmailMessageFactory;

public class GmailMessageFactoryMock implements GmailMessageFactory {

	@Override
	public GmailMessage factory() {
		return new GmailMessageMock();
	}

}
