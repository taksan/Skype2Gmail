package gmail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class CreateAMessageTest {
	@Test
	public void createOneMessageImap() throws MessagingException, IOException {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", "anhanga.tinhoso@gmail.com", "nukekubi");

		Folder root = store.getFolder("Skype-Test");
		if (!root.exists()) {
			root.create(Folder.HOLDS_MESSAGES);
		}
		else {
			root.open(Folder.READ_WRITE);
		}
		
		addMessage(session, root);

		// Folder inbox = store.getFolder("ar7e");
		// inbox.open(Folder.READ_ONLY);
		// Message messages[] = inbox.getMessages();
		// for(Message message:messages) {
		// System.out.println(message);
	}

	private void addMessage(Session session, Folder root)
			throws MessagingException, AddressException {
		Message oneMessage = new MimeMessage(session);
		oneMessage.setFrom(new InternetAddress("42_dont_panic@foobar.com"));
		oneMessage.addHeader("Date", "Tue, 1 Mar 2011 12:12:26 -0700");
		oneMessage.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("somebody@some_domain.net"));
		oneMessage.setSubject("Hi there");
		oneMessage.setText("This is of utter importance");
		oneMessage.setFlag(Flag.FLAGGED, true);

		Message[] msgs = new Message[] { oneMessage };
		root.appendMessages(msgs);
	}
}
