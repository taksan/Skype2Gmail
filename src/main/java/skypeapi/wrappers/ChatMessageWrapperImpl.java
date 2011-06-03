package skypeapi.wrappers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import skype.exceptions.MessageProcessingException;


import com.skype.ChatMessage;

public class ChatMessageWrapperImpl implements InvocationHandler {

	public static ChatMessageWrapper wrap(ChatMessage chatMessage) {
		return (ChatMessageWrapper) Proxy.newProxyInstance(
				ChatMessageWrapper.class.getClassLoader(),
				new Class<?>[] { ChatMessageWrapper.class },
				new ChatMessageWrapperImpl(chatMessage));
	}

	private ChatMessage delegateChatMessage;

	public ChatMessageWrapperImpl(ChatMessage chatMessage) {
		this.delegateChatMessage = chatMessage;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		try {
			Method actualMethod = delegateChatMessage.getClass().getMethod(method.getName(), new Class<?>[0]);
			return actualMethod.invoke(delegateChatMessage, args);
		}catch(InvocationTargetException e) {
			throw new MessageProcessingException(e.getCause());
		}
	}

}
