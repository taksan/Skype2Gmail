package skype.commons;

import com.google.inject.ImplementedBy;

@ImplementedBy(SkypeHistoryCli.class)
public interface SkypeCliOptions {

	public abstract boolean isSyncToDisk();

	public abstract boolean isSyncToMail();

	public abstract boolean hasVersion();

	public abstract boolean hasHelp();

	public abstract void printHelpAndExit();

	public abstract void printVersionAndExit();

}