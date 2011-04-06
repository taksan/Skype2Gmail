package skype;

import java.util.Comparator;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class UsersSortedByUserId extends TreeSet<SkypeUser> {
	public UsersSortedByUserId() {
		super(getUserIdComparator());
	}
	
	public SkypeUser findByDisplayName(String userDisplay)
	{
		for (SkypeUser chatUser : this) {
			if (chatUser.getDisplayName().equals(userDisplay)) {
				return chatUser;
			}
			if (chatUser.getUserId().equals(userDisplay)) {
				return chatUser;
			}
		}
		return SkypeUserNotFound.produce(userDisplay);
	}

	private static Comparator<SkypeUser> getUserIdComparator() {
		return new Comparator<SkypeUser>() {

			@Override
			public int compare(SkypeUser o1, SkypeUser o2) {
				return o1.getUserId().compareTo(o2.getUserId());
			}
		};
	}
}
