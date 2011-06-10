package skype.commons;

import java.util.LinkedHashMap;
import java.util.Map;

import skype2disk.Skype2DiskModule;
import skype2gmail.Skype2GmailConfigContents;
import skype2gmail.Skype2GmailModule;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class SelectedRecorderProviderImpl implements SelectedRecorderProvider {
	private Map<Class<?>, SkypeHistoryRecorder> sKypeRecorderMap = new LinkedHashMap<Class<?>, SkypeHistoryRecorder>();
	private final Injector skype2GmailInjector;
	private final Injector skype2DiskInjector;
	private final Skype2GmailConfigContents config;
	
	@Inject
	public SelectedRecorderProviderImpl(String [] args, Skype2GmailConfigContents config) {
		this.config = config;
		skype2GmailInjector = Guice.createInjector(new Skype2GmailModule());
		skype2DiskInjector = Guice.createInjector(new Skype2DiskModule(args));
		
		addSkype2Gmail();
		addSkype2Disk();
	}
	@Override
	public SkypeHistoryRecorder getSelected() {
		return sKypeRecorderMap.get(config.getSelectedRecorder());
	}
	
	private void addSkype2Disk() {
		SkypeHistoryRecorder skype2disk = skype2DiskInjector.getInstance(SkypeHistoryRecorder.class);
		addRecorder(Skype2DiskModule.class, skype2disk);
	}

	private void addSkype2Gmail() {
		SkypeHistoryRecorder skype2gmail= skype2GmailInjector.getInstance(SkypeHistoryRecorder.class);
		addRecorder(Skype2GmailModule.class, skype2gmail);
	}
	
	private void addRecorder(Class<?> moduleName,
			SkypeHistoryRecorder sKypeHistoryRecorder) {
		sKypeRecorderMap.put(moduleName, sKypeHistoryRecorder);
	}
}
