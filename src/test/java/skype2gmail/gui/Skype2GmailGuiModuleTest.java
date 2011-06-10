package skype2gmail.gui;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Skype2GmailGuiModuleTest {
	@Test
	public void testInjection_ShouldInstantiateWithoutExceptions(){
		Injector injector = Guice.createInjector(new Skype2GmailGuiModule());
		injector.getInstance(Skype2GmailGui.class);
	}
}
