package ydh.upload.utils;


public class UploadException extends Exception {
	private static final long serialVersionUID = 1488299955762200642L;

	public static enum Error {
		NO_FILE_UPLOAD, 
		FILE_SIZE_TOO_LARGE, 
		FILE_UPLOAD_ERROR,
		IMAGE_TYPE_ERROR
	}
	
	private final Error error;
	
	public UploadException(Error error) {
		this.error = error;
	}
	
	public UploadException(Error error, Throwable cause) {
		super(cause);
		this.error = error;
	}
	
	public UploadException(Error error, String message) {
		super(message);
		this.error = error;
	}
	
	public UploadException(Error error, String message, Throwable cause) {
		super(message, cause);
		this.error = error;
	}
	
	public Error getError() {
		return this.error;
	}
}
