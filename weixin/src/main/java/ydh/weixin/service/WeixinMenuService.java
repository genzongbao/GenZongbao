package ydh.weixin.service;

import org.springframework.stereotype.Service;

@Service
public class WeixinMenuService {
	
//	/**
//	 * 创建菜单
//	 * @param accessToken
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unchecked")
//	public void createMenu(String accessToken) throws Exception{
//		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
//		Map<String,Object> params = new HashMap<String,Object>();
//		List<Map<String,Object>> button = new ArrayList<Map<String,Object>>();
//		
//		Map<String, Object> param1 = new HashMap<String, Object>();
//		param1.put("type", "view");
//		param1.put("name", "待办");
//		param1.put("url", ConfigTool.getString("ruitwj.path")+"/weixin/user-conter/wait-manage");
//		button.add(param1);
//		System.out.println(ConfigTool.getString("ruitwj.path"));
//		Map<String, Object> param2 = new HashMap<String, Object>();
//		param2.put("type", "view");
//		param2.put("name", "经办");
//		param2.put("url", ConfigTool.getString("ruitwj.path")+"/weixin/user-conter/agency-manage");
//		button.add(param2);
//		
//		Map<String,Object> sub_button = createMenuData();
//		button.add(sub_button);
//		params.put("button", button);
//		
//		String requestResult = WeixinUtil.sendPost(url, GsonUtil.toJson(params));
//		Map<String, String> map =GsonUtil.toObject(requestResult, Map.class);
//		String errcode = String.valueOf(map.get("errcode"));
//		if(!errcode.equals("0.0")){
//			throw new Exception(map.get("errmsg"));
//		}
//		System.out.println(JsonUtil.toString(map));
//	}
//	
//	/**
//	 * 组装小区管理菜单
//	 * @return
//	 */
//	public Map<String,Object> createMenuData(){
//		Map<String,Object> param = new HashMap<String,Object>();
//		List<Map<String,String>> subButton = new ArrayList<Map<String,String>>();
//		param.put("name", "小区管理");
//		Map<String,String> param1 = new HashMap<String,String>();
//		param1.put("type", "view");
//		param1.put("name", "工单查询");
//		param1.put("url", ConfigTool.getString("ruitwj.path")+"/weixin/company/task-demand");
//		subButton.add(param1);
//		Map<String,String> param2 = new HashMap<String,String>();
//		param2.put("type", "view");
//		param2.put("name", "住户查询");
//		param2.put("url", ConfigTool.getString("ruitwj.path")+"/weixin/company/customer-demand");
//		subButton.add(param2);
//		Map<String,String> param3 = new HashMap<String,String>();
//		param3.put("type", "view");
//		param3.put("name", "报事报修");
//		param3.put("url", ConfigTool.getString("ruitwj.path")+"/weixin/company/repair-apply");
//		subButton.add(param3);
//		Map<String,String> param4 = new HashMap<String,String>();
//		param4.put("type", "view");
//		param4.put("name", "投诉建议");
//		param4.put("url", ConfigTool.getString("ruitwj.path")+"/weixin/company/feed-back");
//		subButton.add(param4);
//		Map<String,String> param5 = new HashMap<String,String>();
//		param5.put("type", "view");
//		param5.put("name", "绑定/解绑");
//		param5.put("url", ConfigTool.getString("ruitwj.path")+"/weixin/weixin-bind-page");
//		subButton.add(param5);
//		param.put("sub_button", subButton);
//		return param;
//	}
//	
//	/**
//	 * 删除自定义菜单
//	 * @param accessToken
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unchecked")
//	public void deleteConditionalMenu(String accessToken) throws Exception{
//		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete";
//		String requestResult = WeixinUtil.sendGet(url, "access_token=" + accessToken);
//		Map<String, String> map =GsonUtil.toObject(requestResult, Map.class);
//		String errcode = String.valueOf(map.get("errcode"));
//		if(!errcode.equals("0.0")){
//			throw new Exception(map.get("errmsg"));
//		}
//	}
//	
//	/**
//	 * 测试个性化菜单匹配结果
//	 * @param accessToken
//	 * @param openId
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unchecked")
//	public void tryMatchMenu(String accessToken,String openId) throws Exception{
//		String url = "https://api.weixin.qq.com/cgi-bin/menu/trymatch?access_token="+accessToken;
//		Map<String,Object> param = new HashMap<String,Object>();
//		param.put("user_id", openId);
//		String requestResult = WeixinUtil.sendPost(url, GsonUtil.toJson(param));
//		Map<String, String> map =GsonUtil.toObject(requestResult, Map.class);
//		if(map.containsKey("errcode")){
//			throw new Exception(map.get("errmsg"));
//		}
//	}
//	
//	/**
//	 * 查询已创建的个性菜单
//	 * 返回匹配菜单条件tagId集合
//	 * @param accessToken
//	 */
//	@SuppressWarnings("unchecked")
//	public void getConditionalMenu(String accessToken){
//		String url = "https://api.weixin.qq.com/cgi-bin/menu/get";
//		String requestResult = WeixinUtil.sendGet(url, "access_token=" + accessToken);
//		Map<String, String> map =GsonUtil.toObject(requestResult, Map.class);
//		System.out.println(JsonUtil.toString(map));
//	}
}
