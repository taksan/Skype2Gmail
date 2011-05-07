package skype;

import java.io.InputStream;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;

public class Skype2GmailVersion {
	private static String POM_FULL_PATH = "/META-INF/maven/com.github.taksan/skype2gmail/pom.xml";

	public String getVersionMessage() {
			String version = getVersion();
			return String.format("Skype2Gmail version \"%s\"", version);
	}
	
	public String getVersion() {
		InputStream pomStream = this.getClass().getResourceAsStream(POM_FULL_PATH);
		MavenXpp3Reader mavenreader = new MavenXpp3Reader();
		Model model = null;
		model = getModelOrCry(pomStream, model, mavenreader);
		MavenProject project = new MavenProject(model);
		return project.getVersion();
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
}
