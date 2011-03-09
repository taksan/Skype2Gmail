package skype2gmail;

import org.junit.Ignore;
import org.junit.Test;

import skype.SkypeHistoryRecorder;

import com.google.inject.Guice;
import com.google.inject.Injector;

@Ignore
public class Skype2GmailTest {
	@Test
	public void happyDay() {
		Injector injector = Guice.createInjector(new Skype2GmailTestModule());

		SkypeHistoryRecorder gmailRecorder = injector.getInstance(SkypeHistoryRecorder.class);
		
		gmailRecorder.record();
	}

}
