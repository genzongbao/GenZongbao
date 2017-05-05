package ydh.message.push;

import ydh.cicada.dict.YesNo;
import ydh.utils.JPushTool;
import ydh.utils.MailSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PushMessage<U, T extends MessageType<U>> {
	private final T messageType;
	private final String userId;
	private final String[] args;
	private final String title;
	private final String content;
	
	private String messageId;
	
	private String userName;

	public PushMessage(T messageType, String userId, String messageId, String... args) {
		this.messageType = messageType;
		this.userId = userId;
		this.args = args;
		this.messageId = messageId;
		String title = this.messageType.titleTemplate();
		String content = this.messageType.contentTemplate();
		if(args.length > 0) {
			title = format(title, args);
			content = format(content, args);
		}
		this.title = title;
		this.content = content;
	}

	public PushMessage(T messageType, String userId, String userName, String messageId, String... args) {
		this.messageType = messageType;
		this.userId = userId;
		this.userName = userName;
		this.args = args;
		this.messageId = messageId;
		String title = this.messageType.titleTemplate();
		String content = this.messageType.contentTemplate();
		if(args.length > 0) {
			title = format(title, args);
			content = format(content, args);
		}
		this.title = title;
		this.content = content;
	}

	public void push(PushAddressTranslator<U, T> translator) {
		PushSetting setting = translator.loadPushSetting(this.userId, this.messageType);
		if (setting == null) {
			setting = this.messageType.defaultSetting();
		}
		U user = translator.loadUser(this.userId);
		// APP推送
		if (setting.getAllowApp() == YesNo.YES) {
			String[] appTokens = translator.addressForAndroid(user);
			if (appTokens != null && appTokens.length > 0) {
				JPushTool.push(this.title, this.content, ((Enum<?>)this.messageType).name(), this.messageId, appTokens);
			}
		}
		// 短信推送
		if (setting.getAllowSms() == YesNo.YES) {
			String[] mobiles = translator.addressForSms(user);
			if (mobiles != null && mobiles.length > 0) {
				for (String mobile : mobiles) {
					this.messageType.smsTemplate().send(mobile, this.args);
				}
			}
		}
		// 微信推送 
		if (setting.getAllowWx() == YesNo.YES) {
			String[] openIds = translator.addressForWx(user);
			if (openIds != null && openIds.length > 0) {
				for (String openId : openIds) {
					this.messageType.wxTemplate().send(openId, this.args);
				}
			}
		}
		// 邮件推送
		if (setting.getAllowEmail() == YesNo.YES) {
			String[] emailAddresses = translator.addressForEmail(user);
			if (emailAddresses != null && emailAddresses.length > 0) {
				if (this.userName == null) {
					this.userName = translator.nameOfUser(user);
				}
				for (String emailAddress : emailAddresses) {
					MailSender.sendHtmlEmail(emailAddress, 
							this.userName, this.title, this.content);
				}
			}
		}
	}

	private static String format(String template, String... args) {
		//生成匹配模式的正则表达式 
		Pattern pattern = Pattern.compile("\\{(\\d+)\\}"); 
		Matcher matcher = pattern.matcher(template); 

		StringBuffer sb = new StringBuffer(); 
		while(matcher.find()) {
			matcher.appendReplacement(sb, args[Integer.parseInt(matcher.group(1)) - 1]); 
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	public T getMessageType() {
		return messageType;
	}

	public String getUserId() {
		return userId;
	}

	public String[] getArgs() {
		return args;
	}

	public String getMessageId() {
		return messageId;
	}
	
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getUserName() {
		return userName;
	}

}
