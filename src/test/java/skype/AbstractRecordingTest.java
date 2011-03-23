package skype;

import skype.mocks.SkypeApiImplMock;
import testutils.SkypeChatBuilderHelper;

import com.google.inject.Injector;

public abstract class AbstractRecordingTest {
	protected void testRecording(final Injector injector) {
		final SkypeHistoryRecorder historyRecorder = injector.getInstance(SkypeHistoryRecorder.class);
		
		final SkypeApiImplMock skypeApi = (SkypeApiImplMock) injector.getInstance(SkypeApi.class);
		
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {
			@Override
			public void addChatMessages() {
				addMessage("joe", "howdy", 3, 21, 15, 01, 00);
				addMessage("moe", "hiiya", 3, 21, 15, 02, 00);
			}
		};
		
		skypeApi.addChat(chatHelper.getChat("$foo#42;", "foo"));
		skypeApi.addChat(chatHelper.getChat("$foo#43;", "bazbar"));
		skypeApi.addChat(chatHelper.getChat("$foo#44", "TOPIC"));
		
		historyRecorder.record();
		
		chatHelper.addMessage("joe", "Here I am again", 3, 23, 15, 20, 1);
		skypeApi.addChat(chatHelper.getChat("$foo#44", "TOPIC"));
		
		historyRecorder.record();
	}
}
