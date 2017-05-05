package ydh.news.web.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
/**
 * 常见问题表
 * @author 李永炳
 *
 */
@Entity(name = "MESSAGE_BOARD")
public class MessageBoard {
	/**id*/
	@Id(autoincrement=true)
	@Column(name="MESSAGE_BOARD_ID")
	private Integer messageBoardId;	
	/**问题*/
	@Column(name="MESSAGE_BOARD_ASK")
	private String messageBoardAsk;
	/**答案*/
	@Column(name="MESSAGE_BOARD_ANSWER")
	private String messageBoardAnswer;
	
	public Integer getMessageBoardId() {
		return messageBoardId;
	}
	public void setMessageBoardId(Integer messageBoardId) {
		this.messageBoardId = messageBoardId;
	}
	public String getMessageBoardAsk() {
		return messageBoardAsk;
	}
	public void setMessageBoardAsk(String messageBoardAsk) {
		this.messageBoardAsk = messageBoardAsk;
	}
	public String getMessageBoardAnswer() {
		return messageBoardAnswer;
	}
	public void setMessageBoardAnswer(String messageBoardAnswer) {
		this.messageBoardAnswer = messageBoardAnswer;
	}
	
}
