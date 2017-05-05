package ydh.customer.service;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ydh.cicada.dao.JdbcDao;
import ydh.cicada.query.QueryObject;
import ydh.customer.entity.CusTag;
import ydh.customer.entity.CusTagMapping;
import ydh.customer.entity.Customer;
import ydh.mvc.BaseResult;
import ydh.utils.CheckTool;
import ydh.utils.GsonUtil;
import ydh.utils.LogHelper;
import ydh.utils.UUIDTool;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CusTagServiceImpl implements CusTagService {
	
	@Autowired
	private JdbcDao jdbcDao;

	@Override
	public BaseResult addCusTag(String result){
		Type type = new TypeToken<BaseResult<CusTag>>() {}.getType();
		BaseResult<CusTag> baseResult=null;
		try {			
			 baseResult = GsonUtil.toObjectChange(result, type);
			 if(baseResult!=null && baseResult.getData()!=null){				 
				 CusTag cusTag=baseResult.getData();
				 CusTag alreadyTag=QueryObject.select(CusTag.class)
						 .cond("CUS_ID").equ(baseResult.getCusId())
						 .cond("CUS_TAG_NAME").equ(cusTag.getCusTagName())
						 .firstResult(jdbcDao);
				 if(alreadyTag!=null){
					 baseResult.setMsg("已有相同的标签！保存失败");
					 baseResult.setError(true);
					 baseResult.setData(null);//清空data
					 return baseResult;
				 }
				 if(StringUtils.isBlank(cusTag.getCusTagId())){		
					
					 cusTag.setCusTagId(UUIDTool.getUUID());
					 cusTag.setCusId(baseResult.getCusId());
					 cusTag.setCreateDate(new Date());
					 jdbcDao.insert(cusTag);
				 }else{//修改名称,查询是否具有相同的标签
					 cusTag.setCusId(baseResult.getCusId());
					 cusTag.setCreateDate(new Date());
					 jdbcDao.update(cusTag);
					 baseResult.setMsg("保存成功！");
					 baseResult.setError(false);
				 }
			 }else{
				 baseResult=new BaseResult();
				 baseResult.setError(true);
				 baseResult.setMsg("保存失败！");
			 }
		} catch (Exception e) {
			LogHelper.getLogger(this.getClass()).error(e.getMessage());
		}
		
		return baseResult;
	}
	@Override
	public BaseResult deleteCusTag(String result) {
		Type type = new TypeToken<BaseResult<CusTag>>() {}.getType();
		BaseResult<CusTag> baseResult = null;
		baseResult = GsonUtil.toObjectChange(result, type);
		if (baseResult != null && baseResult.getData() != null) {
			CusTag cusTag = baseResult.getData();
			if (StringUtils.isNotBlank(cusTag.getCusTagId())) {
				List<CusTagMapping> cusList = QueryObject.select(CusTagMapping.class).cond("CUS_TAG_ID")
						.equ(cusTag.getCusTagId()).list(jdbcDao);
				if (CheckTool.checkListIsNotNull(cusList)) {
					for (CusTagMapping cusTagMapping : cusList) {
						jdbcDao.delete(cusTagMapping);
					}
				}
				jdbcDao.delete(cusTag);
				baseResult.setMsg("删除成功！");
				baseResult.setError(false);
				return baseResult;
			}
		} else {
			baseResult = new BaseResult();
			baseResult.setError(true);
			baseResult.setMsg("删除失败！");
		}
		return baseResult;
	}
	

	@Override
	public BaseResult addCusToTag(String CusTagId,String result) {
		Type type = new TypeToken<BaseResult<List<Customer>>>() {}.getType();
		BaseResult<List<Customer>> baseResult=null;
		try {			
			 baseResult = GsonUtil.toObjectChange(result, type);
			 if(baseResult!=null && baseResult.getData()!=null){
				 List<Customer> cusList=baseResult.getData();
				 for (Customer customer : cusList) {
					CusTagMapping ctm=new CusTagMapping();
					ctm.setCusTagMappingId(UUIDTool.getUUID());
					ctm.setCusTagId(CusTagId);
					ctm.setCusId((int)customer.getCusId());
					ctm.setChangeDate(new Date());
					jdbcDao.insert(ctm);
				}
				 //修改标签内人数统计
				 CusTag cusTag=this.jdbcDao.load(CusTag.class, CusTagId);
				 if(cusTag!=null){
					 String sql="select count(1) from cus_tag_mapping where cus_tag_id=?";
					 Integer countNum=this.jdbcDao.getJdbcTemplate().queryForObject(sql, new Object[]{cusTag.getCusTagId()},Integer.class);
					 cusTag.setCountCus(countNum.intValue());
					 jdbcDao.update(cusTag);
				 }
				baseResult.setError(false);
				baseResult.setData(null);//清空数据返回
				baseResult.setMsg("保存成功");
				return baseResult;
			 }else{		
				 baseResult=new BaseResult();
				 baseResult.setError(false);
				 baseResult.setMsg("保存失败");	 
			 }
			 
		} catch (Exception e) {
			e.printStackTrace();
//			LogHelper.getLogger(this.getClass()).error(e.getMessage());
		}
		
		return baseResult;
	}

	@Override
	public BaseResult getCusTags(String result) {
		Type type = new TypeToken<BaseResult>() {}.getType();
		BaseResult baseResult=GsonUtil.toObjectChange(result, type);
		try {
			if(baseResult!=null){
				List<CusTag> cusTags=QueryObject.select(CusTag.class)
						.cond("CUS_ID").equ(baseResult.getCusId())
						.asc("CREATE_DATE")
						.list(jdbcDao);
				
				baseResult.setData(cusTags);
			}
		} catch (Exception e) {
			LogHelper.getLogger(this.getClass()).error(e.getMessage());
		}
		return baseResult;
	}

	@Override
	public BaseResult<List<Customer>> getCusList(String baseResult) {
		Type type = new TypeToken<BaseResult<CusTag>>() {}.getType();
		BaseResult<CusTag> cusResult=null;
		BaseResult<List<Customer>> returnResult=null;
		try {
			cusResult=GsonUtil.toObjectChange(baseResult, type);
			if(cusResult!=null && cusResult.getData()!=null){
				List<Customer> cusList=QueryObject.select("C.*",Customer.class)
						.from("CUS_TAG_MAPPING T LEFT JOIN CUSTOMER C ON T.CUS_ID=C.CUS_ID")
						.cond("T.CUS_TAG_ID").equ(cusResult.getData().getCusTagId()).list(jdbcDao);
				returnResult=new BaseResult<List<Customer>>();
				returnResult.setData(cusList);
				returnResult.setCusId(cusResult.getCusId());
				returnResult.setError(false);
				return returnResult;
			}
			returnResult=new BaseResult<List<Customer>>();
			returnResult.setError(true);
			returnResult.setMsg("加载失败！");
		} catch (Exception e) {
			e.printStackTrace();
//			LogHelper.getLogger(this.getClass()).error(e.getStackTrace());
		}
		return returnResult;
	}
	
	
	/**
	 * TODO 需要前台改进
	 * 
	 */
	@Override
	public BaseResult signCustomer(String result) {
		//获取标签内联系人列表
		BaseResult<List<Customer>> baseResult = getCusList(result);
		List<Customer> cusList=baseResult.getData();
		try {
			if(cusList!=null){
				//获取朋友列表
				List<Customer> friends=QueryObject.select("C.*",Customer.class).from("CUSTOMER_FRIEND_MAPPING M LEFT JOIN CUSTOMER C ON M.FRIEND_ID=C.CUS_ID")
						 .cond("M.CUS_ID").equ(baseResult.getCusId()).list(jdbcDao);
					//比对
				for (Customer friend : friends) {
					 for (Customer sign : cusList) {
						 if(friend.getCusId().equals(sign.getCusId())){
							 friend.setChecked(true);
						 }
					 }
				}
				 baseResult.setData(friends);
				 return baseResult;
			}
		} catch (Exception e) {
			LogHelper.getLogger(this.getClass()).error(e.getMessage());
		}
		return baseResult;
	}
	@SuppressWarnings("deprecation")
	@Override
	public BaseResult deleteCusFromTag(String result,String cusTagId) {
		Type type = new TypeToken<BaseResult<List<Customer>>>() {}.getType();
		BaseResult<List<Customer>> baseResult = null;
		try {
			baseResult = GsonUtil.toObjectChange(result, type);
			if(baseResult!=null && baseResult.getData()!=null){
				for (Customer delCus : baseResult.getData()) {
					if(delCus!=null){
						CusTagMapping ctm=QueryObject.select(CusTagMapping.class)
								.cond("CUS_ID").equ(delCus.getCusId())
								.cond("CUS_TAG_ID").equ(cusTagId).firstResult(jdbcDao);
						if(ctm!=null){							
							this.jdbcDao.delete(ctm);
						}
					}
				}
				 //修改标签内人数统计 TODO 待完善
				 CusTag cusTag=this.jdbcDao.load(CusTag.class, cusTagId);
				 if(cusTag!=null){
					 String sql="select count(1) from cus_tag_mapping where cus_tag_id=?";
					 Integer countNum=this.jdbcDao.getJdbcTemplate().queryForObject(sql, new Object[]{cusTag.getCusTagId()},Integer.class);
					 cusTag.setCountCus(countNum.intValue());
					 jdbcDao.update(cusTag);
				 }
				baseResult.setData(null);
				baseResult.setMsg("删除成功");
			}else{
				baseResult=new BaseResult();
				baseResult.setError(true);
				baseResult.setMsg("删除失败");
			}
		} catch (Exception e) {
			LogHelper.getLogger(this.getClass()).error(e.getMessage());
		}
		return baseResult;
	}

}
