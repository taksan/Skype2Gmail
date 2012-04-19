package mail;

import static org.junit.Assert.assertEquals;

import javax.mail.Message.RecipientType;

import mail.skypemail.SkypeMailMessageFactoryImpl;

import org.junit.Test;

import skype.mocks.SkypeUserFactoryMock;
import skype2gmail.DefaultSkypeChatFolderProvider;
import skype2gmail.SessionProviderImpl;
import skype2gmail.mocks.SkypeMailStoreMock;
import utils.SimpleLoggerProvider;

public class SkypeMailMessageFactoryImplTest {
	@Test
	public void setFrom_ShouldNotBreakFromHeader() {
		SkypeMailStoreMock mockStore = new SkypeMailStoreMock();
		
		SessionProviderImpl sessionProvider = new SessionProviderImpl();
		DefaultSkypeChatFolderProvider cfp = new DefaultSkypeChatFolderProvider();
		SimpleLoggerProvider loggerProvider = new SimpleLoggerProvider();
		
		SkypeMailMessageFactoryImpl factory = 
			new SkypeMailMessageFactoryImpl(sessionProvider, cfp, mockStore, loggerProvider);
		
		SkypeMailMessage subject = factory.factory();
		subject.setFrom("ã");
		
		
		
		subject.addRecipient(new SkypeUserFactoryMock().produce("foo#jão", "jão"));
		String[] recipients = subject.getRecipients(RecipientType.TO);
		String actual = recipients[0];
		assertEquals("foo#jão", actual); 
	}
}
