package skype.commons;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.plexus.util.CollectionUtils;

class TimerSortedMessageOperations {

	static SkypeChatMessage findEarliestCommonMessage(
			TimeSortedMessages messagesA, TimeSortedMessages messagesB) {

		final TimeSortedMessages[] tsms = sortMessageListsByEarliestMessage(
				messagesA, messagesB);

		@SuppressWarnings("unchecked")
		Collection<SkypeChatMessage> intersection = 
			CollectionUtils.intersection(tsms[0], tsms[1]);
		
		if (intersection.size() == 0)
			return null;

		TimeSortedMessages sortedMessages = new TimeSortedMessages();
		sortedMessages.addAll(intersection);
		return sortedMessages.first();
	}

	private static TimeSortedMessages[] sortMessageListsByEarliestMessage(
			TimeSortedMessages messagesA, TimeSortedMessages messagesB) {
		final TimeSortedMessages[] tsms;
		if (messagesA.earlierThan(messagesB)) {
			tsms = new TimeSortedMessages[] { messagesA, messagesB };
		} else {
			tsms = new TimeSortedMessages[] { messagesB, messagesA };
		}
		return tsms;
	}

	static void mergeMessagesUsingEarliestSequenceFirst(
			TimeSortedMessages timeSortedMessages,
			TimeSortedMessages secondChatMessages) {
		throw new NotImplementedException();
	}

	static TimeSortedMessages[] splitMessageListKeepingSplitPointInTheFirstSegment(
			TimeSortedMessages timeSortedMessages,
			SkypeChatMessage splitPointAndTimeReference) {
		TimeSortedMessages firstSegment = new TimeSortedMessages();
		TimeSortedMessages secondSegment = new TimeSortedMessages();
		
		TimeSortedMessages segmentToAdd = firstSegment;
		for(SkypeChatMessage chatMessage: timeSortedMessages) {
			if (chatMessage.equals(splitPointAndTimeReference)) {
				firstSegment.add(chatMessage);
				segmentToAdd = secondSegment;
				continue;
			}
			segmentToAdd.add(chatMessage);
		}
		return new TimeSortedMessages[]{firstSegment, secondSegment};
	}
	
	static TimeSortedMessages getMessagesWithTimeReferenceAdjustedToGivenMessage(
			TimeSortedMessages messages,
			SkypeChatMessage referenceMessage) {
		SkypeChatMessage messageForOldTimeReference = messages.findFirstMessageWithSameSignature(referenceMessage);
		Date newTimeReference = referenceMessage.getTime();
		
		TimeSortedMessages adjustedList = new TimeSortedMessages();
		for (SkypeChatMessage m : messages) {
			int diffInMillis = getMessagesTimeDiffInMillis(messageForOldTimeReference, m);
			Date newMessageTime = DateUtils.addMilliseconds(newTimeReference, diffInMillis);
			
			SkypeChatMessage nm = m.createCopyWithNewTime(newMessageTime);
			adjustedList.add(nm);
		}
		
		return adjustedList;
	}

	private static int getMessagesTimeDiffInMillis(SkypeChatMessage referenceMessage,
			SkypeChatMessage m) {
		long timeInMs = m.getTime().getTime();
		long refTimeInMs = referenceMessage.getTime().getTime();
		return (int)(timeInMs - refTimeInMs);
	}
}
