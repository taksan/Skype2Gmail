package skype;

import java.util.Comparator;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class TimeSortedMessages extends TreeSet<SkypeChatMessage> {
	public TimeSortedMessages() {
		super(getTimeComparator());
	}

	private static Comparator<SkypeChatMessage> getTimeComparator() {
		return new Comparator<SkypeChatMessage>() {

			@Override
			public int compare(SkypeChatMessage o1, SkypeChatMessage o2) {
				return o1.getTime().compareTo(o2.getTime());
			}
		};
	}
}
