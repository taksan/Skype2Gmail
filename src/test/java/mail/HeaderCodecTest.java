package mail;

import junit.framework.Assert;

import org.junit.Test;

public class HeaderCodecTest {
	@Test
	public void testEncode() {
		
		HeaderCodec headerCodec = new HeaderCodec();
		String original = "fine\r\nnot\\";
		String encoded = headerCodec.encodeText(original);
		
		Assert.assertEquals("fine\\\r\\\nnot\\\\", encoded);
		
		String decoded = headerCodec.decodeText(encoded);
		Assert.assertEquals(original, decoded);
	}
	
	@Test
	public void testDecode() {
		HeaderCodec headerCodec = new HeaderCodec();
		String actual = headerCodec.decodeText("fine\r\nno\\\nt");
		
		Assert.assertEquals("fineno\nt", actual);
		
		String actual2 = headerCodec.decodeText("\rfinen");
		
		Assert.assertEquals("finen", actual2);
		
		String actual3 = headerCodec.decodeText("\\\rfinen");
		
		Assert.assertEquals("\rfinen", actual3);
	}
}
