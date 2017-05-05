package ydh.upload.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ydh.cicada.dao.JdbcDao;
import ydh.cicada.query.QueryObject;
import ydh.upload.entity.QuoteRelation;
import ydh.upload.utils.FileTool;
import ydh.upload.utils.SystemPath;
import ydh.upload.utils.UploadConfig;
import ydh.utils.ConfigTool;

@Service("uploadFileService")
@Transactional
public class UploadFileService {

	@Autowired
	private JdbcDao dao;
	
	/**
	 * 删除未关联的文件
	 */
	public void deleteLoseSource(){
		List<QuoteRelation> relations=QueryObject.select(QuoteRelation.class).cond("SOURCE_ID").isNull().list(dao);
		if(relations!=null){
			for (QuoteRelation quoteRelation : relations) {
				String path=SystemPath.getSysPath()+ConfigTool.getString(UploadConfig.templateSavePath)
				+quoteRelation.getQuoteSourceId()+"."+quoteRelation.getQuoteSourceSuffix();
				try {					
					FileTool.delFile(path);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		this.dao.getJdbcTemplate().update("delete from QUOTE_RELATION where SOURCE_ID='' or ISNULL(SOURCE_ID)");
			
		}
	}
}
