package skype2gmail;

import gmail.GmailMessage;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import skype.SkypeChat;
import skype.SkypeChatDateFormat;
import skype.SkypeChatSetter;
import skype.mocks.Skype2GmailModuleMockingSkypeApi;
import testutils.SkypeChatBuilderHelper;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GmailStorageEntryTest {

	@Test
	public void happyDay() throws MessagingException, IOException {
		final Injector injector = Guice.createInjector(new Skype2GmailModuleMockingSkypeApi());
		
		SessionProvider sessionProvider = injector.getInstance(SessionProvider.class);
		GmailFolderStore rootFolderProvider  = injector.getInstance(GmailFolderStore.class);
		SkypeChatDateFormat skypeChatDateFormat = injector.getInstance(SkypeChatDateFormat.class);
		
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {

			@Override
			public void addChatMessages() {
				addMessage("joe", "fellow", 3, 21, 15, 15, 18);
				addMessage("moe", "Howdy\n	I'm doing fine", 3, 21, 15, 24, 18);
				addMessage("joe", "A day has passed", 3, 22, 15, 24, 18);
			}
		};
		
		SkypeChat chat = chatHelper.getChat("#joe$moe", "chit chat");
		injector.getInstance(GmailStorageEntryFactory.class);
		GmailStorageEntry gmailStorageEntry = new GmailStorageEntry(sessionProvider, rootFolderProvider, chat, skypeChatDateFormat);
		
		gmailStorageEntry.store(new SkypeChatSetter(chat));
		
		GmailMessage mimeMessage = gmailStorageEntry.getMessage();
		final InternetAddress[] from = (InternetAddress[]) mimeMessage.getFrom();
		Assert.assertEquals("joe", from[0].getAddress());
		
		final InternetAddress[] recipients = (InternetAddress[]) mimeMessage.getRecipients(RecipientType.TO);
		Assert.assertEquals("moe", recipients[0].getAddress());
		
		String[] posters = mimeMessage.getPosters();
		String posterText = StringUtils.join(posters,"\n");
		
		String expectedUsersInHeader = 
				"Poster: id=joe; display=JOE\n" + 
				"Poster: id=moe; display=MOE";
		Assert.assertEquals(expectedUsersInHeader, posterText);
		
		final String messageBody = "[2011/04/21 15:15:18] JOE: fellow\n" + 
				"[2011/04/21 15:24:18] MOE: Howdy\n" + 
				"	I'm doing fine\n" + 
				"[2011/04/22 15:24:18] JOE: A day has passed";
		Assert.assertEquals(messageBody, mimeMessage.getBody());
		
		Assert.assertEquals("#joe$moe", mimeMessage.getChatId());
		
		Assert.assertEquals("16#ba424f9030bf99e6251494525982b82d9afb06081952197c256d9f9bac2dd0ef", 
				mimeMessage.getBodySignature());
		
		String[] messagesSignaturesArray = mimeMessage.getMessagesSignatures();
		String messagesSignatures = StringUtils.join(messagesSignaturesArray,",");
		Assert.assertEquals("765addc4c3fa9bf96af260131af55c3a,209cfb52f20f1310a2de704eb5ccc0ed,501fb6ccae7c8806d56daa1ee89ba949", 
				messagesSignatures);
	}
}
