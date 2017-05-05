package ydh.operatelog.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryOperator;
import ydh.cicada.query.api.QueryParam;

import java.util.Date;

@Query(select=" OP.*,US.* ",
		from=" OPERATE_LOG OP LEFT JOIN USERS US ON OP.USER_ID=US.USER_ID ",
		orderBy =" OP.OPERATE_DATE DESC"
		)
public class OperateLogQuery extends PagableQueryCmd {
	/** 日志ID */
	@QueryParam(fieldName="OP.OPERATE_LOG_ID")
	private Integer operateLogId;
	
	/** 用户ID */
	@QueryParam(fieldName = "OP.USER_ID")
	private Integer userId;

	/** 时间 */
	@QueryParam(fieldName="OP.OPERATE_DATE",op = QueryOperator.GT_EQU)
	private Date minOperateDate;
	
	/** 时间 */
	@QueryParam(fieldName="OP.OPERATE_DATE",op = QueryOperator.LESS_EQU)
	private Date maxOperateDate;
	
	/** 信息 */
	@QueryParam(fieldName="OP.OPERATE_INFO" ,op=QueryOperator.LIKE)
	private String operateInfo;
	
	/** 姓名 */
	@QueryParam(fieldName="US.REAL_NAME" ,op=QueryOperator.LIKE)
	private String realName;

	public Integer getOperateLogId() {
		return operateLogId;
	}

	public void setOperateLogId(Integer operateLogId) {
		this.operateLogId = operateLogId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getOperateInfo() {
		return operateInfo;
	}

	public void setOperateInfo(String operateInfo) {
		this.operateInfo = operateInfo;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Date getMinOperateDate() {
		return minOperateDate;
	}

	public void setMinOperateDate(Date minOperateDate) {
		this.minOperateDate = minOperateDate;
	}

	public Date getMaxOperateDate() {
		return maxOperateDate;
	}

	public void setMaxOperateDate(Date maxOperateDate) {
		this.maxOperateDate = maxOperateDate;
	}
}
