package ydh.event.service;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ydh.cicada.dao.JdbcDao;
import ydh.cicada.query.QueryObject;
import ydh.event.dict.TagStatus;
import ydh.event.dict.TagType;
import ydh.event.entity.EventTag;
import ydh.mvc.ex.WorkException;
import ydh.utils.GsonUtil;
import ydh.utils.UUIDTool;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 事件标签service
 * @author tearslee
 *
 */
@Service
public class EventTagService {
	@Autowired
	private JdbcDao dao;
	
	
	/**
	 * 添加事件标签
	 * @param tagName
	 * @param cusId
	 * @return
	 * @throws WorkException 
	 */
	@Transactional
	public void saveEventTag(String tags,int cusId){
		Type type=new TypeToken<List<EventTag>>(){}.getType();
		List<EventTag> list=null;
		list =GsonUtil.toObjectChange(tags, type);	
		Date now=new Date();
		for (int i = 0; i < list.size(); i++) {
			if(StringUtils.isNotBlank(list.get(i).getEventTagId())){
				list.get(i).setSortIndex(i);
				dao.update(list.get(i));
			}else{
				list.get(i).setEventTagId(UUIDTool.getUUID());
				list.get(i).setCusId(cusId);
				list.get(i).setSortIndex(i);
				list.get(i).setEventTagCreateDate(now);
				dao.insert(list.get(i));
			}
		}
	}
	/**
	 * 根据用户id查询事件标签(列表筛选)
	 * @return
	 */
	@Transactional
	public List<EventTag> selectByCusId(int cusId){
		List<EventTag> eventTags=QueryObject.select(EventTag.class)
				.cond("TAG_TYPE").equ(TagType.PUBLIC)
				.or(true)
				.cond("TAG_TYPE").equ(TagType.UNABLED)
				.asc("SORT_INDEX")
				.asc("EVENT_TAG_CREATE_DATE")
				.list(dao);
		eventTags.addAll(QueryObject.select(EventTag.class)
				.condition("CUS_ID=? AND TAG_STATUS=?",new Object[]{cusId, TagStatus.ALIVE})
				.asc("SORT_INDEX")
				.asc("EVENT_TAG_CREATE_DATE")
				.list(dao));
		return eventTags;
	}
	
	/**
	 * 根据名称与用户id查询事件标签
	 * @return
	 */
	@Transactional
	public List<EventTag> selectListInEvent(int cusId){
		List<EventTag> list=QueryObject.select(EventTag.class)
				.cond("TAG_TYPE").equ(TagType.PUBLIC)
				.asc("SORT_INDEX")
				.list(dao);
		if(list==null){
			list=new ArrayList<EventTag>();
		}
		if(cusId!=0){				
			list.addAll(QueryObject.select(EventTag.class).distinct(true)
					.cond("CUS_ID").equ(cusId)
					.cond("TAG_TYPE").equ(TagType.PRIVATE)
					.cond("TAG_STATUS").equ(TagStatus.ALIVE)
					.asc("SORT_INDEX")
					.list(dao));
		}
		return list;
	}
	/**
	 * 删除事件标签
	 */
	@Transactional
	public void deleteEventTag(String tags,int cusId){
		Type type=new TypeToken<List<EventTag>>(){}.getType();
		List<EventTag> list=null;
		list=GsonUtil.toObjectChange(tags, type);
		for (EventTag eventTag : list) {
			if(StringUtils.isNotBlank(eventTag.getEventTagId())){					
				dao.delete(eventTag);
			}
		}
	}
	

}
