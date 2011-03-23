package testutils;

import skype.SkypeUserImpl;
import skype.UsersSortedByUserId;

public class SkypeChatHelper {

	public static UsersSortedByUserId makeUserList(String[] users) {
		final UsersSortedByUserId userIds = new UsersSortedByUserId();
		boolean isCurrentUser = true;
		for(String user : users) {
			userIds.add(new SkypeUserImpl(user, user.toUpperCase(), isCurrentUser));
			isCurrentUser = false;
		}
		return userIds;
	}
}
