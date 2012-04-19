package skype.commons;

import main.SkypeHistoryMainOptions;

import org.junit.Test;

import skype2disk.SkypeHistoryMainModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SkypeHistoryMainModuleTest {
	@Test
	public void testInjection() {
		Injector historyInjector = Guice.createInjector(new SkypeHistoryMainModule(new String[0]));
		historyInjector.getInstance(SkypeHistoryMainOptions.class);
	}
}
