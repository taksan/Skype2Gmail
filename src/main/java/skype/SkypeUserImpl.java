package skype;


public class SkypeUserImpl implements SkypeUser {

	private final String userId;
	private final String displayName;

	public SkypeUserImpl(String id, String displayName) {
		this.userId = id;
		this.displayName = displayName;
		
	}

	@Override
	public String getUserId() {
		return this.userId;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	public String toString() {
		return String.format("id=%s; displayName=%s", this.userId, this.displayName);
	}
}
