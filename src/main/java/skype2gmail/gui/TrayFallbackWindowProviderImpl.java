package skype2gmail.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.google.inject.Inject;

@SuppressWarnings("serial")
public class TrayFallbackWindowProviderImpl
	extends JFrame
	implements TrayFallbackWindowProvider {

	private final ConfigurationMenuProvider configurationMenu;

	@Inject
	public TrayFallbackWindowProviderImpl(ConfigurationMenuProvider configurationMenu) {
		this.configurationMenu = configurationMenu;
		addComponents();
	}


	@Override
	public void display() {
		this.setVisible(true);
	}
	

	private void addComponents() {
		this.setTitle("Skype2Gmail");
		this.getContentPane().add(createConfigureButton());
	}
	
	private JButton createConfigureButton() {
		JButton configureJButton = new JButton("Configure");
		configureJButton.setName("configure-button");
		configureJButton.addActionListener(makeConfigurationWindowActionListener());
	
		return configureJButton;
	}

	private ActionListener makeConfigurationWindowActionListener() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				configurationMenu.display();
			}
		};
	}
}
