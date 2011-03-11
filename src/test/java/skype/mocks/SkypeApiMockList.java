package skype.mocks;

import org.apache.commons.lang.NotImplementedException;

import skype.SkypeApi;
import skype.SkypeApiChatVisitor;

public class SkypeApiMockList implements SkypeApi{

	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public void accept(SkypeApiChatVisitor visitor) {
		throw new NotImplementedException();
	}

}
