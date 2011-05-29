package mail;

import mail.api.CommandLineShell;

import com.google.inject.ImplementedBy;

@ImplementedBy(CommandLineShell.class)
public interface UserShell {

	String prompt(String message);

	String promptPassword(String message);

	void displayWarning(String message);

}
