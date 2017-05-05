package ydh.upload.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import ydh.upload.entity.UploadImg;
import ydh.utils.ConfigTool;
import ydh.utils.UUIDTool;

public class FileUploader {
	public static final int IMAGE_SMALL_WIDTH = 209;
	public static final int IMAGE_SMALL_HEIGHT = 159;
	public static final int IMAGE_BIG_WIDTH = 423;
	public static final int IMAGE_BIG_HEIGHT = 213;
	
	private static String[] SUPPORT_PHOTO_TYPES = new String[]{
		"jpg", "jpeg", "gif", "png", "bmp"
	};
	
	private static boolean isSupportPhotoType(String type) {
		for (String t : SUPPORT_PHOTO_TYPES) {
			if (type.equalsIgnoreCase(t)) {
				return true;
			}
		}
		return false;
	}
	
	public static UploadImg upload(MultipartFile file) throws UploadException {
		if (file == null) {
			throw new UploadException(UploadException.Error.NO_FILE_UPLOAD, "未选择上传文件");
		}
		if (file.getSize() > 3 * 1024 * 1024) {
			throw new UploadException(UploadException.Error.FILE_SIZE_TOO_LARGE, "文件尺寸不能超过3M");
		} else if (file.getSize() < 1) {
			throw new UploadException(UploadException.Error.FILE_UPLOAD_ERROR, "文件上传错误");
		}
		String filename = file.getOriginalFilename();
		String type = "png";
		if (!isSupportPhotoType(type)) {
			throw new UploadException(UploadException.Error.IMAGE_TYPE_ERROR, 
					"仅支持以下图片类型:"+StringUtils.join(SUPPORT_PHOTO_TYPES, "|"));
		}
		String filePath = ConfigTool.getString(UploadConfig.imageSavePath);
		String imgId = UUIDTool.getUUID();
		try {
			saveFileFromInputStream(file.getInputStream(), filePath, imgId, type);
		} catch (IOException e) {
			throw new UploadException(UploadException.Error.FILE_UPLOAD_ERROR, "文件上传错误", e);
		}
		UploadImg uploadImg = new UploadImg();
		uploadImg.setImgFilename(filename);
		uploadImg.setImgId(imgId);
		uploadImg.setImgSuffix(type);
		uploadImg.setUploadTime(new Date());
		return uploadImg;
	}

	/**
	 * 保存文件
	 * 
	 * @param stream
	 * @param path
	 * @param imgId
	 * @throws IOException
	 */
	public static void saveFileFromInputStream(InputStream stream, String path, String imgId, String type) throws IOException {
		FileOutputStream fs = null;
		try {
			File file=new File(path);
			if(!file.exists())file.mkdirs();
			fs = new FileOutputStream(path + "/" + imgId + "." + type);
			Integer fileSize = ConfigTool.getInteger(UploadConfig.fileSize);//上传文件限制大小
			byte[] buffer = new byte[fileSize];
			int byteread = 0;
			while ((byteread = stream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			fs.close();
			stream.close();
		}
	}
	
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static void saveImgBigFromServer(String filePath, String imgId,
			String type) throws IOException {
		File originalImage = new File(filePath + "/" + imgId + "." + type);
		File resizedFile = new File(filePath + "/" + imgId + "resized.jpg");
		resize(originalImage, resizedFile, 1.0f, 
				ConfigTool.getInt(UploadConfig.imageBigWidth, IMAGE_BIG_WIDTH),
				ConfigTool.getInt(UploadConfig.imageBigHeight, IMAGE_BIG_HEIGHT));
	}
	
	public static void saveImgSmallFromServer(String filePath, String imgId,
			String type) throws IOException {
		File originalImage = new File(filePath + "/" + imgId + "." + type);
		File resizedFile = new File(filePath + "/" + imgId + ".png");
		resize(originalImage, resizedFile, 1.0f,
				ConfigTool.getInt(UploadConfig.imageSmallWidth, IMAGE_SMALL_WIDTH),
				ConfigTool.getInt(UploadConfig.imageSmallHeight, IMAGE_SMALL_HEIGHT));
	}

	public static void resize(File originalFile, File resizedFile,
			float quality,int resizedWidth, int resizedHeight) throws IOException {
		Assert.isTrue(quality <= 1.0 && quality > 0);
		BufferedImage originalImage = ImageIO.read(originalFile);
		BufferedImage resizedImage = new BufferedImage(resizedWidth, resizedHeight, 
				BufferedImage.TYPE_INT_RGB);
		resizedImage.getGraphics().drawImage(  
				originalImage.getScaledInstance(resizedWidth, resizedHeight, 
						Image.SCALE_SMOOTH), 0,  0, null);
		ImageIO.write(resizedImage, "jpg", resizedFile);
	}
	
}
