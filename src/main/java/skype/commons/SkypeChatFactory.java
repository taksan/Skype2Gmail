package skype.commons;

import skypeapi.wrappers.ChatWrapper;


public interface SkypeChatFactory {
	SkypeChat produce(ChatWrapper chat);
	SkypeChat produceEmpty();
}
