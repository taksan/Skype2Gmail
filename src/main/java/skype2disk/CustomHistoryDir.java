package skype2disk;

import java.io.File;

import com.google.inject.Inject;

public class CustomHistoryDir implements HistoryDir {

	private final String[] args;
	private final Skype2GmailConfigDir skype2GmailConfigDir;

	@Inject
	public CustomHistoryDir(String[] args, Skype2GmailConfigDir skype2GmailConfigDir) {
		this.args = args;
		this.skype2GmailConfigDir = skype2GmailConfigDir;
	}
	
	@Override
	public File getHistoryDir() {
		String dir;
		if(args.length < 1)
			dir = getDefaultDumpDirectory();
		else
			dir = args[0];
		File historyDir = new File(dir);
		if (!historyDir.exists())
			historyDir.mkdirs();
		return historyDir;
	}
	
	private String getDefaultDumpDirectory() {
		return this.skype2GmailConfigDir.getFileNameUnder("history");
	}
}
