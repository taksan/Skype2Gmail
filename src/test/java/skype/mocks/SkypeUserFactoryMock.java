package skype.mocks;

import skype.SkypeUser;
import skype.SkypeUserFactory;
import skype.SkypeUserImpl;

public class SkypeUserFactoryMock implements SkypeUserFactory {

	@Override
	public SkypeUser produce(String userId, String displayName) {
		return new SkypeUserImpl(userId, displayName, false);
	}

}
