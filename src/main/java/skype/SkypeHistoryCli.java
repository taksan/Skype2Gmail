package skype;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import skype2disk.HistoryDir;
import skype2disk.Skype2GmailConfigDir;

import com.google.inject.Inject;

public class SkypeHistoryCli implements HistoryDir {
	private final Skype2GmailConfigDir skype2GmailConfigDir;
	private CommandLine cmd;
	private SkypeHistoryOptions cliOptions;
	private Skype2GmailVersion version = new Skype2GmailVersion();
	
	@Inject
	public SkypeHistoryCli(String [] args, Skype2GmailConfigDir skype2GmailConfigDir) {
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
	
	public boolean hasHelp() {
		return cliOptions.hasHelpOption(cmd);
	}
	
	public File getHistoryDir() {
		String dir = this.getHistoryDirLocation();
		File historyDir = new File(dir);
		if (!historyDir.exists())
			historyDir.mkdirs();
		return historyDir;
	}

	public void printHelpAndExit() {
		printVersion();
		
		HelpFormatter formatter = new HelpFormatter();
		String commandLine = String.format("java -jar skype2gmail-%s.jar", version.getVersion());
		formatter.printHelp( commandLine, cliOptions );
		System.exit(1);
	}

	public void printVersionAndExit() {
		printVersion();
		System.exit(1);
	}

	private String getHistoryDirLocation() {
		if (cliOptions.hasHistoryOutput(cmd))
			return cliOptions.getHistoryOutput(cmd);
		return this.getDefaultDumpDirectory();
	}
	
	private String getDefaultDumpDirectory() {
		return this.skype2GmailConfigDir.getFileNameUnder("history");
	}
	
	private void printVersion() {
		System.out.println(version.getVersionMessage());
	}
}
