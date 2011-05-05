package skype;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.inject.Inject;

import skype2disk.HistoryDir;
import skype2disk.Skype2GmailConfigDir;

public class SkypeHistoryCliOptions implements HistoryDir {
	private final Skype2GmailConfigDir skype2GmailConfigDir;
	private CommandLine cmd;
	private SkypeHistoryOptions cliOptions;
	
	@Inject
	public SkypeHistoryCliOptions(String [] args, Skype2GmailConfigDir skype2GmailConfigDir) {
		this.skype2GmailConfigDir = skype2GmailConfigDir;
		
		cliOptions = new SkypeHistoryOptions();
		parseOptions(args, cliOptions);
	}

	private void parseOptions(String[] args, Options cliOptions) {
		CommandLineParser parser = new PosixParser();
		try {
			cmd = parser.parse( cliOptions, args);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isSyncToDisk() {
		return cliOptions.hasDiskOption(cmd);
	}

	public boolean isSyncToMail() {
		return cliOptions.hasMailOption(cmd);
	}
	
	public boolean hasVersion() {
		return cliOptions.hasVersionOption(cmd);
	}
	
	public File getHistoryDir() {
		String dir = this.getHistoryDirOption();
		File historyDir = new File(dir);
		if (!historyDir.exists())
			historyDir.mkdirs();
		return historyDir;
	}

	public String getHistoryDirOption() {
		if (cliOptions.hasHistoryOutput(cmd))
			return cliOptions.getHistoryOutput(cmd);
		return this.getDefaultDumpDirectory();
	}
	private String getDefaultDumpDirectory() {
		return this.skype2GmailConfigDir.getFileNameUnder("history");
	}
}
