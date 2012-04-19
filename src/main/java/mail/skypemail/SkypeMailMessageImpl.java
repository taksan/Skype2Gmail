package mail.skypemail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import mail.HeaderCodec;
import mail.MailSession;
import mail.SkypeMailMessage;
import mail.SkypeMailStore;

import org.apache.log4j.Logger;

import skype.commons.SkypeUser;
import skype.exceptions.MessageProcessingException;
import skype2gmail.FolderIndex;
import skype2gmail.SkypeChatFolderProvider;

class SkypeMailMessageImpl implements SkypeMailMessage {
	private final SkypeChatFolderProvider chatFolderProvider;
	private final SkypeMailStore mailStore;
	private final Logger logger;
	

	SkypeMailMessageImpl(MimeMessage message,
			SkypeChatFolderProvider chatFolderProvider,
			SkypeMailStore mailStore, Logger logger) {
		this.mimeMessage = message;
		this.chatFolderProvider = chatFolderProvider;
		this.mailStore = mailStore;
		this.logger = logger;
	}
	
	SkypeMailMessageImpl(MailSession session, 
			SkypeChatFolderProvider chatFolderProvider, 
			SkypeMailStore mailStore, 
			Logger logger) {
		this.mimeMessage = session.createMimeMessage();
		this.chatFolderProvider = chatFolderProvider;
		this.mailStore = mailStore;
		this.logger = logger;
	}
	

	@Override
	public void delete() {
		String skypeFolderName = chatFolderProvider.getFolderName();
		Folder skypeFolder = mailStore.getFolder(skypeFolderName);
		Folder trash =  mailStore.getFolder("[Gmail]/Trash");
		MimeMessage mimeMessage = this.getMimeMessage();
		Message messageToRemove = mimeMessage;
		try {
			Message[] messagesToRemove = new Message[]{messageToRemove};
			skypeFolder.copyMessages(messagesToRemove, trash);
		} catch (MessagingException e) {
			logger.error("Could not transfer message to delete: " + this.getTopic());
			throw new MessageProcessingException(e);
		}
		try {
			mimeMessage.setFlag(Flags.Flag.DELETED, true);
			Message[] messages = trash.search(FolderIndex.CHAT_INDEX_SEARCH_TERM);
			if (messages.length>0)
				messages[0].setFlag(Flags.Flag.DELETED, true);
			trash.expunge();
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}
	
	
	
	private MimeMessage mimeMessage;
	private HeaderCodec headerCodec = new HeaderCodec();
	

	@Override
	public MimeMessage getMimeMessage() {
		return mimeMessage;
	}

	@Override
	public void setFrom(String chatAuthor) {
		try {
			mimeMessage.setFrom(new InternetAddress(headerCodec.encodeText(chatAuthor)));
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public void setSubject(String topic) {
		try {
			mimeMessage.setSubject(topic);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public void addRecipient(SkypeUser skypeUser) {
		try {
			String mailAddress = headerCodec.encodeText(skypeUser.getMailAddress());
			mimeMessage.addRecipient(RecipientType.TO, 
					new InternetAddress(mailAddress));
		} catch (AddressException e) {
			throw new MessageProcessingException(e);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public void setBody(String messageBody) {
		try {
			mimeMessage.setText(messageBody, "UTF-8");
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public String getChatId() {
		return getFirstHeaderOrNull(SkypeMailMessage.X_MESSAGE_ID);
	}

	@Override
	public String getBodySignature() {
		return getFirstHeaderOrNull(SkypeMailMessage.X_BODY_SIGNATURE);
	}

	@Override
	public String getTopic() {
		try {
			return this.mimeMessage.getSubject();
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public String getBody() {
		try {
			return (String) mimeMessage.getContent();
		} catch (IOException e) {
			throw new MessageProcessingException(e);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public void setChatId(String id) {
		this.setHeader(SkypeMailMessage.X_MESSAGE_ID, id);
	}

	@Override
	public void setBodySignature(String bodySignature) {
		this.setHeader(SkypeMailMessage.X_BODY_SIGNATURE, bodySignature);
	}

	@Override
	public void setMessagesSignatures(String signatures) {
		this.setHeader(SkypeMailMessage.X_MESSAGES_SIGNATURES, signatures);
	}

	@Override
	public Date getDate() {
		try {
			return mimeMessage.getSentDate();
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public String[] getMessagesSignatures() {
		String signatures = this.getFirstHeaderOrNull(X_MESSAGES_SIGNATURES);
		return signatures.split(",");
	}

	@Override
	public String[] getPosters() {
		return this.getHeader(X_SKYPE_POSTERS);
	}

	@Override
	public void addPoster(SkypeUser skypeUser) {
		this.addHeader(X_SKYPE_POSTERS, skypeUser.getPosterHeader());
	}

	@Override
	public InternetAddress[] getFrom() {
		try {
			return (InternetAddress[]) mimeMessage.getFrom();
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public String[] getRecipients(javax.mail.Message.RecipientType to) {
		try {
			ArrayList<String> decodedRecipients = new ArrayList<String>();
			InternetAddress[] recipients = (InternetAddress[]) mimeMessage.getRecipients(to);
			for (InternetAddress address : recipients) {
				decodedRecipients.add(headerCodec.decodeText(address.getAddress()));
			}
			return decodedRecipients.toArray(new String[0]);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public void setDate(String timeAsString) {
		try {
			mimeMessage.setHeader("Date", timeAsString);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	private String getFirstHeaderOrNull(String headerName) {
		String[] header = this.getHeader(headerName);
		if (header == null)
			return null;
		return header[0];
	}

	private void setHeader(String headerField, String value) {
		try {
			mimeMessage.setHeader(headerField, headerCodec.encodeText(value));
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		} 
	}

	private void addHeader(String headerField, String value) {
		try {
			mimeMessage.addHeader(headerField, headerCodec.encodeText(value));
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		} 
	}

	private String[] getHeader(String headerName) {
		try {
			String[] headers = this.mimeMessage.getHeader(headerName);
			List<String> decodedHeaders = new LinkedList<String>();
			for (String aHeader : headers) {
				decodedHeaders.add(headerCodec.decodeText(aHeader));
			}

			return decodedHeaders.toArray(new String[0]);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		} 
	}

	@Override
	public void setSentDate(Date time) {
		try {
			mimeMessage.setSentDate(time);
		} catch (MessagingException e) {
			throw new MessageProcessingException(e);
		}
	}

	@Override
	public void setCustomHeader(String headerField, String value) {
		this.setHeader(headerField, value);
	}
}