package ydh.upload.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import ydh.cicada.service.CommonService;
import ydh.mvc.BaseResult;
import ydh.upload.entity.QuoteRelation;
import ydh.upload.entity.QuoteSourceType;
import ydh.upload.utils.FileUploader;
import ydh.upload.utils.SystemPath;
import ydh.upload.utils.UploadConfig;
import ydh.upload.utils.UploadException;
import ydh.utils.ConfigTool;
import ydh.utils.GsonUtil;
import ydh.utils.UUIDTool;
import ydh.website.localization.controller.BaseController;

@Controller
@RequestMapping("upload-file")
public class UploadFileController extends BaseController{

	@Autowired
	private CommonService commonService;

	@SuppressWarnings({ "unused", "unchecked" })
	@RequestMapping(value = "upload", method = RequestMethod.POST,produces = "text/html;charset=utf8")
	@ResponseBody
	public String upload(  ModelMap model, HttpServletRequest request) throws IOException, UploadException {
		
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
        Iterator<String> iter = multiRequest.getFileNames();
        List<QuoteRelation> relations = new ArrayList<QuoteRelation>();
        while(iter.hasNext()){
        	int pre = (int) System.currentTimeMillis();  
            //取得上传文件  
            List<MultipartFile> fileUploads = multiRequest.getFiles(iter.next());
            for (MultipartFile fileUpload : fileUploads) {
            	String filename = fileUpload.getOriginalFilename();
            	String type = filename.substring(filename.lastIndexOf(".") + 1);
            	//TODO 测试服务器路径
            	String filePath = SystemPath.getSysPath()+ConfigTool.getString(UploadConfig.templateSavePath);//ConfigTool.getString(UploadConfig.fileSavePath);
            	String fileId = UUIDTool.getUUID();
            	FileUploader.saveFileFromInputStream(fileUpload.getInputStream(), filePath, fileId, type);
            	
            	//引用关系
            	File file=new File(filePath, fileId+"."+type);
            	QuoteRelation relation=new QuoteRelation();
            	if(isImage(file)){
            		relation.setQuoteSourceType(QuoteSourceType.IMG);
            	}else{
            		relation.setQuoteSourceType(QuoteSourceType.FILE);
            	}
            	relation.setQuoteRelationId(UUIDTool.getUUID());
            	relation.setQuoteDate(new Date());
            	relation.setQuoteSourceName(filename.substring(0,filename.lastIndexOf(".")));
            	relation.setQuoteSourceId(fileId);
            	relation.setQuoteSourceSuffix(type);
            	commonService.insert(relation);
            	relations.add(relation);
			}
		}
        returnResult.setData(relations);
        return resultVal();
	}
	
	/**
	 * 判断文件是否是图片
	 * @param file
	 * @return
	 */
	private boolean isImage(File file){
		boolean flag = false;   
        try  
        {  
        	ImageInputStream iis = ImageIO.createImageInputStream(file);//resFile为需被
        	Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        	if (!iter.hasNext()) {//文件不是图片
        		iis.close();  
            	iter=null;
            	return flag;
        	}
        	iis.close();  
        	iter=null;
            flag = true; 
           
        } catch (Exception e)  
        {  
        	e.printStackTrace();
        }  
        return flag;  
	}
}


