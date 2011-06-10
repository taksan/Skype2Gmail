package skype2gmail.commons;

import java.util.TimerTask;

import com.google.inject.Inject;

import skype2gmail.gui.TimedTaskExecutor;
import skype2gmail.gui.TimedTaskExecutorStarter;

public class TimedTaskExecutorImpl implements TimedTaskExecutor, Runnable {

	private Thread thread;
	private TimerTask taskToRun;
	private int secondsBetweenInvocations;
	private final TimerControl timeController;
	
	@Inject
	public TimedTaskExecutorImpl(TimerControl timeController) {
		this.timeController = timeController;
	}

	@Override
	public TimedTaskExecutorStarter setTask(TimerTask aTaskToRun, int secondsBetweenInvocations) {
		this.taskToRun = aTaskToRun;
		this.secondsBetweenInvocations = secondsBetweenInvocations;
		thread = new Thread(this);
		thread.setDaemon(true);
		
		return new TimedTaskExecutorStarterImpl();
	}

	@Override
	public void run() {
		while(true) {
			taskToRun.run();
			try {
				timeController.sleep(secondsBetweenInvocations);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	private class TimedTaskExecutorStarterImpl implements TimedTaskExecutorStarter {

		@Override
		public void start() {
			thread.start();
		}		
	}
}
