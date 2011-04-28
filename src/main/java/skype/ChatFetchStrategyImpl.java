package skype;

public class ChatFetchStrategyImpl implements ChatFetchStrategy {

	@Override
	public boolean fetchOnlyRecent() {
		return false;
	}

}
