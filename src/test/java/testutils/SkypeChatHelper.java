package testutils;

import skype.commons.SkypeUserImpl;
import skype.commons.UsersSortedByUserId;

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
