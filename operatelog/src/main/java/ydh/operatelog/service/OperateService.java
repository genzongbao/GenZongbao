package ydh.operatelog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ydh.cicada.dao.JdbcDao;
import ydh.operatelog.entity.OperateLog;
import ydh.user.entity.User;
import ydh.user.realm.UserLoginZone;

import java.util.Date;

@Service
public class OperateService {
	@Autowired
	private JdbcDao dao;
	@Transactional
	public void createOperateLog(String operateInfo){
		OperateLog operateLog = new OperateLog();
		User user = UserLoginZone.loginUser();
		operateLog.setUserId(user.getUserId());
		operateLog.setOperateDate(new Date());
		operateLog.setOperateInfo(operateInfo);
		this.dao.insert(operateLog);
	}
}
