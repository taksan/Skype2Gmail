package skype;

public interface SkypeUser {
	public String getUserId();
	public String getDisplayName();
	public boolean isCurrentUser();
	public String getMailAddress();
}
