package ydh.upload.entity;

import java.util.Date;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

/**
 * 文件上传记录
 * @author LXL
 *
 */
@Entity(name = "UPLOAD_FILE")
public class UploadFile {
	
	/** 文件ID*/
	@Id
	@Column(name = "FILE_ID")
	private String fileId;

	/** 原始文件名*/
	@Column(name = "FILE_NAME")
	private String fileName;

	/** 上传时间*/
	@Column(name = "UPLOAD_TIME")
	private Date uploadTime;

	/** 文件类型(后缀名)*/
	@Column(name = "FILE_SUFFIX")
	private String fileSuffix;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
	
}
