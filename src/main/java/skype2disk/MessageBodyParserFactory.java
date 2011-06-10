package skype2disk;

import com.google.inject.ImplementedBy;

import skype.commons.UsersSortedByUserId;

@ImplementedBy(MessageBodyParserFactoryImpl.class)
public interface MessageBodyParserFactory {

	public abstract MessageBodyParser produce(UsersSortedByUserId userList,
			String[] messageSignatures);

}