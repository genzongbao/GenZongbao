package ydh.customer.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryOperator;
import ydh.cicada.query.api.QueryParam;
import ydh.message.dict.MessageState;

import java.util.Date;

@Query( from="CUSTOMER_MESSAGES CM", orderBy="CM.IS_READ,CM.RECEIVE_TIME DESC")
public class CustomerMessageQuery extends PagableQueryCmd{

	/** 消息ID*/
	
	@QueryParam(fieldName="CM.MSG_ID",op = QueryOperator.EQU)
	private Integer msgId;

	/** 消息标题*/
	@QueryParam(fieldName = "CM.MSG_TITLE", op = QueryOperator.LIKE)
	private String msgTitle;

	/** 客户ID*/
	@QueryParam(fieldName="CM.CUS_ID",op = QueryOperator.EQU)
	private Integer cusId;

	/** 接收时间*/
	@QueryParam(fieldName = "CM.RECEIVE_TIME")
	private Date receiveTime;
	
	/** 开始日期（大于） */
	@QueryParam(fieldName = "CM.RECEIVE_TIME", op = QueryOperator.GT_EQU)
	private Date minReceiveTime;
	
	/** 最后日期（小于） */
	@QueryParam(fieldName = "CM.RECEIVE_TIME", op = QueryOperator.LESS_EQU)
	private Date maxReceiveTime;

	/** 是否阅读*/
	@QueryParam(fieldName="CM.IS_READ")
	private MessageState[] isRead;

	@QueryParam(fieldName="CM.IS_READ", op = QueryOperator.NOT_EQU)
	private MessageState isReadNoEQ;

	public MessageState getIsReadNoEQ() {
		return isReadNoEQ;
	}

	public void setIsReadNoEQ(MessageState isReadNoEQ) {
		this.isReadNoEQ = isReadNoEQ;
	}

	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public Integer getCusId() {
		return cusId;
	}

	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Date getMinReceiveTime() {
		return minReceiveTime;
	}

	public void setMinReceiveTime(Date minReceiveTime) {
		this.minReceiveTime = minReceiveTime;
	}

	public Date getMaxReceiveTime() {
		return maxReceiveTime;
	}

	public void setMaxReceiveTime(Date maxReceiveTime) {
		this.maxReceiveTime = maxReceiveTime;
	}

	public MessageState[] getIsRead() {
		return isRead;
	}

	public void setIsRead(MessageState... isRead) {
		if(isRead.length<1){
			this.isRead=null;
		}else{
			this.isRead = isRead;
		}
	}
}
