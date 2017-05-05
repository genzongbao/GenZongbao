package ydh.news.web.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryOperator;
import ydh.cicada.query.api.QueryParam;
/**
 * 常见问题表查询
 * @author 李永炳
 *
 */
@Query(from="MESSAGE_BOARD")
public class MessageBoardQuery extends PagableQueryCmd {
	/**问题*/
	@QueryParam(fieldName="MESSAGE_BOARD_ASK",op=QueryOperator.LIKE)
	private String messageBoardAsk;
	/**答案*/
	@QueryParam(fieldName="MESSAGE_BOARD_ANSWER",op=QueryOperator.LIKE)
	private String messageBoardAnswer;

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
