package skype;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

@SuppressWarnings("serial")
public class SkypeHistoryOptions extends Options{
	private static final String VERSION_OPTION = "version";
	private static final String DISK_OPTION_NAME = "disk";
	private static final String MAIL_OPTION_NAME = "mail";
	private static final String HISTORY_OUTPUT_DIR_OPTION_NAME = "historyOutputDir";

	public SkypeHistoryOptions() {
		addOptionalOption(DISK_OPTION_NAME, "Synchronize to local disk");
		addOptionalOptionWithArgument(HISTORY_OUTPUT_DIR_OPTION_NAME, "Synchronize to local disk");
		addOptionalOption(MAIL_OPTION_NAME, "Synchronize to gmail");
		addOptionalOption(VERSION_OPTION, "Prints the application version");
		}
	
	private void addOptionalOptionWithArgument(String optFlag, String description) {
		Option opt = new Option(optFlag, true, description);
		opt.setRequired(false);
		this.addOption(opt);
	}

	private void addOptionalOption(String optFlag, String description) {
		Option opt = new Option(optFlag, false, description);
		opt.setRequired(false);
		this.addOption(opt);
	}
	
	public String getHistoryOutput(CommandLine cmd) {
		return cmd.getOptionValue(HISTORY_OUTPUT_DIR_OPTION_NAME);
	}
	
	public boolean hasHistoryOutput(CommandLine cmd) {
		return cmd.hasOption(HISTORY_OUTPUT_DIR_OPTION_NAME);
	}

	public boolean hasMailOption(CommandLine cmd) {
		return cmd.hasOption(MAIL_OPTION_NAME);
	}

	public boolean hasDiskOption(CommandLine cmd) {
		return cmd.hasOption(DISK_OPTION_NAME);
	}

	public boolean hasVersionOption(CommandLine cmd) {
		return cmd.hasOption(VERSION_OPTION);
	}
}
