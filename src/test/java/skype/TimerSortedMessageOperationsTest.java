package skype;

import static testutils.MessageProducerUtil.createMessage;

import org.junit.Assert;
import org.junit.Test;

public class TimerSortedMessageOperationsTest {
	@Test
	public void testFindEarliestCommonMessage() {
		TimeSortedMessages tsm1 = new TimeSortedMessages();
		tsm1.add(createMessage("joe", "m5", "22/03 15:24:20"));
		tsm1.add(createMessage("moe", "fellow", "22/03 15:24:22"));
		tsm1.add(createMessage("moe", "m3", "23/03 15:24:22"));
		
		SkypeChatMessage expectedMessage = createMessage("moe", "fellow", "21/03 15:24:18");
		
		TimeSortedMessages tsm2 = new TimeSortedMessages();
		tsm2.add(createMessage("joe", "m1", "21/03 15:15:18"));
		tsm2.add(expectedMessage);
		tsm2.add(createMessage("joe", "m5", "22/03 15:24:18"));
		tsm1.add(createMessage("moe", "m3", "23/03 15:24:22"));
		
		
		String expected = expectedMessage.toString(); 
		assertExpectedMessageEnsureComutative(tsm1, tsm2, expected);
	}

	
	@Test
	public void testFindEarliestCommonMessage2() {
		TimeSortedMessages tsm1 = new TimeSortedMessages();
		tsm1.add(createMessage("joe", "m5", "22/03 15:24:20"));
		tsm1.add(createMessage("moe", "fellow", "22/03 15:24:22"));
		tsm1.add(createMessage("moe", "m3", "23/03 15:24:22"));
		
		SkypeChatMessage expectedMessage = createMessage("moe", "m3", "20/03 15:24:22");
		TimeSortedMessages tsm2 = new TimeSortedMessages();
		tsm2.add(expectedMessage);
		tsm2.add(createMessage("joe", "m1", "21/03 15:15:18"));
		tsm2.add(createMessage("moe", "fellow", "21/03 15:24:18"));
		tsm2.add(createMessage("joe", "m5", "22/03 15:24:18"));
		
		
		String expected = expectedMessage.toString(); 
		assertExpectedMessageEnsureComutative(tsm1, tsm2, expected);
	}
	
	@Test
	public void testSplitMessageListKeepingSplitPointInTheFirstSegment(){
		SkypeChatMessage splitPoint = createMessage("moe", "m3", "23/03 15:24:22");
		TimeSortedMessages tsm1 = new TimeSortedMessages();
		tsm1.add(createMessage("joe", "m5", "22/03 15:24:20"));
		tsm1.add(createMessage("moe", "fellow", "22/03 15:24:22"));
		tsm1.add(splitPoint);
		tsm1.add(createMessage("joe", "m5", "23/03 15:30:22"));
		tsm1.add(createMessage("hir", "m9", "23/03 15:35:22"));
		
		TimeSortedMessages[] segments = TimerSortedMessageOperations.splitMessageListKeepingSplitPointInTheFirstSegment(tsm1, splitPoint);
		
		TimeSortedMessages expectedFirst = new TimeSortedMessages();
		expectedFirst.add(createMessage("joe", "m5", "22/03 15:24:20"));
		expectedFirst.add(createMessage("moe", "fellow", "22/03 15:24:22"));
		expectedFirst.add(splitPoint);
		Assert.assertEquals(expectedFirst.toString(), segments[0].toString());
		
		TimeSortedMessages expectedSecond = new TimeSortedMessages();
		expectedSecond.add(createMessage("joe", "m5", "23/03 15:30:22"));
		expectedSecond.add(createMessage("hir", "m9", "23/03 15:35:22"));
		Assert.assertEquals(expectedSecond.toString(), segments[1].toString());
	}
	
	@Test
	public void testGetListWithTimesRelativeToGivenMessage() {
		TimeSortedMessages tsm1 = new TimeSortedMessages();
		tsm1.add(createMessage("joe", "m5", "22/03 15:24:20"));
		tsm1.add(createMessage("moe", "fellow", "22/03 15:24:22"));
		tsm1.add(createMessage("moe", "m3", "23/03 15:24:22"));
		tsm1.add(createMessage("joe", "m7", "23/03 15:30:22"));
		tsm1.add(createMessage("hir", "m9", "23/03 15:35:22"));
		
		SkypeChatMessage normalizationReference = createMessage("hir", "m9", "22/03 15:35:22");
		TimeSortedMessages normalizedList = TimerSortedMessageOperations.
			getMessagesWithTimeReferenceAdjustedToGivenMessage(tsm1, normalizationReference);
		
		
		TimeSortedMessages expected = new TimeSortedMessages();
		expected.add(createMessage("joe", "m5", "21/03 15:24:20"));
		expected.add(createMessage("moe", "fellow", "21/03 15:24:22"));
		expected.add(createMessage("moe", "m3", "22/03 15:24:22"));
		expected.add(createMessage("joe", "m7", "22/03 15:30:22"));
		expected.add(createMessage("hir", "m9", "22/03 15:35:22"));
		
		Assert.assertEquals(expected.toString(), normalizedList.toString());
	}
	
	private void assertExpectedMessageEnsureComutative(TimeSortedMessages tsm1,
			TimeSortedMessages tsm2, String expected) {
		String earliest1 = TimerSortedMessageOperations.findEarliestCommonMessage(tsm1, tsm2).toString();
		Assert.assertEquals(expected, earliest1);
		String earliest2 = TimerSortedMessageOperations.findEarliestCommonMessage(tsm2, tsm1).toString();
		Assert.assertEquals(expected, earliest2);
	}
}
