package skype.commons;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

@SuppressWarnings("serial")
public class SkypeHistoryOptions extends Options {
	private static final String HELP_OPTION_NAME = "help";
	private static final String VERSION_OPTION = "version";
	private static final String DISK_OPTION_NAME = "disk";
	private static final String MAIL_OPTION_NAME = "mail";
	private static final String HISTORY_OUTPUT_DIR_OPTION_NAME = "historyOutputDir";

	public SkypeHistoryOptions() {
		addOptionalOption("h", HELP_OPTION_NAME, "Display help");
		addOptionalOption("d", DISK_OPTION_NAME, "Write chats to local disk");
		addOptionalOptionWithArgument("out", HISTORY_OUTPUT_DIR_OPTION_NAME, "Directory to write skype messages when disk option is enabled");
		addOptionalOption("m", MAIL_OPTION_NAME, "Send chats to gmail");
		addOptionalOption("v", VERSION_OPTION, "Prints the application version");
	}

	private void addOptionalOptionWithArgument(String shortOptFlag,
			String optFlag, String description) {
		Option opt = new Option(shortOptFlag, optFlag, true, description);
		opt.setRequired(false);
		this.addOption(opt);
	}

	private void addOptionalOption(String shortOptFlag,
			String optFlag, String description) {
		Option opt = new Option(shortOptFlag, optFlag, false, description);
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

	public boolean hasHelpOption(CommandLine cmd) {
		return cmd.hasOption(HELP_OPTION_NAME);
	}
}
