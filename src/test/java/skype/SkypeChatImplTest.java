package skype;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import testutils.DigestProviderForTestFactory;
import testutils.SkypeChatBuilderHelper;
import utils.DigestProvider;

public class SkypeChatImplTest {
	
	@Test
	public void testBodyContentSignature() {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {
		
			@Override
			public void addChatMessages() {
				addMessage("goofoo", "what's up", 3, 21, 15, 14, 18);
				addMessage("camaron.goo", "Howdy\n	I'm doing fine", 3, 21, 15, 18, 16);
				addMessage("goofoo", "A day has passed", 3, 22, 15, 14, 24);
			}
		};
		
		final String chatId = "#camaron.goo/$goofoo;81ef2618fc9a6343";
		final SkypeChat chat = chatHelper.getChat(chatId, "FOO");
		
		final DigestProvider digestProvider = DigestProviderForTestFactory.getInstance();
		final SkypeChatImpl skypeChatImpl = new SkypeChatImpl(
				digestProvider, 
				chat.getId(), 
				chat.getTime(), 
				chat.getTopic(), 
				chat.getPosters(), 
				chat.getChatMessages());
		
		final String chatContentId = skypeChatImpl.getBodySignature();
		final String expected="15e36e4391c61703331c2136e5feda6a8ce794b0c67286eaea184cd2100f9a23";
		Assert.assertEquals(expected, chatContentId);
		
		final Date lastModificationTime = skypeChatImpl.getLastModificationTime();
		final String actualTime = SkypeChatMessage.chatDateFormat.format(lastModificationTime);
		final String expectedTime = "2011/04/22 15:14:24";
		Assert.assertEquals(expectedTime , actualTime);
		
		String chatAuthor = skypeChatImpl.getChatAuthor().getUserId();
		Assert.assertEquals("camaron.goo", chatAuthor);
	}
	
	@Test
	public void testMergeWithEmpty() {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {

			@Override
			public void addChatMessages() {
				addMessage("joe", "fellow", 3, 21, 15, 15, 18);
				addMessage("moe", "Howdy\n	I'm doing fine", 3, 21, 15, 24, 18);
				addMessage("joe", "A day has passed", 3, 22, 15, 24, 18);
				addMessage("moe", "booo2", 3, 22, 15, 25, 2);
				addMessage("joe", "gap", 3, 22, 15, 25, 5);
			}
		};
		
		SkypeChat chatA = chatHelper.getChat("toUpdate", "FOO");
		SkypeChat chatB = new EmptySkypeChat(null);
		
		SkypeChat chatCshouldBeTheSameAsA = chatA.merge(chatB);
		
		Assert.assertEquals(chatA.getBodySignature(), chatCshouldBeTheSameAsA.getBodySignature());
	}
	
	@Test
	public void testEmptyChatList() {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {

			@Override
			public void addChatMessages() {
			}
		};
		SkypeChatImpl emptyChat = chatHelper.getChat("empty", "FOO");
		emptyChat.getLastModificationTime();
	}
	
	@Test
	public void testGetAuthor() {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {
			
			@Override
			public void addChatMessages() {
				addMessage("me", "myself", "what's up", 3, 21, 15, 14, 18);
				addMessage("him", "himself", "Howdy\n	I'm doing fine", 3, 21, 15, 18, 16);
			}
			
			protected UsersSortedByUserId addPosters() {
				final UsersSortedByUserId userIds = new UsersSortedByUserId();
				userIds.add(new SkypeUserImpl("me", "myself", true));
				userIds.add(new SkypeUserImpl("him", "himself", false));
				
				return userIds;
			}
		};
		
		final SkypeChatImpl skypeChatImpl = makeSubject(chatHelper);
		Assert.assertEquals("him", skypeChatImpl.getChatAuthor().getUserId());
	}
	
	@Test
	public void testGetAuthorWithSingleMessage() {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {
			
			@Override
			public void addChatMessages() {
				addMessage("me", "myself", "what's up", 3, 21, 15, 14, 18);
			}
			
			protected UsersSortedByUserId addPosters() {
				final UsersSortedByUserId userIds = new UsersSortedByUserId();
				userIds.add(new SkypeUserImpl("me", "myself", true));
				
				return userIds;
			}
		};
		
		final SkypeChatImpl skypeChatImpl = makeSubject(chatHelper);
		Assert.assertEquals("me", skypeChatImpl.getChatAuthor().getUserId());
	}
	
	@Test
	public void testGetAuthorWithThree() {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {
			
			@Override
			public void addChatMessages() {
				addMessage("him", "himself", "Howdy\n	I'm doing fine", 3, 21, 15, 18, 16);
				addMessage("her", "herself", "Howdy\n	I'm doing fine", 3, 21, 15, 18, 16);
				addMessage("me", "myself", "what's up", 3, 21, 15, 14, 18);
			}
			
			protected UsersSortedByUserId addPosters() {
				final UsersSortedByUserId userIds = new UsersSortedByUserId();
				userIds.add(new SkypeUserImpl("her", "herself", false));
				userIds.add(new SkypeUserImpl("me", "myself", true));
				userIds.add(new SkypeUserImpl("him", "himself", false));
				
				return userIds;
			}
		};
		
		final SkypeChatImpl skypeChatImpl = makeSubject(chatHelper);
		Assert.assertEquals("him", skypeChatImpl.getChatAuthor().getUserId());
	}

	private SkypeChatImpl makeSubject(SkypeChatBuilderHelper chatHelper) {
		final String chatId = "#me/him";
		final SkypeChat chat = chatHelper.getChat(chatId, "FOO");
		
		final DigestProvider digestProvider = DigestProviderForTestFactory.getInstance();
		final SkypeChatImpl skypeChatImpl = new SkypeChatImpl(
				digestProvider, 
				chat.getId(), 
				chat.getTime(), 
				chat.getTopic(), 
				chat.getPosters(), 
				chat.getChatMessages());
		return skypeChatImpl;
	}
}
