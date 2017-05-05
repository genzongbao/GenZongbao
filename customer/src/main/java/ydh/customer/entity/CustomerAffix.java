package ydh.customer.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

import java.util.Date;

@Entity(name = "CUSTOMER_AFFIX")
public class CustomerAffix {
	/** 附件类型-模板*/
	public static final Integer AFFIX_TEMPLATE = 1;
	/** 附件类型-客户上传的 */
	public static final Integer AFFIX_CUSTOMER = 2;
	/**附件id*/
	@Id
	@Column(name = "AFFIX_ID")
	private String affixId;
	/**客户id*/
	@Column(name = "CUS_ID")
	private Integer cusId;
	/**文件id*/
	@Column(name = "FILE_ID")
	private String fileId;
	/**文档名*/
	@Column(name = "PROJECT_NAME")
	private String projectName;
	
	/**上传附件类型*/
	@Column(name = "AFFIX_TYPE_ID")
	private Integer affixTypeId;
	
	/**上传文档时间*/
	private Date uploadTime;
	
	/**文档后缀*/
	private String fileSuffix;
	
	public Integer getCusId() {
		return cusId;
	}
	public void setCusId(Integer cusId) {
		this.cusId = cusId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getAffixId() {
		return affixId;
	}
	public void setAffixId(String affixId) {
		this.affixId = affixId;
	}
	public Integer getAffixTypeId() {
		return affixTypeId;
	}
	public void setAffixTypeId(Integer affixTypeId) {
		this.affixTypeId = affixTypeId;
	}
	public String getFileSuffix() {
		return fileSuffix;
	}
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
	
}
