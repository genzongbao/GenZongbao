package ydh.event.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ydh.cicada.dao.BaseDao;
import ydh.cicada.query.QueryObject;
import ydh.event.entity.EventTagMapping;

@Service("eventTagMappingService1")
public class EventTagMappingService {
	
	@Autowired
	private BaseDao dao;

	@Transactional
	public List<EventTagMapping> getList(){
		
		
		return null;
	}
}
