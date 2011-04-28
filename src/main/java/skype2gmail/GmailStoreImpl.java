package skype2gmail;

import gmail.GmailFolder;
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

public class GmailStoreImpl implements GmailStore, GmailFolder {

	private final Session session;
	private final SkypeChatFolderProvider chatFolderProvider;
	private final UserAuthProvider userInfoProvider;
	private Store store;
	private Folder root;
	private Logger logger;
	private GmailMessage[] retrievedMessages;
	private final Map<String, GmailMessage> gmailMessages = new LinkedHashMap<String, GmailMessage>();
	
	@Inject
	public GmailStoreImpl(
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
			Folder rootFolder = getRootFolder();
			
			logger.info("Retrieving mail chat messages to merge with new messages");			
			logger.info("Messages to retrieve: " + rootFolder.getMessageCount());
			for (Message message : rootFolder.getMessages()) {
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
		Folder rootFolder = getRootFolder();
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
					if (root != null) {
						boolean deleteFlaggedMessages = true;
						root.close(deleteFlaggedMessages);
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

	private Folder getRootFolder() {
		if (root != null)
			return root;
		
		String user = userInfoProvider.getUser();
		String password = userInfoProvider.getPassword();
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", user, password);
			
			root = store.getFolder(chatFolderProvider.getFolder());
			if (!root.exists()) {
				root.create(Folder.HOLDS_MESSAGES);
			}
			else {
				root.open(Folder.READ_WRITE);
			}
			return root;
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
		
		GmailStoreImpl gmailStoreImpl = new GmailStoreImpl(sessionProvider, userInfoProvider, achatFolderProvider, new SimpleLoggerProvider());
		gmailStoreImpl.getMessages();
	}

}
