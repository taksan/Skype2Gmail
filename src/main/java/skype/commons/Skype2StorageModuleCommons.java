package skype.commons;

import skype2disk.MessageBodyParserFactory;
import skype2disk.MessageBodyParserFactoryImpl;
import skype2gmail.Skype2GmailConfigContents;
import skype2gmail.Skype2GmailConfigContentsImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class Skype2StorageModuleCommons extends AbstractModule  {
	@Override
	protected void configure() {
		bind(SkypeChatFactory.class).to(SkypeChatFactoryImpl.class).in(Scopes.SINGLETON);
		bind(SkypeHistoryRecorder.class).to(SkypeRecorder.class).in(Scopes.SINGLETON);
		bind(SkypeChatDateFormat.class).to(SkypeChatDateFormatImpl.class).in(Scopes.SINGLETON);
		bind(SkypeMessageDateFormat.class).to(SkypeMessageDateFormatImpl.class).in(Scopes.SINGLETON);
		bind(MessageBodyParserFactory.class).to(MessageBodyParserFactoryImpl.class).in(Scopes.SINGLETON);
		bind(ChatFetchStrategyChooser.class).to(ChatFetchStrategyChooserImpl.class).in(Scopes.SINGLETON);
		bind(Skype2GmailConfigContents.class).to(Skype2GmailConfigContentsImpl.class).in(Scopes.SINGLETON);
	}
}
