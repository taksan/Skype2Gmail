package skype;

public interface SkypeUserFactory {

	SkypeUser produce(String userId, String displayName);

}
