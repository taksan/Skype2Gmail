package skypeapi.wrappers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.LinkedList;

import skype.commons.UserWrapper;
import skype.exceptions.ApplicationException;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

public class ChatWrapperImpl implements InvocationHandler, ChatWrapper {
	public static ChatWrapper wrap(Chat chat) {
		return (ChatWrapper) Proxy.newProxyInstance(
				ChatWrapper.class.getClassLoader(),
				new Class<?>[]{ChatWrapper.class},
				new ChatWrapperImpl(chat));
	}

	private final Chat decoratedChat;

	public ChatWrapperImpl(Chat aChat) {
		this.decoratedChat = aChat;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		return method.invoke(decoratedChat, args);
	}

	public ChatMessageWrapper[] getAllChatMessages() {
		try {
			ChatMessage[] allChatMessages = this.decoratedChat.getAllChatMessages();
			return createChatMessageWrapperArrayFromChatMessages(allChatMessages);
		} catch (SkypeException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public Date getTime() {
		try {
			return decoratedChat.getTime();
		} catch (SkypeException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public String getWindowTitle() {
		try {
			return decoratedChat.getWindowTitle();
		} catch (SkypeException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public UserWrapper[] getAllMembers() {
		try {
			return wrapChatUsers(decoratedChat.getAllMembers());
		} catch (SkypeException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public String getId() {
		return decoratedChat.getId();
	}
	
	private UserWrapper[] wrapChatUsers(User[] allMembers) {
		LinkedList<UserWrapper> result = new LinkedList<UserWrapper>();
		for (User user : allMembers) {
			result.add(UserWrapperImpl.wrap(user));
		}
		return result.toArray(new UserWrapper[0]);
	}

	private ChatMessageWrapper[] createChatMessageWrapperArrayFromChatMessages(
			ChatMessage[] allChatMessages) {
		LinkedList<ChatMessageWrapper> result = new LinkedList<ChatMessageWrapper>();
		for (ChatMessage chatMessage : allChatMessages) {
			result.add(ChatMessageWrapperImpl.wrap(chatMessage));
		}
		return result.toArray(new ChatMessageWrapper[0]);
	}	
}
