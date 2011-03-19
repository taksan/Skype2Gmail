package skype;

import skype2disk.SkypeChatSetterVisitor;

public class SkypeChatSetter {

	private final SkypeChat chat;

	public SkypeChatSetter(SkypeChat chat){
		this.chat = chat;
		
	}

	public void accept(SkypeChatSetterVisitor skypeChatSetterVisitor) {
		skypeChatSetterVisitor.visitChatAuthor(this.chat.getChatAuthor());
		skypeChatSetterVisitor.visitChatId(this.chat.getId());
		skypeChatSetterVisitor.visitDate(this.chat.getTime());
		skypeChatSetterVisitor.visitBodySignature(this.chat.getBodySignature());
		skypeChatSetterVisitor.visitMessagesSignatures(getSignatures());
		skypeChatSetterVisitor.visitTopic(this.chat.getTopic());
		skypeChatSetterVisitor.visitLastModifiedDate(this.chat.getLastModificationTime());
		
		UsersSortedByUserId chatPosters = this.chat.getPosters();
		for (SkypeUser skypeUser : chatPosters) {
			skypeChatSetterVisitor.visitPoster(skypeUser);
		}
		final TimeSortedMessages chatMessages = chat.getChatMessages();
		for (SkypeChatMessage skypeChatMessage : chatMessages) {
			skypeChatSetterVisitor.visitMessage(skypeChatMessage);
		}
	}

	private String getSignatures() {
		final TimeSortedMessages chatMessages = chat.getChatMessages();
		StringBuilder signatureBuilder = new StringBuilder(); 
		for (SkypeChatMessage aChatMessage : chatMessages) {
			signatureBuilder.append(",");
			signatureBuilder.append(aChatMessage.getSignature());
		}
		return convertToStringAndRemoveFirstComma(signatureBuilder);
	}

	private String convertToStringAndRemoveFirstComma(
			StringBuilder signatureBuilder) {
		String signatures = signatureBuilder.toString();
		if (signatures.length() > 0)
			return signatures.substring(1);
		return signatures;
	}

}
