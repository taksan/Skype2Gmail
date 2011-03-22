package utils;

import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {
	@Override
	public Thread newThread(Runnable paramRunnable) {
		Thread thread = new Thread(paramRunnable);
		thread.setDaemon(true);
		return thread;
	}
}
