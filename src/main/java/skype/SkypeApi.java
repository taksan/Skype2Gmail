package skype;

public interface SkypeApi {
	boolean isRunning();
	
	void accept(SkypeApiChatVisitor visitor);
}
