package skype;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import utils.DaemonThreadFactory;
import utils.LoggerProvider;

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
	private final ChatFetchStrategy chatFetchStrategy;

	@Inject
	public SkypeApiImpl(SkypeChatFactory chatFactory, LoggerProvider loggerProvider, ChatFetchStrategy chatFetchStrategy) {
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
		
		Logger logger = loggerProvider.getPriorityLogger(getClass());
		logger.info((String.format("Found %d chats.", chatsArray.length)));
		
		for (Chat chat : chatsArray) {
			final SkypeChat skypeChat = chatFactory.produce(chat);
			visitor.visit(skypeChat);
		}
	}

	private Chat[] getChatHistory() {
		final Callable<Chat[]> getAllChats = new Callable<Chat[]>() {
			@Override
			public Chat[] call() throws Exception {
				return getAllChats();
			}
		};
		final Callable<Chat[]> getRecentChats = new Callable<Chat[]>() {
			@Override
			public Chat[] call() throws Exception {
				return getRecentChats();
			}
		};
		Callable<Chat[]> getChatsCallable = getAllChats;
		if (chatFetchStrategy.fetchOnlyRecent()) {
			getChatsCallable = getRecentChats;
		}
		
		Chat[] allChatsArray = executeWithTimeout(getChatsCallable);
		
		return allChatsArray;
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

		FutureTask<T> getChatsTask = new FutureTask<T>(aCallable);
		ExecutorService executor = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
		executor.submit(getChatsTask);

		T result;
		try {
			result = getChatsTask.get(1L, TimeUnit.MINUTES);
			executor.shutdown();
		} catch (InterruptedException e) {
			throw new ApplicationException(e);
		} catch (ExecutionException e) {
			throw new ApplicationException(e);
		} catch (TimeoutException e) {
			throw new ApplicationException(e);
		}
		return result;
	}

}
