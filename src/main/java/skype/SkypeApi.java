package skype;

public interface SkypeApi {
	boolean isRunning();
	
	SkypeUser getCurrentUser();
	
	void accept(SkypeApiChatVisitor visitor);
}
