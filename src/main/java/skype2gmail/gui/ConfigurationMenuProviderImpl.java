package skype2gmail.gui;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ConfigurationMenuProviderImpl
	extends JFrame
	implements ConfigurationMenuProvider {

	@Override
	public void display() {
		this.setVisible(true);
	}
}
