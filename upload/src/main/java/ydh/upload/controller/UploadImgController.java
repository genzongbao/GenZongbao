package ydh.upload.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.view.RedirectView;

import ydh.cicada.query.QueryObject;
import ydh.cicada.service.CommonService;
import ydh.upload.entity.UploadImg;
import ydh.upload.utils.FileUploadMsg;
import ydh.upload.utils.FileUploader;
import ydh.upload.utils.SystemPath;
import ydh.upload.utils.UploadConfig;
import ydh.upload.utils.UploadException;
import ydh.utils.ConfigTool;
import ydh.utils.GsonUtil;
import ydh.website.localization.service.WebSiteExceptionService;

@Controller
@RequestMapping("upload-img")
public class UploadImgController {
	@Autowired
	private CommonService commonService;
	@Autowired
	private WebSiteExceptionService exceptionService;
	/**
	 * 图片文件上传
	 * @param file
	 * @param model
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public void upload(MultipartFile imageUpload, ModelMap model, HttpServletRequest request) throws IOException {
		uploadFile(imageUpload, model, request);
	}

	/**
	 * 批量上传
	 * @param imageUpload
	 * @param imageUpload2
	 * @param model
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value = "uploadBatch", method = RequestMethod.POST)
	public @ResponseBody String uploadBatchImg(MultipartHttpServletRequest request, HttpServletResponse response,ModelMap model) throws IOException {
		Iterator<String> itr = request.getFileNames();
		MultipartFile imageUpload = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<FileUploadMsg> fileList = new ArrayList<FileUploadMsg>();
		//迭代文件
		while (itr.hasNext()) {
			imageUpload = request.getFile(itr.next());
			if (imageUpload != null) {
				uploadFile(imageUpload, model, request);
			}
			FileUploadMsg fileMsg = new FileUploadMsg();
			fileMsg.setName(imageUpload.getName());
			fileMsg.setSuccess(model.get("success").toString());
			fileMsg.setMsg(model.get("msg").toString());
			fileMsg.setUrl(model.get("obj").toString());
			fileList.add(fileMsg);
		}
		map.put("files", fileList);
		return GsonUtil.toJSONString(map);
	}
	/**
	 * 图片文件上传
	 * @param imageUpload
	 * @param model
	 * @param request
	 * @throws IOException
	 */
	private void uploadFile(MultipartFile imageUpload, ModelMap model,  HttpServletRequest request) throws IOException {
		try {
			String imageUrlPrefix = ConfigTool.getString(UploadConfig.imageUrlPrefix);
			UploadImg uploadImg = FileUploader.upload(imageUpload);
			commonService.insert(uploadImg);
			model.addAttribute("success", true);
			model.addAttribute("msg", uploadImg.getImgId());
			model.addAttribute("obj", imageUrlPrefix +"/"+uploadImg.getImgId()+"."+uploadImg.getImgSuffix());
		} catch (UploadException e) {
			exceptionService.createWebSiteException("图片文件上传异常", e);
			model.addAttribute("success", false);
			model.addAttribute("msg", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param imgId
	 * @return
	 */
	@RequestMapping("show-img")
	public RedirectView showImg(String imgId){
		UploadImg uploadImg = QueryObject.select(UploadImg.class).cond("IMG_ID").equ(imgId).firstResult(commonService);
		return new RedirectView("http://qianlai6.com/uploadImg/"+uploadImg.getImgId()+"."+uploadImg.getImgSuffix());
	}
}
