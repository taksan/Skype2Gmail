package skype;

import java.util.Comparator;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class TimeSortedMessages extends TreeSet<SkypeChatMessage> {
	public TimeSortedMessages() {
		super(getTimeComparator());
	}
	
	@Override
	public String toString() {
		return getStringRepresentation();
	}

	public boolean earlierThan(TimeSortedMessages secondChatMessages) {
		if (this.size() == 0) 
			return true;
		
		if (secondChatMessages.size() == 0)
			return false;
		
		SkypeChatMessage firstMessageInTheFirstChat = this.first();
		SkypeChatMessage firstMessageInTheSecondSet = secondChatMessages.first();
		return firstMessageInTheFirstChat.earlierThan(firstMessageInTheSecondSet);
	}	

	public void addMessageListsMergingThem(
			TimeSortedMessages firstChatMessages,
			TimeSortedMessages secondChatMessages) {
		String firstAsString = firstChatMessages.getStringRepresentation();
		String secondAsString = secondChatMessages.getStringRepresentation();
		if (firstAsString.equals(secondAsString)) {
			this.addAll(firstChatMessages);
			return;
		}

		SkypeChatMessage earliesCommonMessage = 
			TimerSortedMessageOperations.findEarliestCommonMessage(firstChatMessages, secondChatMessages);
		if (earliesCommonMessage == null) {
			this.addAll(firstChatMessages);
			this.addAll(secondChatMessages);
			return;
		}
		
		TimeSortedMessages [] m1 = TimerSortedMessageOperations.
			splitMessageListKeepingSplitPointInTheFirstSegment(firstChatMessages, earliesCommonMessage);
		TimeSortedMessages normalizedM1 = TimerSortedMessageOperations.
			getMessagesWithTimeReferenceAdjustedToGivenMessage(m1[0], earliesCommonMessage);
		
		TimeSortedMessages [] m2 = TimerSortedMessageOperations.
			splitMessageListKeepingSplitPointInTheFirstSegment(secondChatMessages, earliesCommonMessage);
		TimeSortedMessages normalizedM2 = TimerSortedMessageOperations.
			getMessagesWithTimeReferenceAdjustedToGivenMessage(m2[0], earliesCommonMessage);
		
		this.addAll(normalizedM1);
		this.addAll(normalizedM2);
		
		addMessageListsMergingThem(m1[1], m2[1]);
	}

	public SkypeChatMessage findFirstMessageWithSameSignature(
			SkypeChatMessage referenceMessage) {
		final String refSignature = referenceMessage.getSignature();
		for (SkypeChatMessage c : this) {
			if (refSignature.equals(c.getSignature()))
				return c;
		}
		return null;
	}	

	private String getStringRepresentation() {
		return StringUtils.join(this,"").trim();
	}
		
	
	private static Comparator<SkypeChatMessage> getTimeComparator() {
		return new Comparator<SkypeChatMessage>() {

			@Override
			public int compare(SkypeChatMessage o1, SkypeChatMessage o2) {
				int timeComparison = o1.getTime().compareTo(o2.getTime());
				if (timeComparison != 0)
					return timeComparison;
				return o1.getSignature().compareTo(o1.getSignature());
			}
		};
	}

}
