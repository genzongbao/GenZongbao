package ydh.message.service;


import ydh.message.push.MessageType;

public interface MessageService<U, T extends MessageType<U>> {
	/**
	 * 发送站内消息
	 * @param cusId       接收消息的用户ID
	 * @param msgTitle    消息的标题
	 * @param msgContext  消息的内容
	 */
	public void createWebMessage(String userId, T messageType, String... args);

}
