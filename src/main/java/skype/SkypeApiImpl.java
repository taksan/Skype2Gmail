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

import com.google.inject.Inject;
import com.skype.Chat;
import com.skype.Profile;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.connector.Connector;

public class SkypeApiImpl implements SkypeApi {

	private static final Logger LOGGER = Logger.getLogger(SkypeApiImpl.class);
	private final SkypeChatFactory chatFactory;
	private Chat[] allChats;
	private Profile profile;
	private SkypeUser currentUser;

	@Inject
	public SkypeApiImpl(SkypeChatFactory chatFactory) {
		this.chatFactory = chatFactory;
		Connector.getInstance().setApplicationName("Skype2Gmail");
	}

	@Override
	public boolean isRunning() {
		try {
			return Skype.isRunning();
		} catch (SkypeException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void accept(final SkypeApiChatVisitor visitor) {
		Callable<Chat[]> getAllChatsCallable = new Callable<Chat[]>() {
			@Override
			public Chat[] call() throws Exception {
				return getAllChats();
			}
		};
		Chat[] allChatsArray = executeWithTimeout(getAllChatsCallable);
		LOGGER.info(String.format("Found %d chats.", allChatsArray.length));
		
		for (Chat chat : allChatsArray) {
			final SkypeChat skypeChat = chatFactory.produce(chat);
			visitor.visit(skypeChat);
		}
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
		if (allChats != null)
			return allChats;

		allChats = Skype.getAllChats();
		return allChats;
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
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause());
		} catch (TimeoutException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

}
