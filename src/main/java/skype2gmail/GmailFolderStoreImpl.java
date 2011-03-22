package skype2gmail;

import gmail.GmailFolder;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import com.google.inject.Inject;

public class GmailFolderStoreImpl implements GmailFolderStore {

	private final Session session;
	private final SkypeChatFolderProvider chatFolderProvider;
	private final UserAuthProvider userInfoProvider;
	private GmailFolder gmailFolder;
	private Store store;

	@Inject
	public GmailFolderStoreImpl(
			SessionProvider sessionProvider, 
			UserAuthProvider userInfoProvider, 
			SkypeChatFolderProvider chatFolderProvider) 
	{
		this.userInfoProvider = userInfoProvider;
		this.chatFolderProvider = chatFolderProvider;
		this.session = sessionProvider.getInstance();
	}

	@Override
	public GmailFolder getFolder() {
		if (gmailFolder != null)
			return gmailFolder;
		
		gmailFolder = getGmailFolder();
		return gmailFolder;
	}

	private GmailFolder getGmailFolder() {
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
			
			return new GmailFolderImpl(root);
		} catch (NoSuchProviderException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		if (store != null) {
			try {
				store.close();
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
			getFolder().close();
		}
	}
}
