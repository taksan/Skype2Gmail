package skype;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import skype.mocks.SkypeChatMock;
import skype2disk.FileDumpContentBuilder;
import testutils.DigestProviderForTestFactory;
import testutils.SkypeChatHelper;
import testutils.SkypeChatTestHelper;
import utils.DigestProvider;

public class SkypeChatImplTest {
	@Test
	public void testBodyContentSignature() {
		final SkypeChatMock chat = SkypeChatHelper.createSkypeTestEnvironment();
		
		final DigestProvider digestProvider = DigestProviderForTestFactory.getInstance();
		final SkypeChatImpl skypeChatImpl = new SkypeChatImpl(
				digestProvider, 
				chat.getId(), 
				chat.getTime(), 
				chat.getTopic(), 
				chat.getMembersIds(), 
				chat.getChatMessages());
		
		final String chatContentId = skypeChatImpl.getBodySignature();
		final String expected="14#35ff9458ed508c794ad0c309e12902f452cdc25cd5991bc2422cd51f7c4272fd";
		Assert.assertEquals(expected, chatContentId);
		
		final Date lastModificationTime = skypeChatImpl.getLastModificationTime();
		final String actualTime = SkypeChatMessage.chatDateFormat.format(lastModificationTime);
		final String expectedTime = "2011/03/21 15:18:16";
		Assert.assertEquals(expectedTime , actualTime);
	}
	
	@Test
	public void testMerge() {
		SkypeChatTestHelper chatHelper = new SkypeChatTestHelper() {

			@Override
			public void addChatMessages() {
				addMessage("joe", "fellow", 21, 15, 15, 18);
				addMessage("moe", "Howdy\n	I'm doing fine", 21, 15, 24, 18);
				addMessage("joe", "A day has passed", 22, 15, 24, 18);
			}
		};
		SkypeChatImpl chatA = chatHelper.getChat("toUpdate", "FOO");
		SkypeChatImpl chatB = chatHelper.getChat("toUpdate", "FOO");
		
		FileDumpContentBuilder fileDumpContentBuilder1 = new FileDumpContentBuilder(chatA);
		
		System.out.println(fileDumpContentBuilder1.getContent());
		
		SkypeChat chatCshouldBeTheSameAsA = chatA.merge(chatB);
		
		Assert.assertEquals(chatA.getBodySignature(), chatCshouldBeTheSameAsA.getBodySignature());
		
		chatHelper.addMessage("moe", "Another day has passed", 23, 15, 24, 18);
		SkypeChat chatD = chatHelper.getChat("toUpdate", "FOO");
		
		SkypeChat mergeResult = chatA.merge(chatD);
		FileDumpContentBuilder fileDumpContentBuilder = new FileDumpContentBuilder(mergeResult);
		
		String expected = 
			"Chat Id: toUpdate\n" + 
			"Chat Time: 2011/04/21 15:00:00\n" + 
			"Chat Body Signature: 22#872b3345cf1dcc31db41bef3e4cb482cd613a516699b62f5bfdc0272e1ea892b\n" + 
			"Messages signatures: [765addc4c3fa9bf96af260131af55c3a,209cfb52f20f1310a2de704eb5ccc0ed,501fb6ccae7c8806d56daa1ee89ba949,8917de92790e1dde41f70f5eb9ff96f9]\n" + 
			"Chat topic: FOO\n" + 
			"Poster: id=joe; display=JOE\n" + 
			"Poster: id=moe; display=MOE\n" + 
			"[2011/04/21 15:15:18] JOE: fellow\n" + 
			"[2011/04/21 15:24:18] MOE: Howdy\n" + 
			"	I'm doing fine\n" + 
			"[2011/04/22 15:24:18] JOE: A day has passed\n" +
			"[2011/04/23 15:24:18] MOE: Another day has passed";
		
		Assert.assertEquals(expected, fileDumpContentBuilder.getContent());
	}
}
