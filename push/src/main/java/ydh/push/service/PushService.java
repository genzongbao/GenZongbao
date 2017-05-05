package ydh.push.service;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ydh.cicada.dao.JdbcDao;
import ydh.cicada.query.QueryObject;
import ydh.message.push.PushMessage;
import ydh.mvc.BaseResult;
import ydh.mvc.ErrorCode;
import ydh.push.entity.PushSetting;
import ydh.utils.GsonUtil;
import ydh.utils.JPushTool;
import ydh.utils.UUIDTool;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly=true)
public class PushService {
	
	@Autowired
	private JdbcDao dao;
	
	/**
	 * 修改或者添加推送设置
	 * @param result
	 * @return
	 */
	@Transactional(readOnly=false)
	public BaseResult addPushSetting(String result){
		Type type=new TypeToken<List<PushSetting>>(){}.getType();
		BaseResult<PushSetting> baseResult=GsonUtil.toObjectChange(result, type);
		if(baseResult!=null && baseResult.getData()!=null){
			PushSetting setting = baseResult.getData();
			if(StringUtils.isNotBlank(setting.getPushSettingId())){
				this.dao.update(setting);
			}else{
				setting.setPushSettingId(UUIDTool.getUUID());
				this.dao.insert(setting);
			}
			baseResult.setMsg("保存成功");
			return baseResult;
		}
		baseResult.setError(true);
		baseResult.setMsg("保存失败");
		baseResult.setData(null);
		return baseResult;
	}
	/**
	 * 查询用户设置
	 * @param cusId
	 * @return
	 */
	public BaseResult selectSetting(int cusId){
		BaseResult<PushSetting> baseResult=new BaseResult<PushSetting>();
		if(cusId!=0){
			baseResult.setData(QueryObject.select(PushSetting.class)
					.cond("CUS_ID").equ(cusId)
					.firstResult(dao));
		}else{
			baseResult.setError(true);
			baseResult.setMsg("该用户不存在");
		}
		return baseResult;
	}
	/**
	 * 查询消息列表
	 * @param messageType
	 * @return
	 */
	public BaseResult selectMessageList(int cusId){
		BaseResult<List<PushMessage>> baseResult=new BaseResult<List<PushMessage>>();
		if(cusId!=0){
			baseResult.setData(QueryObject.select("P.*,M.READ_FLAG as readFlag",PushMessage.class)
					.distinct(true)
					.from("PUSH_MESSAGE P LEFT JOIN CUS_MESSAGE_MAPPING M ON M.MESSAGE_ID=P.PUSH_MESSAGE_ID")
					.cond("M.CUS_ID").equ(cusId)
					.list(dao));
		}else{
			baseResult.setError(true);
			baseResult.setMsg("该用户不存在");
		}
		return baseResult;
	}
	
	/**
	 * 更新事件状态为“已读”
	 * @param cusId 更新的用户id
	 * @param eventId
	 */
	public void changeMessageReadFlag(int cusId,String cusMessageMappingId){
		
	}
	public static void main(String[] args) {
		Map<String,String> map=new HashMap<>();
		map.put("msg", "您的账号在其它地方登录，您被迫下线！");
		map.put("errorCode", ErrorCode.NO_LOGIN.toString());
		map.put("error", "true");
		List<String> alias=new ArrayList<String>();
		alias.add("15281057301");
		JPushTool.pushAliasMessageGzb("警告", "账号强制下线", map, "", alias);
	}
}
