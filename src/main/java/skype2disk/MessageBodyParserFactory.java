package skype2disk;

import skype.UsersSortedByUserId;

public interface MessageBodyParserFactory {

	public abstract MessageBodyParser produce(UsersSortedByUserId userList,
			String[] messageSignatures);

}