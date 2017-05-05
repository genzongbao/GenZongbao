package ydh.message.push;

import ydh.cicada.dict.YesNo;
import ydh.utils.ApplePushTool;
import ydh.utils.JPushTool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BroadcastMessage<U, T extends MessageType<U>> {
	private final MessageType<U> messageType;
	private final String title;
	private final String content;
	
	private String messageId;
	
	public BroadcastMessage(MessageType<U> messageType, String messageId, String... args) {
		this.messageType = messageType;
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

	public void broadcast(final PushAddressTranslator<U, T> translator) {
		PushSetting setting = this.messageType.defaultSetting();
		// APP推送
		if (setting.getAllowApp() == YesNo.YES) {
			PushExcecutor.submit(new Runnable() {
				@Override
				public void run() {
					JPushTool.broadcast(title, content, messageType.messageType(), messageId);
				}
			});
			final String[] iosTokens = translator.addressForIos();//所有ios用户的Tokens
			PushExcecutor.submit(new Runnable() {
				@Override
				public void run() {
					ApplePushTool.push(title, messageType.messageType(), messageId, iosTokens);
				}
			});
		}
		// 短信、微信、邮件不支持广播
	}
	
	private static String format(String template, String[] args) {
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
	
}