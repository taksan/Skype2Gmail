package skype;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import utils.DigestProvider;

public class SkypeChatImpl implements SkypeChat {

	private static final Logger LOGGER = Logger.getLogger(SkypeChat.class);
	private TimeSortedMessages chatMessageList;
	private final UsersSortedByUserId memberIds;
	
	private final String chatId;
	private final Date chatTime;
	private final String topic;
	private final DigestProvider digestProvider;
	private String bodySignature;

	public SkypeChatImpl(DigestProvider digestProvider, 
			String chatId, 
			Date chatTime, 
			String topic, 
			UsersSortedByUserId userIds, 
			TimeSortedMessages timeSortedMessages) {
		
		this.digestProvider = digestProvider;
		this.chatId = chatId;
		this.chatTime = chatTime;
		this.topic = topic;

		memberIds = userIds;
		chatMessageList = timeSortedMessages;
	}

	@Override
	public UsersSortedByUserId getPosters() {
		return memberIds;
	}

	@Override
	public TimeSortedMessages getChatMessages() {
		return chatMessageList;
	}

	@Override
	public String getId() {
		return this.chatId;
	}

	@Override
	public Date getTime() {
		return this.chatTime;
	}

	@Override
	public String getTopic() {
		return this.topic;
	}

	@Override
	public String getBodySignature() {
		if (this.bodySignature != null) {
			return this.bodySignature;
		}
		this.bodySignature = this.calcBodySignature();
		return this.bodySignature;
	}

	private String calcBodySignature() {
		StringBuffer sb = new StringBuffer();
		for (SkypeChatMessage aMessage : this.chatMessageList) {
			sb.append(aMessage.getSignature());
		}
		final String data = this.chatId+sb.toString();
		return digestProvider.extendedEncode(data);
	}

	@Override
	public Date getLastModificationTime() {
		if (this.chatMessageList.size() == 0) {
			return Calendar.getInstance().getTime();
		}
		return this.chatMessageList.last().getTime();
	}

	@Override
	public SkypeChat merge(SkypeChat skypeChat) {
		loggerInfo("Previous chat found. Merging chats");
		final TimeSortedMessages mergedMessages = new TimeSortedMessages();
		
		LinkedBlockingQueue<SkypeChatMessage> messagesToMerge = new LinkedBlockingQueue<SkypeChatMessage>();
		messagesToMerge.addAll(skypeChat.getChatMessages());
		
		for (SkypeChatMessage skypeChatMessage : this.getChatMessages()) {
			mergedMessages.add(skypeChatMessage);
			if (skypeChatMessage.equals(messagesToMerge.element())) {
				messagesToMerge.poll();
			}
			else {
				if (messagesToMerge.contains(skypeChatMessage)) {
					while (!messagesToMerge.peek().equals(skypeChatMessage)) {
						mergedMessages.add(messagesToMerge.poll());
					}
					mergedMessages.add(messagesToMerge.poll());
				}
			}
		}
		while(!messagesToMerge.isEmpty()) {
			mergedMessages.add(messagesToMerge.poll());
		}
		
		final SkypeChatImpl mergedChat = new SkypeChatImpl(digestProvider, chatId, chatTime, topic, memberIds, mergedMessages);
		
		if (mergedChat.getBodySignature().equals(this.getBodySignature())) {
			loggerInfo("New chat was actually contained on previous chat. Update skipped.");
			return this;
		}
		
		return mergedChat;
	}

	private void loggerInfo(String message) {
		LOGGER.info(String.format("<%s> %s", this.getId(), message));
	}

	@Override
	public SkypeUser getChatAuthor() {
		UsersSortedByUserId members = this.getPosters();
		SkypeUser aUser = null;
		for(SkypeChatMessage aMessage: this.getChatMessages()) {
			aUser = members.findByDisplayName(aMessage.getSenderDisplayname());
			if (!aUser.isCurrentUser()) {
				return aUser;
			}
		}
		if (aUser != null)
			return aUser;
		
		SkypeUser currentUser = null;
		for (SkypeUser skypeUser : members) {
			if (skypeUser.isCurrentUser()) {
				currentUser = skypeUser;
				continue;
			}
			return skypeUser;
		}
		if (currentUser == null) {
			throw new MessageProcessingException("A chat without users? Impossible!");
		}
		return currentUser;
	}
}
