package ydh.utils;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	public static final String smtpServer="email.smtpServer";
	public static final String name="email.name";
	public static final String address="email.address";
	public static final String password="email.password";
	public static final String smtpPort="email.smtpPort";
	public static final String smtpUseSsl="email.smtpUseSsl";
	/**
	 * 发送邮件
	 * @param toAddr  收件人地址
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @param attachs 附件（可变参数，必须是偶数个，第一个为附件文件地址，第二个参数为附件名。多个附件继续累加）
	 * @return 发送成功或失败
	 * @throws MessagingException 
	 */
	public static boolean sendHtmlEmail(String toAddr, String subject, String content, String ... attachs){
		assert(attachs == null || attachs.length % 2 == 0);
		try {
			Properties props = new Properties();  
			// 开启debug调试  
			props.setProperty("mail.debug", "true");  
			// 发送服务器需要身份验证  
			props.setProperty("mail.smtp.auth", "true");  
			// 设置邮件服务器主机名  
			props.setProperty("mail.host", ConfigTool.getString(smtpServer));  
			// 发送邮件协议名称  
			props.setProperty("mail.transport.protocol", "smtp");  
			// 设置环境信息  
			Session session = Session.getInstance(props);  
			// 创建邮件对象  
			Message msg = new MimeMessage(session);  
			msg.setSubject(subject);
			// 设置邮件内容  
			msg.setText(content);  
			// 设置发件人  
			msg.setFrom(new InternetAddress(ConfigTool.getString(address)));  
			Transport transport = session.getTransport();  
			// 连接邮件服务器  
			transport.connect( ConfigTool.getString(name),  ConfigTool.getString(password));  
			// 发送邮件  
			transport.sendMessage(msg, new Address[] {new InternetAddress(toAddr)});  
			// 关闭连接  
			transport.close();  
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return true;
	}

}
