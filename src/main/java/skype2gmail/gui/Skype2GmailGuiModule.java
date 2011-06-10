package skype2gmail.gui;


import skype.commons.BasePath;
import skype.commons.SelectedRecorderProvider;
import skype.commons.SelectedRecorderProviderImpl;
import skype.commons.UserHomeBasePath;
import skype2gmail.Skype2GmailConfigContents;
import skype2gmail.Skype2GmailConfigContentsImpl;
import skype2gmail.commons.TimedTaskExecutorImpl;
import skype2gmail.commons.TimerControl;
import skype2gmail.commons.TimerControlImpl;
import tray.SystemTrayProvider;
import tray.SystemTrayProviderInterface;

import com.google.inject.AbstractModule;

public class Skype2GmailGuiModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(String[].class).toInstance(new String[0]);
		bind(Skype2GmailTrayProvider.class).to(Skype2GmailTrayProviderImpl.class);
		bind(ConfigurationMenuProvider.class).to(ConfigurationMenuProviderImpl.class);
		bind(Skype2GmailMenuProvider.class).to(Skype2GmailMenuProviderImpl.class);
		bind(Skype2GmailClickActionProvider.class).to(Skype2GmailClickActionProviderImpl.class);
		bind(SystemTrayProviderInterface.class).to(SystemTrayProvider.class);
		bind(TrayFallbackWindowProvider.class).to(TrayFallbackWindowProviderImpl.class);
		bind(Skype2GmailConfigContents.class).to(Skype2GmailConfigContentsImpl.class);
		bind(BasePath.class).to(UserHomeBasePath.class);
		bind(SelectedRecorderProvider.class).to(SelectedRecorderProviderImpl.class);
		bind(TimedTaskExecutor.class).to(TimedTaskExecutorImpl.class);
		bind(TimerControl.class).to(TimerControlImpl.class);
	}
}
