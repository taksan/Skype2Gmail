package skype;

import java.io.InputStream;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;

import skype2disk.Skype2DiskModule;
import skype2gmail.Skype2GmailModule;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;


public class SkypeHistoryMain {
	
	private final SkypeHistoryCliOptions options;
	private final String[] args;
	@Inject
	public SkypeHistoryMain(SkypeHistoryCliOptions options, String [] args) {
		this.options = options;
		this.args = args;
		
	}

	public void run() {
		if (options.hasVersion()) {
			printVersionAndExit();
		}
		if (options.isSyncToDisk()) {
			runSkypeModule(new Skype2DiskModule(args));
			return;
		}
		else
		if (options.isSyncToMail()) {
			runSkypeModule(new Skype2GmailModule());
			return;
		}
		printHelpAndExit();
	}

	private void printVersionAndExit() {
		String pomPath = "/META-INF/maven/com.github.taksan/skype2gmail/pom.xml";
		InputStream pomStream = this.getClass().getResourceAsStream(pomPath);
		Model model = null;
		MavenXpp3Reader mavenreader = new MavenXpp3Reader();
		model = getModelOrCry(pomStream, model, mavenreader);
		MavenProject project = new MavenProject(model);
		System.out.println("Skype2Gmail version \"" + project.getVersion()+"\"");

		System.exit(1);
	}

	private Model getModelOrCry(InputStream pomStream, Model model,
			MavenXpp3Reader mavenreader) {
		try {
		    model = mavenreader.read(pomStream);
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
		return model;
	}

	private void runSkypeModule(Skype2StorageModuleCommons skype2DiskModule) {
		Injector injector = Guice.createInjector(skype2DiskModule);
		SkypeRecorder skypeRecorder = injector.getInstance(SkypeRecorder.class);
		skypeRecorder.record();
	}

	private void printHelpAndExit() {
		System.out.println("Usage:");
		System.out.println("	--mail : synchronize with a gmail account");
		System.out.println("	--disk : synchronize with local disk (in the .skype2gmail directory)");
		System.exit(1);
	}

}
