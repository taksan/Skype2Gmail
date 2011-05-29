package skype.commons;

import static testutils.MessageProducerUtil.createMessage;

import org.junit.Assert;
import org.junit.Test;

import skype.commons.SkypeChat;
import skype.commons.SkypeChatImpl;
import skype.commons.SkypeChatMessage;
import skype.commons.TimeSortedMessages;
import testutils.SkypeChatBuilderHelper;

public class TimeSortedMessagesTest {
	@Test
	public void testFindMessageWithSameSignature() {
		TimeSortedMessages tsm1 = new TimeSortedMessages();
		tsm1.add(createMessage("joe", "m5", "22/03 15:24:20"));
		tsm1.add(createMessage("moe", "fellow", "22/03 15:24:22"));
		tsm1.add(createMessage("moe", "m3", "23/03 15:24:22"));
		SkypeChatMessage expectedMessage = createMessage("joe", "m7", "23/03 15:30:22");
		tsm1.add(expectedMessage);
		tsm1.add(createMessage("hir", "m9", "23/03 15:35:22"));
		
		SkypeChatMessage ref = createMessage("joe", "m7", "24/03 15:30:22");
		
		SkypeChatMessage foundMessage = tsm1.findFirstMessageWithSameSignature(ref);
		Assert.assertEquals(expectedMessage.toString(), foundMessage.toString());
		
		SkypeChatMessage ref2 = createMessage("joe", "not on the list", "24/03 15:30:22");
		SkypeChatMessage shouldBeNull = tsm1.findFirstMessageWithSameSignature(ref2);
		Assert.assertNull(shouldBeNull);
	}
	
	@Test
	public void testMergeListWithoutCommonMessages() {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {
			@Override
			public void addChatMessages() {
				addMessage("joe", "m1", "21/03 15:15:18");
				addMessage("moe", "m2", "21/03 15:24:18");
				addMessage("joe", "m3", "22/03 15:24:18");
			}
		};
		
		SkypeChat chatA = chatHelper.getChat("toUpdate", "FOO");
		SkypeChatBuilderHelper chatFromOtherSource = new SkypeChatBuilderHelper() {
			@Override
			public void addChatMessages() {
				addMessage("cloe", "m1.5", "21/03 15:15:19");
				addMessage("joe", "m5", "22/03 15:24:20");
				addMessage("moe", "m6", "22/03 15:24:22");
			}
		};
		
		SkypeChat chatB = chatFromOtherSource.getChat("toUpdate", "FOO");
		
		String expected = 
			"[2011/03/21 15:15:18] JOE: m1\n" + 
			"[2011/03/21 15:15:19] CLOE: m1.5\n" + 
			"[2011/03/21 15:24:18] MOE: m2\n" + 
			"[2011/03/22 15:24:18] JOE: m3\n" + 
			"[2011/03/22 15:24:20] JOE: m5\n" + 
			"[2011/03/22 15:24:22] MOE: m6"; 
		
		assertMergeEnsuringCommutativeProperty(chatA.getChatMessages(), chatB.getChatMessages(), expected);
	}
	
	@Test
	public void testMergeIdenticalLists() {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {

			@Override
			public void addChatMessages() {
				addMessage("joe", "fellow", "21/03 15:15:18");
				addMessage("moe", "Howdy\n	I'm doing fine", "21/03 15:24:18");
				addMessage("joe", "A day has passed", "22/03 15:24:18");
				addMessage("moe", "booo2", "22/03 15:25:02");
				addMessage("joe", "gap", "22/03 15:25:05");
			}
		};
		
		
		SkypeChatImpl chatA = chatHelper.getChat("toUpdate", "FOO");
		SkypeChatImpl chatB = chatHelper.getChat("toUpdate", "FOO");
		
		String expectedMessages = chatA.getChatMessages().toString();
		
		String mergedMessages = mergeChatsAndGetBodyAsString(chatA.getChatMessages(), chatB.getChatMessages());
		Assert.assertEquals(expectedMessages, mergedMessages);
	}
	
	@Test
	public void testMergeWithSingleEquivalentMessage() {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {
			@Override
			public void addChatMessages() {
				addMessage("joe", "m1", "21/03 15:15:18");
				addMessage("joe", "m2", "21/03 15:17:18");
				addMessage("foo", "C",  "21/03 15:24:18");
				addMessage("joe", "m6", "22/03 15:24:18");
			}
		};
		
		SkypeChat chatA = chatHelper.getChat("toUpdate", "FOO");
		SkypeChatBuilderHelper chatFromOtherSource = new SkypeChatBuilderHelper() {
			@Override
			public void addChatMessages() {
				addMessage("moe", "m5", "22/03 15:16:20");
				addMessage("foo", "C",  "22/03 15:24:18");
				addMessage("moe", "m7", "22/03 15:30:22");
			}
		};
		
		SkypeChat chatB = chatFromOtherSource.getChat("toUpdate", "FOO");
		
		String expected = 
			"[2011/03/21 15:15:18] JOE: m1\n" + 
			"[2011/03/21 15:16:20] MOE: m5\n" + 
			"[2011/03/21 15:17:18] JOE: m2\n" + 
			"[2011/03/21 15:24:18] FOO: C\n" + 
			"[2011/03/22 15:24:18] JOE: m6\n" + 
			"[2011/03/22 15:30:22] MOE: m7"; 
		
		assertMergeEnsuringCommutativeProperty(chatA.getChatMessages(), chatB.getChatMessages(), expected);
	}
	
	@Test
	public void testMergeWithTwoEquivalentMessages() {
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {
			@Override
			public void addChatMessages() {
				addMessage("joe", "m1", "21/03 15:15:18");
				addMessage("joe", "m2", "21/03 15:17:18");
				addMessage("foo", "C",  "21/03 15:24:18");
				addMessage("joe", "m6", "22/03 15:24:18");
				addMessage("foo", "D", "22/03 16:24:18");
				addMessage("moe", "m9", "22/03 16:25:20");
			}
		};
		
		SkypeChat chatA = chatHelper.getChat("toUpdate", "FOO");
		SkypeChatBuilderHelper chatFromOtherSource = new SkypeChatBuilderHelper() {
			@Override
			public void addChatMessages() {
				addMessage("moe", "m5", "22/03 15:16:20");
				addMessage("foo", "C",  "22/03 15:24:18");
				addMessage("moe", "m7", "22/03 15:30:22");
				addMessage("foo", "D", "23/03 16:24:18");
				addMessage("moe", "m10", "23/03 16:25:20");
			}
		};
		
		SkypeChat chatB = chatFromOtherSource.getChat("toUpdate", "FOO");
		
		String expected = 
			"[2011/03/21 15:15:18] JOE: m1\n" + 
			"[2011/03/21 15:16:20] MOE: m5\n" + 
			"[2011/03/21 15:17:18] JOE: m2\n" + 
			"[2011/03/21 15:24:18] FOO: C\n" + 
			"[2011/03/21 15:30:22] MOE: m7\n" + 
			"[2011/03/22 15:24:18] JOE: m6\n" + 
			"[2011/03/22 16:24:18] FOO: D\n" + 
			"[2011/03/22 16:25:20] MOE: m9\n" + 
			"[2011/03/23 16:25:20] MOE: m10"; 
		
		assertMergeEnsuringCommutativeProperty(chatA.getChatMessages(), chatB.getChatMessages(), expected);
	}

	private void assertMergeEnsuringCommutativeProperty(
			TimeSortedMessages firstChatMessages,
			TimeSortedMessages secondChatMessages, String expected) {
		String mergeOfAwithB = mergeChatsAndGetBodyAsString(firstChatMessages, secondChatMessages);
		Assert.assertEquals(expected, mergeOfAwithB);
		String mergeOfBwithA = mergeChatsAndGetBodyAsString(secondChatMessages, firstChatMessages);
		Assert.assertEquals(expected, mergeOfBwithA);
	}

	private String mergeChatsAndGetBodyAsString(
			TimeSortedMessages firstChatMessages, 
			TimeSortedMessages secondChatMessages) {
		TimeSortedMessages mergedMessages = new TimeSortedMessages();
		mergedMessages.addMessageListsMergingThem(firstChatMessages, secondChatMessages);
		return mergedMessages.toString();
	}
}
