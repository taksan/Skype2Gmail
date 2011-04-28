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
		GmailStoreFolder rootFolderProvider  = injector.getInstance(GmailStoreFolder.class);
		SkypeChatDateFormat skypeChatDateFormat = injector.getInstance(SkypeChatDateFormat.class);
		
		SkypeChatBuilderHelper chatHelper = new SkypeChatBuilderHelper() {

			@Override
			public void addChatMessages() {
				addMessage("joe", "JOE", "fellow", 3, 21, 15, 15, 18);
				addMessage("moe", "Howdy\n	I'm doing fine", 3, 21, 15, 24, 18);
				addMessage("joe", "JOSE", "A day has passed", 3, 22, 15, 24, 18);
				addMessage("moe", "MOSE", "Dumb", 3, 22, 15, 24, 20);
				addMessage("boe", "BOE", "Yuck", 3, 22, 15, 24, 21);
				addMessage("boe", "BONE", "Numb", 3, 22, 15, 24, 22);
			}
		};
		
		SkypeChat chat = chatHelper.getChat("#joe$moe", "chit chat");
		injector.getInstance(GmailStorageEntryFactory.class);
		GmailStorageEntry gmailStorageEntry = new GmailStorageEntry(sessionProvider, rootFolderProvider, chat, skypeChatDateFormat);
		
		gmailStorageEntry.store(new SkypeChatSetter(chat));
		
		GmailMessage mimeMessage = gmailStorageEntry.getMessage();
		final InternetAddress[] from = (InternetAddress[]) mimeMessage.getFrom();
		Assert.assertEquals("moe", from[0].getAddress());
		
		final InternetAddress[] recipients = (InternetAddress[]) mimeMessage.getRecipients(RecipientType.TO);
		Assert.assertEquals(2, recipients.length);
		String recipientList = StringUtils.join(recipients, ",");
		Assert.assertEquals("boe,joe", recipientList);
		
		String[] posters = mimeMessage.getPosters();
		String posterText = StringUtils.join(posters,"\n");
		
		String expectedUsersInHeader = 
				"Poster: id=boe; display=BOE\n" + 
				"Poster: id=boe; display=BONE\n" + 
				"Poster: id=joe; display=JOE\n" + 
				"Poster: id=joe; display=JOSE\n" + 
				"Poster: id=moe; display=MOE\n" + 
				"Poster: id=moe; display=MOSE";
		Assert.assertEquals(expectedUsersInHeader, posterText);
		
		final String messageBody = "[2011/04/21 15:15:18] JOE: fellow\n" + 
				"[2011/04/21 15:24:18] MOE: Howdy\n" + 
				"	I'm doing fine\n" + 
				"[2011/04/22 15:24:18] JOSE: A day has passed\n" + 
				"[2011/04/22 15:24:20] MOSE: Dumb\n" + 
				"[2011/04/22 15:24:21] BOE: Yuck\n" + 
				"[2011/04/22 15:24:22] ... Numb";
		Assert.assertEquals(messageBody, mimeMessage.getBody());
		
		Assert.assertEquals("#joe$moe", mimeMessage.getChatId());
		
		Assert.assertEquals("e2b5a993cca294110435ea7849f5f4425e125abd13a31ba708dd085789f45831", 
				mimeMessage.getBodySignature());
		
		String[] messagesSignaturesArray = mimeMessage.getMessagesSignatures();
		String messagesSignatures = StringUtils.join(messagesSignaturesArray,",");
		Assert.assertEquals("765addc4c3fa9bf96af260131af55c3a,209cfb52f20f1310a2de704eb5ccc0ed,501fb6ccae7c8806d56daa1ee89ba949,c70b5f1fea51d9aa37bd61e76a71c300,f498d2faba0e6ab74ca6ca9d27c4815c,21e6ec97dbe2bf10561d8e84f9938be4", 
				messagesSignatures);
		
	}
}
