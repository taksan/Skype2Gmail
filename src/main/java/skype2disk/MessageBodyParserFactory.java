package skype2disk;

import skype.commons.UsersSortedByUserId;

public interface MessageBodyParserFactory {

	public abstract MessageBodyParser produce(UsersSortedByUserId userList,
			String[] messageSignatures);

}