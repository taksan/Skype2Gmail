package skype2gmail;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultSkypeChatFolderProvider.class)
public interface SkypeChatFolderProvider {

	String getFolderName();

}
