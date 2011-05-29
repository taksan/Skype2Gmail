package utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import skype.exceptions.ApplicationException;

public class TaskWithTimeOut<T> {

	private final Callable<T> aCallable;

	public TaskWithTimeOut(Callable<T> aCallable) {
		this.aCallable = aCallable;
	}

	public T run() {
		long timeout = 1L;
		TimeUnit minutes = TimeUnit.MINUTES;
		return run(timeout, minutes);
	}

	public T run(long timeout, TimeUnit minutes) {
		FutureTask<T> getChatsTask = new FutureTask<T>(aCallable);
		ExecutorService executor = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
		executor.submit(getChatsTask);

		T result;
		try {
			result = getChatsTask.get(timeout, minutes);
			executor.shutdown();
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return result;
	}

}
