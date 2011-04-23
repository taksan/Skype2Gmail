package skype;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class EmptySkypeMessage implements InvocationHandler {
	
	public static SkypeChatMessage produce() {
		return (SkypeChatMessage) Proxy.newProxyInstance(
				SkypeChatMessage.class.getClassLoader(), 
				new Class<?>[]{ SkypeChatMessage.class}, 
				new EmptySkypeMessage());
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		throw new SkypeMessageParsingException("Empty message produced? Impossible");
	}

}
