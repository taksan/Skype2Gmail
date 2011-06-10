package skype2gmail;

import com.google.inject.ImplementedBy;

import mail.SkypeMailFolder;

@ImplementedBy(NonIndexGmailFolderProviderImpl.class)
public interface NonIndexGmailFolderProvider {

	SkypeMailFolder get();
}
