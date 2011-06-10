package skype.commons;

import com.google.inject.ImplementedBy;

@ImplementedBy(ChatFetchStrategyChooserImpl.class)
public interface ChatFetchStrategyChooser {

	boolean catFetchJustTheRecentChats();

}
