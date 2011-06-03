package skype.commons;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import junit.framework.Assert;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Test;

import skype.exceptions.ApplicationException;
import skype.exceptions.MessageProcessingException;
import skype.mocks.LastSynchronizationProviderMock;
import skype.mocks.SkypeApiMock;
import skype.mocks.SkypeStorageMock;
import utils.SimpleLoggerProvider;

public class SkypeRecorderTest {

	@Test
	public void recordTwoMessages_ShouldStoreMessageContents() {
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("42", "moe", "joe"));
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("73", "john", "doe"));

		SkypeStorage skypeStorage = new SkypeStorageMock(null);
		LastSynchronizationProviderMock lspMock = new LastSynchronizationProviderMock();
		SkypeRecorder skypeRecorder_SUBJECT = new SkypeRecorder(skypeApi, skypeStorage, new SimpleLoggerProvider(), lspMock);
		skypeRecorder_SUBJECT.record();

		final String contentsRecordedInTheFirstInvocation = skypeStorage.toString().trim();

		String expected = "@SkypeStorageMock:\n"
				+ "@StorageEntryMock: ------\n"
				+ "chatAuthor:\n"
				+ "chatId:42\n"
				+ "Time:2011/04/21 15:00:00\n"
				+ "Body Signature:content-id-mock\n"
				+ "Messages Signatures:17f4007f9024da870afae8e60f6635fd,d6bbf5c7f50d1a96fcc3a2156dbf2b86\n"
				+ "Topic:(2 lines) FOO\n"
				+ "Poster:id=joe; displayName=JOE\n"
				+ "Poster:id=moe; displayName=MOE\n"
				+ "[2011/04/21 15:14:18] MOE: Hya\n"
				+ "[2011/04/21 15:14:24] JOE: Howdy\n"
				+ "	I'm doing fine\n"
				+ "Last modified:2011/04/21 15:14:24\n"
				+ "@StorageEntryMock: ------\n"
				+ "chatAuthor:\n"
				+ "chatId:73\n"
				+ "Time:2011/04/21 15:00:00\n"
				+ "Body Signature:content-id-mock\n"
				+ "Messages Signatures:c0687481b3f39aac8fd1ad874f604301,38e57729cade7223217d12bee978a509\n"
				+ "Topic:(2 lines) FOO\n" + "Poster:id=doe; displayName=DOE\n"
				+ "Poster:id=john; displayName=JOHN\n"
				+ "[2011/04/21 15:14:18] JOHN: Hya\n"
				+ "[2011/04/21 15:14:24] DOE: Howdy\n" + "	I'm doing fine\n"
				+ "Last modified:2011/04/21 15:14:24";
		Assert.assertEquals(expected, contentsRecordedInTheFirstInvocation);

		skypeRecorder_SUBJECT.record();
		final String actualForSecondRecord = skypeStorage.toString().trim();
		Assert.assertEquals(expected, actualForSecondRecord);

		skypeRecorder_SUBJECT.record();
		Assert.assertTrue("Should have invoked update", lspMock.updateWasInvoked());
	}

	@Test
	public void recordZeroMessages_ShouldEmmitWarnInLog() {
		ByteArrayOutputStream logStream = getLoggerAppenderOutputStream();
		SkypeApiMock skypeApi = new SkypeApiMock();

		final String actualStoredContent = submitMockedMessagesAgaingSubjectRecorder(skypeApi);

		String expected = "@SkypeStorageMock:";
		Assert.assertEquals(expected, actualStoredContent);

		assertThatGivenEntryIsInLogOutput("INFO - No messages were synchronized. All of them were up to date.", logStream);
	}
	
	@Test
	public void recordWithMessageProcessingError_ShouldNotProduceOutputNeitherException() {
		ByteArrayOutputStream logStream = getLoggerAppenderOutputStream();
		
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(createChatWithError());
		
		final String actualStoredContent =submitMockedMessagesAgaingSubjectRecorder(skypeApi);
		
		String expected = "@SkypeStorageMock:";
		Assert.assertEquals(expected, actualStoredContent);
		
		assertThatGivenEntryIsInLogOutput("ERROR - An error was found processing message with the following id: foo", logStream);
	}
	
	@Test
	public void recordWithMessageProcessingError_ShouldNotPreventProcessingNextChat() {
		ByteArrayOutputStream logStream = getLoggerAppenderOutputStream();
		
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(createChatWithError());
		skypeApi.addMockChat(SkypeApiMock.produceChatMock("42", "moe", "joe"));
		
		SkypeStorage skypeStorage = new SkypeStorageMock(null);
		LastSynchronizationProviderMock lspMock = new LastSynchronizationProviderMock();
		SkypeRecorder skypeRecorder_SUBJECT = new SkypeRecorder(skypeApi, skypeStorage, new SimpleLoggerProvider(), lspMock);
		skypeRecorder_SUBJECT.record();
		final String actualForFirstRecord = submitMockedMessagesAgaingSubjectRecorder(skypeApi);
		
		String expected = "@SkypeStorageMock:\n" + 
				"@StorageEntryMock: ------\n" + 
				"chatAuthor:\n" + 
				"chatId:42\n" + 
				"Time:2011/04/21 15:00:00\n" + 
				"Body Signature:content-id-mock\n" + 
				"Messages Signatures:17f4007f9024da870afae8e60f6635fd,d6bbf5c7f50d1a96fcc3a2156dbf2b86\n" + 
				"Topic:(2 lines) FOO\n" + 
				"Poster:id=joe; displayName=JOE\n" + 
				"Poster:id=moe; displayName=MOE\n" + 
				"[2011/04/21 15:14:18] MOE: Hya\n" + 
				"[2011/04/21 15:14:24] JOE: Howdy\n" + 
				"	I'm doing fine\n" + 
				"Last modified:2011/04/21 15:14:24";
		Assert.assertEquals(expected, actualForFirstRecord);
		
		assertThatGivenEntryIsInLogOutput("ERROR - An error was found processing message with the following id: foo", logStream);
	}
	
	@Test
	public void recordChatWithoutMessages_ShouldEmmitWarning() {
		ByteArrayOutputStream logStream = getLoggerAppenderOutputStream();
		
		SimpleLoggerProvider loggerProvider = new SimpleLoggerProvider();
		SkypeApiMock skypeApi = new SkypeApiMock();
		skypeApi.addMockChat(new EmptySkypeChat(loggerProvider));
		
		final String storedContents = submitMockedMessagesAgaingSubjectRecorder(skypeApi);
		
		String expected = "@SkypeStorageMock:";
		Assert.assertEquals(expected, storedContents);
		
		assertThatGivenEntryIsInLogOutput("WARN - Entry  skipped because it has no messages.", logStream);
	}
	
	@Test
	public void recordChatWithApplicationError_ShouldEmmitErrorInLogOutput() {
		SkypeApi skypeApi = createSkypeApiWithApplicationError();
		ByteArrayOutputStream logStream = getLoggerAppenderOutputStream();
		submitMockedMessagesAgaingSubjectRecorder(skypeApi);
		
		assertThatGivenEntryIsInLogOutput("ApplicationException", logStream);
	}

	private SkypeApi createSkypeApiWithApplicationError() {
		return new SkypeApi() {
			
			@Override
			public boolean isRunning() {
				return true;
			}
			
			@Override
			public SkypeUser getCurrentUser() {
				throw new NotImplementedException();
			}
			
			@Override
			public void accept(SkypeApiChatVisitor visitor) {
				throw new ApplicationException("foo");
			}
		};
	}

	private String submitMockedMessagesAgaingSubjectRecorder(SkypeApi skypeApi) {
		return submitMockedMessagesAgaingSubjectRecorder(skypeApi, new SimpleLoggerProvider());
	}

	private String submitMockedMessagesAgaingSubjectRecorder(
			SkypeApi skypeApi, SimpleLoggerProvider loggerProvider) {
		SkypeStorage skypeStorage = new SkypeStorageMock(null);
		LastSynchronizationProviderMock lspMock = new LastSynchronizationProviderMock();
		SkypeRecorder skypeRecorder_SUBJECT = new SkypeRecorder(skypeApi, skypeStorage, loggerProvider, lspMock);
		skypeRecorder_SUBJECT.record();
		final String actualForFirstRecord = skypeStorage.toString().trim();
		return actualForFirstRecord;
	}

	private void assertThatGivenEntryIsInLogOutput(String expectedEntry, ByteArrayOutputStream outputStream) {
		String log = new String(outputStream.toByteArray()).trim();
		Assert.assertTrue(log.contains(expectedEntry));
	}

	private SkypeChat createChatWithError() {
		return (SkypeChat) Proxy.newProxyInstance(getClass().getClassLoader(), 
				new Class<?>[]{SkypeChat.class}, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						if (method.getName().equals("getId")) {
							return "foo";
						}
						throw new MessageProcessingException("force message processing exception");
					}
				});
	}

	private ByteArrayOutputStream getLoggerAppenderOutputStream() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		BasicConfigurator.configure(new WriterAppender(new SimpleLayout(), outputStream));
		return outputStream;
	}
}
