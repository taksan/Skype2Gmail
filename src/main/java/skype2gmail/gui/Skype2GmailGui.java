package skype2gmail.gui;


import java.util.TimerTask;

import skype.commons.SkypeHistoryRecorder;
import skype.commons.SelectedRecorderProvider;

import com.google.inject.Inject;

public class Skype2GmailGui {
	private final int DEFAULT_SECONDS_BETWEEN_SYNCHRONIZATIONS = 600;
	
	private final Skype2GmailTrayProvider trayProvider;
	private final TimedTaskExecutor timedTaskExecutor;
	private final SelectedRecorderProvider recorderRegistry;

	@Inject
	public Skype2GmailGui(Skype2GmailTrayProvider trayProvider,
			TimedTaskExecutor timedTaskExecutor, 
			SelectedRecorderProvider skypeRecorderRegistry) {
		this.trayProvider = trayProvider;
		this.timedTaskExecutor = timedTaskExecutor;
		this.recorderRegistry = skypeRecorderRegistry;
	}

	public void run() {
		trayProvider.createTray();
		
		timedTaskExecutor.
			setTask(createSyncTask(), DEFAULT_SECONDS_BETWEEN_SYNCHRONIZATIONS).
			start();
		
	}

	private TimerTask createSyncTask() {
		return new TimerTask() {
			@Override
			public void run() {
				SkypeHistoryRecorder currentRecorder = recorderRegistry.getSelected();
				currentRecorder.record();
			}
		};
	}
}
