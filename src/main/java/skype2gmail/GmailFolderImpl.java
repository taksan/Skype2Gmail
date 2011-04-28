package skype2gmail;

import gmail.GmailMessage;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.log4j.Logger;

import skype.ApplicationException;
import utils.LoggerProvider;
import utils.SimpleLoggerProvider;

import com.google.inject.Inject;
import com.sun.mail.imap.IMAPMessage;

public class GmailFolderImpl implements GmailFolder {

	private final Session session;
	private final UserAuthProvider userInfoProvider;
	private Store store;
	
	private final SkypeChatFolderProvider chatFolderProvider;
	private Folder skypeChatFolder;
	private Logger logger;
	private GmailMessage[] retrievedMessages;
	private final Map<String, GmailMessage> gmailMessages = new LinkedHashMap<String, GmailMessage>();
	
	@Inject
	public GmailFolderImpl(
			SessionProvider sessionProvider, 
			UserAuthProvider userInfoProvider, 
			SkypeChatFolderProvider chatFolderProvider,
			LoggerProvider loggerProvider) 
	{
		this.userInfoProvider = userInfoProvider;
		this.chatFolderProvider = chatFolderProvider;
		this.session = sessionProvider.getInstance();
		this.logger = loggerProvider.getLogger(getClass());
	}
	

	@Override
	public GmailMessage[] getMessages() {
		
		try {
			if (retrievedMessages != null) {
				return retrievedMessages;
			}
			Folder folder = getSkypeChatFolder();
			
			logger.info("Retrieving mail chat messages to merge with new messages");			
			logger.info("Messages to retrieve: " + folder.getMessageCount());
			for (Message message : folder.getMessages()) {
				GmailMessage  gmailMessage = new GmailMessage((IMAPMessage)message);
				gmailMessages.put(gmailMessage.getChatId(), gmailMessage);
			}
			logger.info("Messages retrieved");
			retrievedMessages = gmailMessages.values().toArray(new GmailMessage[0]);
			return retrievedMessages;
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void deleteMessageBasedOnId(String chatId) {
		GmailMessage gmailMessage = gmailMessages.get(chatId);
		if (gmailMessage == null)
			return;
		
		gmailMessage.delete();
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
		Folder rootFolder = getSkypeChatFolder();
		Message[] msgs = new javax.mail.Message[] { gmailMessage.getMimeMessage() };
		try {
			rootFolder.appendMessages(msgs);
			replaceOldMessage(gmailMessage);
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void close() {
		if (store != null) {
			try {
				try {
					if (skypeChatFolder != null) {
						boolean deleteFlaggedMessages = true;
						skypeChatFolder.close(deleteFlaggedMessages);
					}
				} catch (MessagingException e) {
					throw new ApplicationException(e);
				}
			}
			finally {
				closeStore();
			}
		}
	}

	private void closeStore() {
		try {
			store.close();
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}

	private Folder getSkypeChatFolder() {
		if (skypeChatFolder != null)
			return skypeChatFolder;
		
		String user = userInfoProvider.getUser();
		String password = userInfoProvider.getPassword();
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", user, password);
			
			skypeChatFolder = store.getFolder(chatFolderProvider.getFolder());
			if (!skypeChatFolder.exists()) {
				skypeChatFolder.create(Folder.HOLDS_MESSAGES);
			}
			else {
				skypeChatFolder.open(Folder.READ_WRITE);
			}
			return skypeChatFolder;
		} catch (NoSuchProviderException e) {
			throw new ApplicationException(e);
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}
	
	private void replaceOldMessage(GmailMessage gmailMessage) {
		gmailMessages.put(gmailMessage.getChatId(), gmailMessage);
		retrievedMessages = gmailMessages.values().toArray(new GmailMessage[0]);
	}
	

	public static void main(String[] args) {
		SessionProvider sessionProvider = new SessionProviderImpl();
		UserAuthProvider userInfoProvider = new UserAuthProvider() {
			
			@Override
			public String getUser() {
				return "anhanga.tinhoso@gmail.com";
			}
			
			@Override
			public String getPassword() {
				return "nukekubi";
			}
		};
		SkypeChatFolderProvider achatFolderProvider = new DefaultSkypeChatFolderProvider();
		
		GmailFolderImpl gmailStoreImpl = new GmailFolderImpl(sessionProvider, userInfoProvider, achatFolderProvider, new SimpleLoggerProvider());
		gmailStoreImpl.getMessages();
	}

}
