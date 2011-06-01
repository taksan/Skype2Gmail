package skype.commons;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import skypeapi.wrappers.ChatWrapper;
import skypeapi.wrappers.ProfileWrapper;
import skypeapi.wrappers.SkypeWrapper;
import utils.LoggerProvider;
import utils.TaskWithTimeOut;

import com.google.inject.Inject;
import com.skype.SkypeException;

public class SkypeApiImpl implements SkypeApi {

	private final SkypeWrapper skypeWrapper;
	private final SkypeChatFactory chatFactory;
	private final LoggerProvider loggerProvider;
	private final ChatFetchStrategyChooser chatFetchStrategy;
	private SkypeUser currentUser;
	private ProfileWrapper profile;
	private ChatWrapper[] recentChats;
	private ChatWrapper[] allChats;

	@Inject
	public SkypeApiImpl(SkypeChatFactory chatFactory, 
			LoggerProvider loggerProvider, 
			ChatFetchStrategyChooser chatFetchStrategy,
			SkypeWrapper skypeWrapper) {
		
		this.chatFactory = chatFactory;
		this.loggerProvider = loggerProvider;
		this.chatFetchStrategy = chatFetchStrategy;
		this.skypeWrapper = skypeWrapper;
		this.skypeWrapper.setApplicationName("Skype2Gmail");
	}

	@Override
	public boolean isRunning() {
		return skypeWrapper.isRunning();
	}

	@Override
	public SkypeUser getCurrentUser() {
		if (currentUser != null)
			return currentUser;

		Callable<SkypeUser> getCurrentUserCallable = new Callable<SkypeUser>() {
			@Override
			public SkypeUser call() throws Exception {
				ProfileWrapper profile = getProfile();
				String fullName = profile.getFullName();
				String id = profile.getId();
				if (fullName == null)
					fullName = id;
				return new SkypeUserImpl(id, fullName, true);
			}
		};
		currentUser = executeWithTimeout(getCurrentUserCallable);
		return currentUser;

	}
	
	@Override
	public void accept(final SkypeApiChatVisitor visitor) {
		ChatWrapper[] chatsArray = getChatHistory();
		
		getLogger().info((String.format("Found %d chats.", chatsArray.length)));
		
		for (ChatWrapper chat : chatsArray) {
			final SkypeChat skypeChat = chatFactory.produce(chat);
			visitor.visit(skypeChat);
		}
	}

	private ChatWrapper[] getAllChats() throws SkypeException {
		if (allChats != null)
			return allChats;

		return skypeWrapper.getAllChats();
	}

	private ChatWrapper[] getRecentChats() throws SkypeException {
		if (recentChats != null)
			return recentChats;

		return skypeWrapper.getAllRecentChats();
	}
	
	private ProfileWrapper getProfile() {
		if (profile == null)
			profile = skypeWrapper.getProfile();

		return profile;
	}

	private <T> T executeWithTimeout(Callable<T> aCallable) {
		return new TaskWithTimeOut<T>(aCallable).run();
	}
	

	private Logger getLogger() {
		return loggerProvider.getPriorityLogger(getClass());
	}

	private ChatWrapper[] getChatHistory() {
		Callable<ChatWrapper[]> getChatsCallable = pickFetchStrategy();
		
		ChatWrapper[] allChatsArray = executeWithTimeout(getChatsCallable);
		
		return allChatsArray;
	}

	private Callable<ChatWrapper[]> pickFetchStrategy() {
		final Callable<ChatWrapper[]> getAllChats = createAllChatsCallable();
		final Callable<ChatWrapper[]> getRecentChats = createRecentChatsCallable();
		Callable<ChatWrapper[]> getChatsCallable = getAllChats;
		
		if (chatFetchStrategy.catFetchJustTheRecentChats()) {
			getChatsCallable = getRecentChats;
			getLogger().info("Will send just the recent chats.");
		}
		else {
			getLogger().info("Will send all chats in history.");
		}
		return getChatsCallable;
	}

	private Callable<ChatWrapper[]> createRecentChatsCallable() {
		return new Callable<ChatWrapper[]>() {
			@Override
			public ChatWrapper[] call() throws Exception {
				return getRecentChats();
			}
		};
	}

	private Callable<ChatWrapper[]> createAllChatsCallable() {
		return new Callable<ChatWrapper[]>() {
			@Override
			public ChatWrapper[] call() throws Exception {
				return getAllChats();
			}
		};
	}
}
