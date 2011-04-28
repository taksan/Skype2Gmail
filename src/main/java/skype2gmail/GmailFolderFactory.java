package skype2gmail;

import gmail.GmailFolder;

import javax.mail.Folder;

import com.google.inject.ImplementedBy;

@ImplementedBy(GmailFolderFactoryImpl.class)
public interface GmailFolderFactory {

	GmailFolder produce(Folder root);

}
