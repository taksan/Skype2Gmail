package skype.mocks;

import skype.commons.SkypeUser;
import skype.commons.SkypeUserFactory;
import skype.commons.SkypeUserImpl;

public class SkypeUserFactoryMock implements SkypeUserFactory {

	@Override
	public SkypeUser produce(String userId, String displayName) {
		return new SkypeUserImpl(userId, displayName, false);
	}

}
