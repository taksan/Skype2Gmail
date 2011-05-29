package skype2gmail;

import junit.framework.Assert;
import mail.SkypeMailMessage;

import org.junit.Test;

import skype.commons.SkypeChat;
import skype.mocks.PreviousSkypeChatMock;
import skype2gmail.mocks.SkypeMailMessageMock;
import testutils.SkypeChatBuilderHelper;

public class LazySkypeChatTest {
	private final class MailMessageChatParserInterfaceMock implements
			MailMessageChatParserInterface {
		private final SkypeChat actualPreviousChat;
		public boolean parseWasInvoked = false;

		private MailMessageChatParserInterfaceMock(SkypeChat actualPreviousChat) {
			this.actualPreviousChat = actualPreviousChat;
		}

		@Override
		public SkypeChat parse(SkypeMailMessage message) {
			if (parseWasInvoked)
				throw new RuntimeException("Parse should not be invoked twice!");
			parseWasInvoked = true;
			return actualPreviousChat;
		}
	}

	@Test
	public void testSimple() {
		SkypeChatBuilderHelper newChatBuilder = createNewChatBuilder();
		final SkypeChatBuilderHelper previousChatBuilder = createActualPreviousChatBuilder();
		
		final String chatId = "#camaron.goo/$goofoo;81ef2618fc9a6343";
		final SkypeChat newChat = newChatBuilder.getChat(chatId, "FOO");
		
		final SkypeChat previousNonCompleteChat = new PreviousSkypeChatMock();
		final SkypeChat actualPreviousChat = previousChatBuilder.getChat(chatId, "FOO");
		final SkypeMailMessageMock previousChatMessage = new SkypeMailMessageMock(previousNonCompleteChat);
		previousChatMessage.setSubject(previousNonCompleteChat.getTopic());
		previousChatMessage.setSentDate(previousNonCompleteChat.getTime());
		
		MailMessageChatParserInterfaceMock gmailMessageChatParser = new MailMessageChatParserInterfaceMock(actualPreviousChat); 
		
		LazySkypeChat subject = new LazySkypeChat(newChat, previousChatMessage, gmailMessageChatParser);
		
		String id = subject.getId();
		Assert.assertEquals(chatId, id);
		Assert.assertEquals(previousNonCompleteChat.getBodySignature(), subject.getBodySignature());
		Assert.assertEquals(previousNonCompleteChat.getTopic(), subject.getTopic());
		Assert.assertEquals(previousNonCompleteChat.getTime(), subject.getTime());
		
		Assert.assertEquals(false, gmailMessageChatParser.parseWasInvoked);
		
		Assert.assertEquals(actualPreviousChat.getChatAuthor(), subject.getChatAuthor());
		Assert.assertEquals(actualPreviousChat.getChatMessages(), subject.getChatMessages());
		Assert.assertEquals(actualPreviousChat.getPosters(), subject.getPosters());
		Assert.assertEquals(actualPreviousChat.getLastModificationTime(), subject.getLastModificationTime());
	}

	private SkypeChatBuilderHelper createActualPreviousChatBuilder() {
		final SkypeChatBuilderHelper previousChatBuilder = new SkypeChatBuilderHelper() {
			
			@Override
			public void addChatMessages() {
				addMessage("goofoo", "what's up", 3, 21, 15, 14, 18);
				addMessage("camaron.goo", "Howdy\n	I'm doing fine", 3, 21, 15, 18, 16);
				addMessage("goofoo", "A day has passed", 3, 22, 15, 14, 24);
			}
		};
		return previousChatBuilder;
	}

	private SkypeChatBuilderHelper createNewChatBuilder() {
		SkypeChatBuilderHelper newChatBuilder = new SkypeChatBuilderHelper() {
			
			@Override
			public void addChatMessages() {
				addMessage("goofoo", "what's up", 3, 21, 15, 14, 18);
				addMessage("camaron.goo", "Howdy\n	I'm doing fine", 3, 21, 15, 18, 16);
				addMessage("goofoo", "A day has passed", 3, 22, 15, 14, 24);
			}
		};
		return newChatBuilder;
	}
}
