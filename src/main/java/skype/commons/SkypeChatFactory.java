package skype.commons;

import com.google.inject.ImplementedBy;

import skypeapi.wrappers.ChatWrapper;

@ImplementedBy(SkypeChatFactoryImpl.class)
public interface SkypeChatFactory {
	SkypeChat produce(ChatWrapper chat);
	SkypeChat produceEmpty();
}
