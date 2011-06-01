package skype.commons;

import java.util.LinkedList;

import junit.framework.Assert;

import main.SkypeHistoryMain;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import skype.commons.SkypeCliOptions;

public class SkypeHistoryMainTest {
	@Test
	public void testInvokeHelp() {
		SkypeCliOptionsMock options = new SkypeCliOptionsMock();
		options.hasHelp = true;
		SkypeHistoryMain subject = new SkypeHistoryMain(options, null);
		subject.run();
		
		String actual = options.getOperations();
		Assert.assertEquals("printHelpAndExit", actual);
	}
	
	@Test
	public void testInvokeVersion() {
		SkypeCliOptionsMock options = new SkypeCliOptionsMock();
		options.hasVersion = true;
		SkypeHistoryMain subject = new SkypeHistoryMain(options, null);
		subject.run();
		
		String actual = options.getOperations();
		Assert.assertEquals("printVersionAndExit", actual);
	}
	
	private final class SkypeCliOptionsMock implements SkypeCliOptions {
		LinkedList<String> operations = new LinkedList<String>();
		public boolean hasVersion;
		public boolean hasHelp;
		
		public String getOperations() {
			return StringUtils.join(operations, ",");
		}

		@Override
		public void printVersionAndExit() {
			operations.add("printVersionAndExit");
		}

		@Override
		public void printHelpAndExit() {
			operations.add("printHelpAndExit");
		}

		@Override
		public boolean hasVersion() {
			return hasVersion;
		}

		@Override
		public boolean hasHelp() {
			return hasHelp;
		}

		@Override
		public boolean isSyncToMail() {
			return false;
		}

		@Override
		public boolean isSyncToDisk() {
			return false;
		}
	}
}
