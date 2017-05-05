package ydh.upload.utils;

/**
 * 多图上传后要返回到前台的参数模板
 * @author lxl
 */
public class FileUploadMsg {

	/** 图片名称  */
	private String name;
	/** 图片小图路径 */
	private String url;
	/** 图片上传状态 */
	private String success;
	/** 图片上传消息 */
	private String msg;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
