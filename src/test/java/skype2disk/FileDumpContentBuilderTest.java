package skype2disk;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import skype.mocks.SkypeApiMock;
import skype.mocks.SkypeChatMock;

public class FileDumpContentBuilderTest {

	@Test
	public void happyDayTest() throws ParseException {
		SkypeChatMock chat = SkypeApiMock.produceChatMock("#42;$foo","moe","joe");
		FileDumpContentBuilder fileDumpEntryBuilder = new FileDumpContentBuilder(chat);
		String actual = fileDumpEntryBuilder.getContent();
		
		final String expected = 
			"Chat Id: #42;$foo\n" + 
			"Chat Time: 2011/04/21 15:00:00\n" + 
			"Chat Body Signature: content-id-mock\n" + 
			"Messages signatures: [17f4007f9024da870afae8e60f6635fd,d6bbf5c7f50d1a96fcc3a2156dbf2b86]\n" + 
			"Chat topic: (2 lines)FOO\n" + 
			"Poster: id=joe; display=JOE\n" + 
			"Poster: id=moe; display=MOE\n" + 
			"[2011/04/21 15:14:18] MOE: Hya\n" + 
			"[2011/04/21 15:14:24] JOE: Howdy\n" + 
			"	I'm doing fine";
		
		Assert.assertEquals(expected, actual);
	}
}
