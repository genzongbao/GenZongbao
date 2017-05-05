package ydh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ydh.cicada.dao.JdbcDao;
import ydh.event.dict.TagStatus;
import ydh.event.dict.TagType;
import ydh.event.entity.EventTag;
import ydh.utils.UUIDTool;

import java.util.Date;

/**
 * @auther 冯冰
 * @email fengbing8206320@sohu.com
 * @weixin f1908951579
 * @date 2017/5/5
 * @doc 类说明：
 */
@Service
public class InitCustomerService {

    @Autowired
    private JdbcDao jdbcDao;

    /**
     * 初始化用户数据  2017年4月27日13:36:41
     * @param cusId
     */
    public void initCustomerData(int cusId){
        //创建默认标签
        Date now=new Date();
        EventTag tag1=new EventTag();
        tag1.setCusId(cusId);
        tag1.setEventTagCreateDate(now);
        tag1.setEventTagId(UUIDTool.getUUID());
        tag1.setEventTagName("工作事件");
        tag1.setTagStatus(TagStatus.ALIVE);
        tag1.setTagType(TagType.PRIVATE);
        this.jdbcDao.insert(tag1);
        tag1.setEventTagId(UUIDTool.getUUID());
        tag1.setEventTagName("生活事件");
        this.jdbcDao.insert(tag1);
        tag1.setEventTagId(UUIDTool.getUUID());
        tag1.setEventTagName("学习事件");
        this.jdbcDao.insert(tag1);
    }
}
