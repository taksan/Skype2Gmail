package skype.commons;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import skype.commons.mocks.ChatMessageWrapperMock;
import skype.commons.mocks.ChatWrapperMock;
import skype.commons.mocks.ProfileWrapperMock;
import skype.commons.mocks.SkypeWrapperMock;
import skype.commons.mocks.UserWrapperMock;
import skype.mocks.SkypeUserFactoryMock;
import skypeapi.wrappers.ProfileWrapper;
import testutils.DateHelper;
import utils.DigestProvider;
import utils.LoggerProvider;
import utils.SimpleLoggerProvider;

import com.skype.SkypeException;

public class SkypeApiImplTest {

	private final SkypeWrapperMock skypeWrapper = new SkypeWrapperMock();
	private SkypeApiImpl subject;
	private ChatFetchStrategyChooserMock chatFetchStrategy;

	@Test
	public void testApplicationName() throws SkypeException {
		Assert.assertEquals("Skype2Gmail", skypeWrapper.getApplicationName());
	}
	
	@Test
	public void testIsRunning() throws SkypeException {
		Assert.assertEquals(skypeWrapper.isRunning(), subject.isRunning());
	}
	
	@Test
	public void testGetCurrentUser() throws SkypeException {
		ProfileWrapper profile = skypeWrapper.getProfile();
		
		SkypeUser currentUser = subject.getCurrentUser();
		Assert.assertEquals(profile.getId(), currentUser.getUserId());
		Assert.assertEquals(profile.getFullName(), currentUser.getDisplayName());
	}
	
	@Test
	public void testGetCurrentUserWithNullFullname() throws SkypeException {
		ProfileWrapperMock profile = (ProfileWrapperMock) skypeWrapper.getProfile();
		profile.setFullname(null);
		
		SkypeUser currentUser = subject.getCurrentUser();
		Assert.assertEquals(profile.getId(), currentUser.getUserId());
		Assert.assertEquals(profile.getId(), currentUser.getDisplayName());
	}
	
	@Test
	public void testAcceptForAllChats() throws SkypeException {
		SkypeChatVisitorMock visitor = new SkypeChatVisitorMock();
		ChatWrapperMock chat1 = createChat("chat-id-1", "joe", "moe", DateHelper.makeDate(2011, 1, 2, 11, 10, 10));
		ChatWrapperMock chat2 = createChat("chat-id-2", "cloe", "viboe", DateHelper.makeDate(2012, 1, 2, 11, 10, 10));
		skypeWrapper.setAllChats(chat1, chat2);
		
		subject.accept(visitor);
		
		String visitedChats = visitor.getVisitedChatsAsString().trim();
		
		String expected = "Chat Id: chat-id-1\n" + 
				"Chat Time: 2011/02/02 11:10:10\n" + 
				"Chat Body Signature: d703b1c0b9a97addacbe64dc68da7fa3fe1cbfb866fedf2a74db93769b44d8ae\n" + 
				"Messages signatures: [8d7ff576e03dd96097d7100a7d68ad06,758313a3683be8c147555cb1189f5dfa]\n" + 
				"Chat topic: (2 lines) CHAT TOPIC\n" + 
				"Poster: id=joe; display=joe Foo\n" + 
				"Poster: id=moe; display=moe Foo\n" + 
				"[2011/02/02 11:12:10] joe Foo: I say hi\n" + 
				"[2011/02/02 11:15:10] moe Foo: Hi you.\n" + 
				"Chat Id: chat-id-2\n" + 
				"Chat Time: 2012/02/02 11:10:10\n" + 
				"Chat Body Signature: 9811bbb3cf3697ccdf9fe75fa27ba18907106b506e9b0f6626c1e01a512d770c\n" + 
				"Messages signatures: [d6f1a17c288f10830f2d643d94f24bd7,d3bf04ced16daac99f720b5263695da4]\n" + 
				"Chat topic: (2 lines) CHAT TOPIC\n" + 
				"Poster: id=cloe; display=cloe Foo\n" + 
				"Poster: id=viboe; display=viboe Foo\n" + 
				"[2012/02/02 11:12:10] cloe Foo: I say hi\n" + 
				"[2012/02/02 11:15:10] viboe Foo: Hi you.";
		
		Assert.assertEquals(expected, visitedChats);
	}
	
	@Test
	public void testAcceptForRecentChats() throws SkypeException {
		SkypeChatVisitorMock visitor = new SkypeChatVisitorMock();
		chatFetchStrategy.enableCanUseRecentChats();
		ChatWrapperMock chat1 = createChat("chat-id-1", "joe", "moe", DateHelper.makeDate(2011, 1, 2, 11, 10, 10));
		ChatWrapperMock chat2 = createChat("chat-id-2", "cloe", "viboe", DateHelper.makeDate(2012, 1, 2, 11, 10, 10));
		skypeWrapper.setAllChats(chat1, chat2);
		skypeWrapper.setRecentChats(chat2);
		
		subject.accept(visitor);
		
		String visitedChats = visitor.getVisitedChatsAsString().trim();
		
		String expected = 
				"Chat Id: chat-id-2\n" + 
				"Chat Time: 2012/02/02 11:10:10\n" + 
				"Chat Body Signature: 9811bbb3cf3697ccdf9fe75fa27ba18907106b506e9b0f6626c1e01a512d770c\n" + 
				"Messages signatures: [d6f1a17c288f10830f2d643d94f24bd7,d3bf04ced16daac99f720b5263695da4]\n" + 
				"Chat topic: (2 lines) CHAT TOPIC\n" + 
				"Poster: id=cloe; display=cloe Foo\n" + 
				"Poster: id=viboe; display=viboe Foo\n" + 
				"[2012/02/02 11:12:10] cloe Foo: I say hi\n" + 
				"[2012/02/02 11:15:10] viboe Foo: Hi you.";
		
		Assert.assertEquals(expected, visitedChats);
	}

	private ChatWrapperMock createChat(String chatId, String username1, String username2, Date chatTime) throws SkypeException {
		ChatWrapperMock mockChat = new ChatWrapperMock(chatId,chatTime);
		
		UserWrapperMock user1 = createMockUser(username1);
		UserWrapperMock user2 = createMockUser(username2);
		mockChat.setMembers(user1, user2);
		
		ChatMessageWrapperMock msg1 = createMockMessage(chatTime, user1, 2, "I say hi");
		ChatMessageWrapperMock msg2 = createMockMessage(chatTime, user2, 5, "Hi you.");
		mockChat.setChatMessages(msg1, msg2);
		return mockChat;
	}

	private ChatMessageWrapperMock createMockMessage(Date chatTime,
			UserWrapperMock user1, int minutesToAdd, String content)
			throws SkypeException {
		return new ChatMessageWrapperMock(
				user1,
				content,
				createNewDateAddingMinutes(chatTime, minutesToAdd));
	}

	private UserWrapperMock createMockUser(String username1) {
		return new UserWrapperMock(username1, username1+" Foo");
	}

	private Date createNewDateAddingMinutes(Date chat1Time, int minutesToAdd) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(chat1Time);
		instance.add(Calendar.MINUTE,minutesToAdd);
		Date msg1Time = instance.getTime();
		return msg1Time;
	}
	
	@Before
	public void before() {
		LoggerProvider loggerProvider = new SimpleLoggerProvider();
		DigestProvider digestProvider = new DigestProvider();
		SkypeChatMessageDataFactory skypeChatMessageFactory = new SkypeChatMessageDataFactory(digestProvider);
		SkypeUserFactory skypeUserFactory = new SkypeUserFactoryMock();
		SkypeChatFactory chatFactory = new SkypeChatFactoryImpl(digestProvider, skypeChatMessageFactory, skypeUserFactory, loggerProvider);
		chatFetchStrategy = new ChatFetchStrategyChooserMock();
		subject = new SkypeApiImpl(chatFactory, loggerProvider, chatFetchStrategy, skypeWrapper);
	}
	
	private final class ChatFetchStrategyChooserMock implements
			ChatFetchStrategyChooser {
		private boolean canUseRecent = false;

		@Override
		public boolean catFetchJustTheRecentChats() {
			return canUseRecent;
		}

		public void enableCanUseRecentChats() {
			canUseRecent  = true;
		}
	}

	private final class SkypeChatVisitorMock implements SkypeApiChatVisitor {
		StringBuffer visitedChats = new StringBuffer();

		@Override
		public void visit(SkypeChat skypeChat) {
			visitedChats.append(skypeChat);
			visitedChats.append("\n");
		}

		public String getVisitedChatsAsString() {
			return visitedChats.toString();
		}
	}
}
