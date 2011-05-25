package skype;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import utils.LoggerProvider;
import utils.TaskWithTimeOut;

import com.google.inject.Inject;
import com.skype.Chat;
import com.skype.Profile;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.connector.Connector;

public class SkypeApiImpl implements SkypeApi {

	private final SkypeChatFactory chatFactory;
	private Chat[] recentChats;
	private Profile profile;
	private SkypeUser currentUser;
	private final LoggerProvider loggerProvider;
	private final ChatFetchStrategyChooser chatFetchStrategy;

	@Inject
	public SkypeApiImpl(SkypeChatFactory chatFactory, LoggerProvider loggerProvider, ChatFetchStrategyChooser chatFetchStrategy) {
		this.chatFactory = chatFactory;
		this.loggerProvider = loggerProvider;
		this.chatFetchStrategy = chatFetchStrategy;
		Connector.getInstance().setApplicationName("Skype2Gmail");
	}

	@Override
	public boolean isRunning() {
		try {
			return Skype.isRunning();
		} catch (SkypeException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void accept(final SkypeApiChatVisitor visitor) {
		Chat[] chatsArray = getChatHistory();
		
		getLogger().info((String.format("Found %d chats.", chatsArray.length)));
		
		for (Chat chat : chatsArray) {
			final SkypeChat skypeChat = chatFactory.produce(chat);
			visitor.visit(skypeChat);
		}
	}

	private Logger getLogger() {
		return loggerProvider.getPriorityLogger(getClass());
	}

	private Chat[] getChatHistory() {
		Callable<Chat[]> getChatsCallable = pickFetchStrategy();
		
		Chat[] allChatsArray = executeWithTimeout(getChatsCallable);
		
		return allChatsArray;
	}

	private Callable<Chat[]> pickFetchStrategy() {
		final Callable<Chat[]> getAllChats = createAllChatsCallable();
		final Callable<Chat[]> getRecentChats = createRecentChatsCallable();
		Callable<Chat[]> getChatsCallable = getAllChats;
		
		if (chatFetchStrategy.catFetchJustTheRecentChats()) {
			getChatsCallable = getRecentChats;
			getLogger().info("Will send just the recent chats.");
		}
		else {
			getLogger().info("Will send all chats in history.");
		}
		return getChatsCallable;
	}

	private Callable<Chat[]> createRecentChatsCallable() {
		return new Callable<Chat[]>() {
			@Override
			public Chat[] call() throws Exception {
				return getRecentChats();
			}
		};
	}

	private Callable<Chat[]> createAllChatsCallable() {
		return new Callable<Chat[]>() {
			@Override
			public Chat[] call() throws Exception {
				return getAllChats();
			}
		};
	}

	@Override
	public SkypeUser getCurrentUser() {
		if (currentUser != null)
			return currentUser;

		Callable<SkypeUser> getCurrentUserCallable = new Callable<SkypeUser>() {
			@Override
			public SkypeUser call() throws Exception {
				Profile profile = getProfile();
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

	private Chat[] getAllChats() throws SkypeException {
		if (recentChats != null)
			return recentChats;

		recentChats = Skype.getAllChats();
		return recentChats;
	}
	
	private Chat[] getRecentChats() throws SkypeException {
		if (recentChats != null)
			return recentChats;

		recentChats = Skype.getAllRecentChats();
		return recentChats;
	}

	private Profile getProfile() {
		if (profile == null)
			profile = Skype.getProfile();

		return profile;
	}

	private <T> T executeWithTimeout(Callable<T> aCallable) {
		return new TaskWithTimeOut<T>(aCallable).run();
	}

}
