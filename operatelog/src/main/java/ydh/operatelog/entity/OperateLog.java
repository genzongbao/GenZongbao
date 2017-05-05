package ydh.operatelog.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

import java.util.Date;

@Entity(name="OPERATE_LOG")
public class OperateLog {
	/** 日志ID */
	@Id
	@Column(name="OPERATE_LOG_ID")
	private Integer operateLogId;
	
	/** 用户ID */
	@Column(name = "USER_ID")
	private Integer userId;

	/** 时间 */
	@Column(name="OPERATE_DATE")
	private Date operateDate;
	
	/** 信息 */
	@Column(name="OPERATE_INFO")
	private String operateInfo;
	
	/** 姓名 */
	private String realName;
	public OperateLog() {
	}

	public Integer getOperateLogId() {
		return operateLogId;
	}

	public void setOperateLogId(Integer operateLogId) {
		this.operateLogId = operateLogId;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getOperateInfo() {
		return operateInfo;
	}

	public void setOperateInfo(String operateInfo) {
		this.operateInfo = operateInfo;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
}
