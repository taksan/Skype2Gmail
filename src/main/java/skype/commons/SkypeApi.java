package skype.commons;

import com.google.inject.ImplementedBy;

@ImplementedBy(SkypeApiImpl.class)
public interface SkypeApi {
	boolean isRunning();
	
	SkypeUser getCurrentUser();
	
	void accept(SkypeApiChatVisitor visitor);
}
