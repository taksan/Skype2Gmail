package skype2gmail.gui;

import javax.swing.JFrame;

import org.apache.commons.lang.NotImplementedException;

@SuppressWarnings("serial")
public class TrayFallbackWindowProviderImpl
	extends JFrame
	implements TrayFallbackWindowProvider {

	@Override
	public void display() {
		throw new NotImplementedException();
	}
}
