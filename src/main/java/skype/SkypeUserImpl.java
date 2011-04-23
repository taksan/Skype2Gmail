package skype;



public class SkypeUserImpl implements SkypeUser {

	private final String userId;
	private final String displayName;
	private final boolean isCurrentUser;

	public SkypeUserImpl(String id, String displayName, boolean isCurrentUser) {
		this.userId = id;
		this.isCurrentUser = isCurrentUser;
		if (displayName == null || displayName.trim().equals(""))
			this.displayName = id;
		else
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
	
	@Override
	public int hashCode() {
		return this.getUserId().hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof SkypeUser)) {
			return false;
		}
		SkypeUser otherUser = (SkypeUser) other;
		return this.getUserId().equals(otherUser.getUserId());
	}

	public String toString() {
		return String.format("id=%s; displayName=%s", this.userId, this.displayName);
	}

	@Override
	public boolean isCurrentUser() {
		return this.isCurrentUser;
	}

	@Override
	public String getMailAddress() {
		return this.getUserId();
	}

	@Override
	public String getPosterHeader() {
		return String.format(
				"Poster: id=%s; display=%s", 
				this.getUserId(),
				this.getDisplayName()
			);
	}
}
