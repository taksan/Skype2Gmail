package skype2gmail.gui;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationWindowProviderImplTest {
	ConfigurationMenuProviderImpl subject = new ConfigurationMenuProviderImpl();
	
	@Test
	public void testDisplay_WindowShouldBeVisible() {
		Assert.assertTrue(subject.isShowing());
	}

	@Before
	public void setup() {
		subject.display();
	}
	
	@After
	public void teardown() {
		subject.dispose();
	}
}
