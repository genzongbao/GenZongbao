package ydh.upload.entity;

import java.util.Date;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

/**
 * 图片上传记录
 * @author LXL
 *
 */
@Entity(name = "UPLOAD_IMG")
public class UploadImg {
	
	/** 图片ID*/
	@Id
	@Column(name = "IMG_ID")
	private String imgId;

	/** 图片原始文件名*/
	@Column(name = "IMG_FILENAME")
	private String imgFilename;

	/** 上传时间*/
	@Column(name = "UPLOAD_TIME")
	private Date uploadTime;

	/** 文件类型(后缀名)*/
	@Column(name = "IMG_SUFFIX")
	private String imgSuffix;

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getImgFilename() {
		return imgFilename;
	}

	public void setImgFilename(String imgFilename) {
		this.imgFilename = imgFilename;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getImgSuffix() {
		return imgSuffix;
	}
	
	public void setImgSuffix(String imgSuffix) {
		this.imgSuffix = imgSuffix;
	}
}
