package skype;

import skype2disk.FileDumpContentBuilder;

public class MessageBodyBuilder {
	private String previousSender = "";
	private StringBuilder messageText = new StringBuilder();
	
	public void appendMessage(SkypeChatMessage aChatMessage) {
		boolean printSender = shouldPrintSender(previousSender ,
				aChatMessage);

		previousSender = aChatMessage.getSenderId();
		String messageLine = aChatMessage.messageText(printSender);
		messageLine = FileDumpContentBuilder.escape(messageLine);
		messageText.append(messageLine);
	}

	private boolean shouldPrintSender(String previousSender,
			SkypeChatMessage aChatMessage) {
		boolean printSender;
		if (previousSender.equals(aChatMessage.getSenderId()))
			printSender = false;
		else
			printSender = true;
		return printSender;
	}

	public String getMessageBody() {
		return this.messageText.toString().trim();
	}
}
