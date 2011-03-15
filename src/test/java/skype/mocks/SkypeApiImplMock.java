package skype.mocks;

import skype.SkypeApi;
import skype.SkypeApiChatVisitor;
import testutils.SkypeChatHelper;

public class SkypeApiImplMock implements SkypeApi {

	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public void accept(SkypeApiChatVisitor visitor) {
		final String[] users = new String[]{"joe", "moe"};
		visitor.visit(SkypeChatHelper.createSkypeImplForTest("$foo#42;", "foo", users));
		visitor.visit(SkypeChatHelper.createSkypeImplForTest("$foo#43;", "bazbar", users));
	}

}
