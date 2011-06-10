package skype.commons;

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
import com.google.inject.Singleton;

@Singleton
public class SkypeHistoryCli implements HistoryDir, SkypeCliOptions {
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

	/* (non-Javadoc)
	 * @see skype.SkypeCliOptions#isSyncToDisk()
	 */
	@Override
	public boolean isSyncToDisk() {
		return cliOptions.hasDiskOption(cmd);
	}

	/* (non-Javadoc)
	 * @see skype.SkypeCliOptions#isSyncToMail()
	 */
	@Override
	public boolean isSyncToMail() {
		return cliOptions.hasMailOption(cmd);
	}
	
	/* (non-Javadoc)
	 * @see skype.SkypeCliOptions#hasVersion()
	 */
	@Override
	public boolean hasVersion() {
		return cliOptions.hasVersionOption(cmd);
	}
	
	/* (non-Javadoc)
	 * @see skype.SkypeCliOptions#hasHelp()
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see skype.SkypeCliOptions#printHelpAndExit()
	 */
	@Override
	public void printHelpAndExit() {
		printVersion();
		
		HelpFormatter formatter = new HelpFormatter();
		String commandLine = String.format("java -jar skype2gmail-%s.jar", version.getVersion());
		formatter.printHelp( commandLine, cliOptions );
		System.exit(1);
	}

	/* (non-Javadoc)
	 * @see skype.SkypeCliOptions#printVersionAndExit()
	 */
	@Override
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
