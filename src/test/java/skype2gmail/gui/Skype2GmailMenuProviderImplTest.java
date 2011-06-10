package skype2gmail.gui;

import java.awt.MenuItem;
import java.awt.PopupMenu;

import junit.framework.Assert;

import org.junit.Test;

public class Skype2GmailMenuProviderImplTest {
	@Test
	public void getMenu_ShouldReturnMenuWithExitOption(){
		Skype2GmailMenuProviderImpl subject = new Skype2GmailMenuProviderImpl();
		PopupMenu menu = subject.getPopupMenu();
		
		Assert.assertEquals(1, menu.getItemCount());
		MenuItem item = menu.getItem(0);
		Assert.assertEquals("Exit", item.getLabel());
	}
}
