package skype2gmail;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

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
		RootFolderProvider rootFolderProvider  = injector.getInstance(RootFolderProvider.class);
		SkypeChatDateFormat skypeChatDateFormat = injector.getInstance(SkypeChatDateFormat.class);
		
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {

			@Override
			public void addChatMessages() {
				addMessage("joe", "fellow", 21, 15, 15, 18);
				addMessage("moe", "Howdy\n	I'm doing fine", 21, 15, 24, 18);
				addMessage("joe", "A day has passed", 22, 15, 24, 18);
			}
		};
		
		SkypeChat chat = chatHelper.getChat("#joe$moe", "chit chat");
		injector.getInstance(GmailStorageEntryFactory.class);
		GmailStorageEntry gmailStorageEntry = new GmailStorageEntry(sessionProvider, rootFolderProvider, chat, skypeChatDateFormat);
		
		gmailStorageEntry.store(new SkypeChatSetter(chat));
		
		MimeMessage mimeMessage = gmailStorageEntry.getMessage().toMimeMessage();
		final InternetAddress[] from = (InternetAddress[]) mimeMessage.getFrom();
		Assert.assertEquals("joe", from[0].getAddress());
		
		final InternetAddress[] recipients = (InternetAddress[]) mimeMessage.getRecipients(RecipientType.TO);
		Assert.assertEquals("moe", recipients[0].getAddress());
		final String messageBody = "[2011/04/21 15:15:18] JOE: fellow\n" + 
				"[2011/04/21 15:24:18] MOE: Howdy\n" + 
				"	I'm doing fine\n" + 
				"[2011/04/22 15:24:18] JOE: A day has passed\n";
		Assert.assertEquals(messageBody, mimeMessage.getContent());
		
		Assert.assertEquals("#joe$moe", getHeaderAsString(mimeMessage, GmailStorage.X_MESSAGE_ID));
		
		Assert.assertEquals("16#ba424f9030bf99e6251494525982b82d9afb06081952197c256d9f9bac2dd0ef", 
				getHeaderAsString(mimeMessage, GmailStorage.X_BODY_SIGNATURE));
		
		Assert.assertEquals("765addc4c3fa9bf96af260131af55c3a,209cfb52f20f1310a2de704eb5ccc0ed,501fb6ccae7c8806d56daa1ee89ba949", 
				getHeaderAsString(mimeMessage, GmailStorage.X_MESSAGES_SIGNATURES));
	}

	private String getHeaderAsString(MimeMessage mimeMessage, String xMessageId)
			throws MessagingException {
		final String[] header = mimeMessage.getHeader(xMessageId);
		String actual = header[0];
		return actual;
	}
}
