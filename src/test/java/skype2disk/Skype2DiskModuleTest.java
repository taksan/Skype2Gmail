package skype2disk;

import org.junit.Test;

import skype.SkypeHistoryRecorder;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Skype2DiskModuleTest {

	@Test
	public void happyDay() {
		Injector injector = Guice.createInjector(new Skype2DiskModule(""));

		injector.getInstance(SkypeHistoryRecorder.class);
	}
}
