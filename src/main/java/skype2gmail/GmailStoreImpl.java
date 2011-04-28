package skype2gmail;

import gmail.GmailFolder;
import gmail.GmailMessage;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import skype.ApplicationException;

import com.google.inject.Inject;

public class GmailStoreImpl implements GmailStore, GmailFolder {

	private final Session session;
	private final SkypeChatFolderProvider chatFolderProvider;
	private final UserAuthProvider userInfoProvider;
	private GmailFolder gmailFolder;
	private Store store;
	private final GmailFolderFactory gmailFolderFactory;

	@Inject
	public GmailStoreImpl(
			SessionProvider sessionProvider, 
			UserAuthProvider userInfoProvider, 
			SkypeChatFolderProvider chatFolderProvider,
			GmailFolderFactory gmailFolderFactory) 
	{
		this.userInfoProvider = userInfoProvider;
		this.chatFolderProvider = chatFolderProvider;
		this.gmailFolderFactory = gmailFolderFactory;
		this.session = sessionProvider.getInstance();
	}
	

	@Override
	public GmailMessage[] getMessages() {
		return getGmailFolder().getMessages();
	}

	@Override
	public void deleteMessageBasedOnId(String chatId) {
		getGmailFolder().deleteMessageBasedOnId(chatId);
	}

	@Override
	public void appendMessage(GmailMessage gmailMessage) {
		getGmailFolder().appendMessage(gmailMessage);
	}

	@Override
	public void close() {
		if (store != null) {
			try {
				getGmailFolder().close();
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

	private GmailFolder getGmailFolder() {
		if (gmailFolder != null)
			return gmailFolder;
		
		String user = userInfoProvider.getUser();
		String password = userInfoProvider.getPassword();
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", user, password);
			
			Folder root = store.getFolder(chatFolderProvider.getFolder());
			if (!root.exists()) {
				root.create(Folder.HOLDS_MESSAGES);
			}
			else {
				root.open(Folder.READ_WRITE);
			}
			
			gmailFolder = this.gmailFolderFactory.produce(root);
			return gmailFolder;
		} catch (NoSuchProviderException e) {
			throw new ApplicationException(e);
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}
}
