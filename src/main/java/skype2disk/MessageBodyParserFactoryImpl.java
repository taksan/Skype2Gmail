package skype2disk;

import skype.commons.SkypeChatMessageDataFactory;
import skype.commons.SkypeMessageDateFormat;
import skype.commons.UsersSortedByUserId;

import com.google.inject.Inject;

public class MessageBodyParserFactoryImpl implements MessageBodyParserFactory {

	private final SkypeChatMessageDataFactory skypeChatMessageDataFactory;
	private final SkypeMessageDateFormat skypeMessageDateFormat;
	
	@Inject
	public MessageBodyParserFactoryImpl(SkypeChatMessageDataFactory skypeChatMessageDataFactory,SkypeMessageDateFormat skypeMessageDateFormat)
	{
		this.skypeChatMessageDataFactory = skypeChatMessageDataFactory;
		this.skypeMessageDateFormat = skypeMessageDateFormat;
		
	}

	/* (non-Javadoc)
	 * @see skype2disk.MessageBodyParserFactory#produce(skype.UsersSortedByUserId, java.lang.String[])
	 */
	@Override
	public MessageBodyParser produce(UsersSortedByUserId userList, String[] messageSignatures) {
		return new MessageBodyParser(skypeChatMessageDataFactory, skypeMessageDateFormat, userList, messageSignatures);
	}

}
