package ydh.event.service;


import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ydh.cicada.dao.JdbcDao;
import ydh.cicada.dict.YesNo;
import ydh.event.entity.*;
import ydh.mvc.BaseResult;
import ydh.mvc.ErrorCode;
import ydh.upload.entity.QuoteRelation;
import ydh.upload.entity.QuoteSourceType;
import ydh.upload.entity.SourceType;
import ydh.upload.utils.FileTool;
import ydh.upload.utils.SystemPath;
import ydh.upload.utils.UploadConfig;
import ydh.utils.CheckTool;
import ydh.utils.ConfigTool;
import ydh.utils.GsonUtil;
import ydh.utils.UUIDTool;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service("editEventService")
@Transactional
public class EditEventService {

	@Autowired
	private JdbcDao dao;
	
	/**
	 * 保存事件基本信息
	 * @param result
	 * @return
	 */
	public BaseResult editBaseEvent(String result){
		Type type=new TypeToken<BaseResult<Event>>(){}.getType();
		BaseResult<Event> baseResult=null;
		baseResult=GsonUtil.toObjectChange(result, type);
		if(baseResult!=null && baseResult.getData()!=null){
			Event event=baseResult.getData();
			event.setEventLastChangeDate(new Date());
			this.dao.update(event);
		}
		baseResult.setData(null);
		baseResult.setMsg("保存成功");
		return baseResult;
	}
	
	/**
	 * 修改引用关系
	 * @param result
	 * @param eventId
	 * @return
	 */
	public BaseResult<List<QuoteRelation>> editQuoteRelation(String result,String eventId){
		Type type=new TypeToken<BaseResult<List<QuoteRelation>>>(){}.getType();
		BaseResult<List<QuoteRelation>> baseResult=null;
		try {
			baseResult=GsonUtil.toObjectChange(result, type);
			if(baseResult!=null && baseResult.getData()!=null){
				for (QuoteRelation qRelation : baseResult.getData()) {
					if(StringUtils.isBlank(qRelation.getSourceId()) && StringUtils.isNotBlank(qRelation.getQuoteRelationId())){
						qRelation.setSourceId(eventId);
						qRelation.setSourceType(SourceType.EVENT);
						this.dao.update(qRelation);
					}
				}
				baseResult.setError(false);
				baseResult.setData(null);
				baseResult.setMsg("保存成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			baseResult.setError(true);
			baseResult.setErrorCode(ErrorCode.SERVER_EXCEPTION);
		}
		return baseResult;
	}
	/**
	 * 单个删除
	 * @param result
	 * @return
	 */
	public BaseResult deleteQuoteRelation(String quoteId){
			BaseResult baseResult=new BaseResult<>();
			//删除关联
			QuoteRelation relation=this.dao.load(QuoteRelation.class,quoteId);
			//删除文件
			try {
				if(!relation.getQuoteSourceType().equals(QuoteSourceType.EVENT)){
					String path=SystemPath.getSysPath()+ConfigTool.getString(UploadConfig.templateSavePath)
					+relation.getQuoteSourceId()+"."+relation.getQuoteSourceSuffix();
					FileTool.delFile(path);
				}
				this.dao.delete(relation);
			} catch (Exception e) {
				e.printStackTrace();
				baseResult.setError(true);
				baseResult.setMsg("删除失败");
			}
			baseResult.setMsg("删除成功");
		return baseResult;
	}
	/**
	 * 修改事件标签关系
	 * @param result
	 * @param eventId
	 * @return
	 */
	public BaseResult editEventTagMapping(String result,String eventId){
		Type type=new TypeToken<BaseResult<List<EventTag>>>(){}.getType();
		BaseResult<List<EventTag>> baseResult=null;
		baseResult=GsonUtil.toObjectChange(result, type);
		String deleteSql="delete m.* from EVENT_TAG_MAPPING M "
				+ " LEFT JOIN EVENT_TAG T ON T.EVENT_TAG_ID=M.EVENT_TAG_ID"
				+ " where M.EVENT_ID=?";
		this.dao.getJdbcTemplate().update(deleteSql,new Object[]{eventId});
		Date now =new Date();
		if(CheckTool.checkListIsNotNull(baseResult.getData())){
			for (EventTag tag : baseResult.getData()) {
				EventTagMapping mapping=new EventTagMapping();
				mapping.setEventId(eventId);
				mapping.setEventTagMappingId(UUIDTool.getUUID());
				mapping.setEventTagId(tag.getEventTagId());
				mapping.setChangeDate(now);
				this.dao.insert(mapping);
			}
			baseResult.setData(null);
			baseResult.setMsg("保存成功");
		}else{
			//添加与“未分类”标签的关联
			EventTagMapping etm2=new EventTagMapping();
			etm2.setChangeDate(now);
			etm2.setEventId(eventId);
			etm2.setEventTagId("1");//TODO 待修改，id=1的标签为“未分类”标签
			etm2.setEventTagMappingId(UUIDTool.getUUID());
			this.dao.insert(etm2);
		}
		//添加与“全部”标签的关联
		EventTagMapping etm1=new EventTagMapping();
		etm1.setChangeDate(now);
		etm1.setEventId(eventId);
		etm1.setEventTagId("0");//TODO 待修改，id=0的标签为“全部”标签
		etm1.setEventTagMappingId(UUIDTool.getUUID());
		this.dao.insert(etm1);
		return baseResult;
	}
	/**
	 * 修改参与人员
	 * @param result
	 * @param eventId
	 * @return
	 */
	public BaseResult editCusMapping(String result,String eventId){
		Type type=new TypeToken<BaseResult<List<EventCusMapping>>>(){}.getType();
		BaseResult<List<EventCusMapping>> baseResult=null;
		baseResult=GsonUtil.toObjectChange(result, type);
		if(baseResult!=null && CheckTool.checkListIsNotNull(baseResult.getData())){
			String deleteSql="delete from EVENT_CUS_MAPPING where EVENT_ID=?";
			this.dao.getJdbcTemplate().update(deleteSql,new Object[]{eventId});
			Date now=new Date();
			for (EventCusMapping mapping : baseResult.getData()) {
				mapping.setEventCusMappingId(UUIDTool.getUUID());
				mapping.setEventId(eventId);
				mapping.setChangeDate(now);
				this.dao.insert(mapping);
			}
			baseResult.setData(null);
			baseResult.setMsg("保存成功");
		}
		return baseResult;
	}
	
	/**
	 * 新增事件条件
	 * @param result
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseResult editConditions(String result,String eventId){
		Type type=new TypeToken<BaseResult<List<EventChangeCondition>>>(){}.getType();
		BaseResult<List<EventChangeCondition>> baseResult=GsonUtil.toObjectChange(result, type);
		if(baseResult!=null && baseResult.getData()!=null){
			Date now=new Date();
			Iterator<EventChangeCondition> iterator=baseResult.getData().iterator();
			List<EventChangeCondition> returnConditions=new ArrayList<EventChangeCondition>();
			while(iterator.hasNext()){
				EventChangeCondition condition=iterator.next();
				switch (condition.getActionType()) {
				case INSERT:
					condition.setEventChangeConditionId(UUIDTool.getUUID());
					condition.setConditionCreateDate(now);
					this.dao.insert(condition);
					returnConditions.add(condition);
					break;
				case UPDATE:
					if(StringUtils.isBlank(condition.getEventChangeConditionId())){
						condition.setEventChangeConditionId(UUIDTool.getUUID());
						condition.setConditionCreateDate(now);
						this.dao.insert(condition);
					}else{
						condition.setIsFulfil(YesNo.NO);//TODO 若为已盘点的条件，修改后则直接修改为未盘点，隐患：条件可能永远不触发
						this.dao.update(condition);
					}
					returnConditions.add(condition);
					break;
				default:
					if(StringUtils.isNotBlank(condition.getEventChangeConditionId())){
						this.dao.delete(condition);
						iterator.remove();
					}
					break;
				}
			}
			baseResult.setData(returnConditions);
			baseResult.setMsg("保存成功");
			return baseResult;
		}
		baseResult=new BaseResult();
		baseResult.setMsg("保存失败！");
		baseResult.setError(true);
		return baseResult;
	}
	
}
