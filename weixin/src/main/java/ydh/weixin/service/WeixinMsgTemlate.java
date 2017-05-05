package ydh.weixin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ydh.cicada.service.CommonService;

@Service
public class WeixinMsgTemlate {
	
	@Autowired
	private CommonService commonService;

	
	public String path= "http://www.ruitwj.com";
	
//	/**
//	 * 睿通万家物业，新报修通知模板
//	 * @param ob
//	 * @param params
//	 * @return
//	 */
//	public JsonObject repairTemplate(JsonObject ob,Integer nodeId,String repairId){
//		//睿通万家物业，新报修通知模板id：ZI4ePb3jXp4lXk6AgNEkssRO-SvERWP6aJctmn7viyY
//		RepairApply repairApply = commonService.load(RepairApply.class, repairId);
//		if(repairApply.getCusId()!=null){
//			//Customer customer = commonService.load(Customer.class, repairApply.getCusId());
//			HomeCustomer customer = customerService.selectHomeCustomerByCusId(repairApply.getCusId());
//			if(customer!=null){
//				repairTempl(ob, repairApply, customer, nodeId);
//			}
//		}else{
//			repairTempl(ob, repairApply, nodeId);
//		}
//		return ob;
//	}
//	/***
//	 * 抢单模板
//	 * @param ob
//	 * @param nodeId
//	 * @param repairId
//	 * @return
//	 */
//	public JsonObject repairRobTemplate(JsonObject ob,String repairId){
//		//睿通万家物业，(抢单)报修通知模板id：cOX3C5B4ob-QvVhva7lg3GXiPo-eQLWZNoA_-LgdMME
//		RepairApply repairApply = commonService.load(RepairApply.class, repairId);
//		if(repairApply.getCusId()!=null){
//			//Customer customer = commonService.load(Customer.class, repairApply.getCusId());
//			HomeCustomer customer = customerService.selectHomeCustomerByCusId(repairApply.getCusId());
//			if(customer!=null){
//				repairRobTempl(ob, repairApply, customer);
//			}
//		}else{
//			repairRobTempl(ob, repairApply, null);
//		}
//		return ob;
//	}
//	
//	/**
//	 * app发起报事报修推送模板
//	 * @param ob
//	 * @param repairApply
//	 * @param customer
//	 * @param nodeId
//	 */
//	public void repairTempl(JsonObject ob,RepairApply repairApply,HomeCustomer customer,Integer nodeId){
//		String ruitwjPath=ConfigTool.getString("ruitwj.path");
//		String url = null;
//		if(ruitwjPath !=null){
//			url = ruitwjPath+"/weixin/process/node-view?nodeId="+nodeId+"&render=handling";
//		}else{
//			url = path+"/weixin/process/node-view?nodeId="+nodeId+"&render=handling";
//		}
//		ob.addProperty("template_id", "ZI4ePb3jXp4lXk6AgNEkssRO-SvERWP6aJctmn7viyY");
//		ob.addProperty("url", url);
//		JsonObject data = new JsonObject();
//		//标题
//		JsonObject first = new JsonObject();
//		first.addProperty("value", "您有新的报修订单待处理");
//		first.addProperty("color", "#000");
//		data.add("first", first);
//		//报修人
//		JsonObject keyword1 = new JsonObject();
//		keyword1.addProperty("value", customer.getCusName());
//		keyword1.addProperty("color", "#000");
//		data.add("keyword1", keyword1);
//		//电话
//		JsonObject keyword2 = new JsonObject();
//		keyword2.addProperty("value",customer.getCusPhone());
//		keyword2.addProperty("color", "#000");
//		data.add("keyword2", keyword2);
//		//房号
//		JsonObject keyword3 = new JsonObject();
//		keyword3.addProperty("value", repairApply.getCommunityName()+repairApply.getNodeFullVal());
//		keyword3.addProperty("color", "#000");
//		data.add("keyword3", keyword3);
//		//报修内容
//		JsonObject keyword4 = new JsonObject();
//		keyword4.addProperty("value", repairApply.getRepairDiscription());
//		keyword4.addProperty("color", "#000");
//		data.add("keyword4", keyword4);
//		//报修时间
//		JsonObject keyword5 = new JsonObject();
//		keyword5.addProperty("value", DateTimeUtil.format(repairApply.getRepairTime(), "yyyy年MM月dd HH:mm"));
//		keyword5.addProperty("color", "#000");
//		data.add("keyword5", keyword5);
//		//报修备注
//		JsonObject remark = new JsonObject();
//		remark.addProperty("value", "点击查看详情");
//		remark.addProperty("color", "#000");
//		data.add("remark", remark);
//		ob.add("data", data);
//	}
//	/**
//	 * 抢单模板
//	 * @param ob
//	 * @param repairApply
//	 * @param customer
//	 * @param nodeId
//	 */
//	public void repairRobTempl(JsonObject ob,RepairApply repairApply,HomeCustomer customer){
//		String ruitwjPath=ConfigTool.getString("ruitwj.path");
//		String url = null;
//		if(ruitwjPath !=null){
//			url = ruitwjPath+"/weixin/company/repair-base?repairId="+repairApply.getRepairId();
//		}else{
//			url = path+"/weixin/company/repair-base?repairId="+repairApply.getRepairId();
//		}
//		ob.addProperty("template_id", "cOX3C5B4ob-QvVhva7lg3GXiPo-eQLWZNoA_-LgdMME");
//		ob.addProperty("url", url);
//		JsonObject data = new JsonObject();
//		//标题
//		JsonObject first = new JsonObject();
//		first.addProperty("value", "你好，新的报事报修,请确认");
//		first.addProperty("color", "#000");
//		data.add("first", first);
//		//地址
//		JsonObject keyword1 = new JsonObject();
//		keyword1.addProperty("value", repairApply.getCommunityName()+repairApply.getNodeFullVal());
//		keyword1.addProperty("color", "#000");
//		data.add("keyword1", keyword1);
//		//姓名
//		JsonObject keyword2 = new JsonObject();
//		String cusName="物业人员";
//		if(customer!=null)cusName=customer.getCusName();
//		keyword2.addProperty("value",cusName);
//		keyword2.addProperty("color", "#000");
//		data.add("keyword2", keyword2);
//		//工单类型
//		JsonObject keyword3 = new JsonObject();
//		keyword3.addProperty("value", repairApply.getRepairType().title());
//		keyword3.addProperty("color", "#000");
//		data.add("keyword3", keyword3);
//		
//		//工单编号
//		JsonObject keyword4 = new JsonObject();
//		keyword4.addProperty("value", repairApply.getRepairId());
//		keyword4.addProperty("color", "#000");
//		data.add("keyword4", keyword4);
//		
//		//具体内容
//		JsonObject keyword5 = new JsonObject();
//		keyword5.addProperty("value", repairApply.getRepairDiscription());
//		keyword5.addProperty("color", "#000");
//		data.add("keyword5", keyword5);
//		
//		//商品信息
//		JsonObject remark = new JsonObject();
//		remark.addProperty("value", "请尽快确认");
//		remark.addProperty("color", "#000");
//		data.add("remark", remark);
//		
//		ob.add("data", data);
//		
//	}
//	/***
//	 * 派件模板
//	 * @param ob
//	 * @param payRecordId
//	 * @param cusId
//	 * @param nodeId
//	 * @param remark
//	 */
//	public void deliveryTempl(JsonObject ob, String payRecordId,
//			String cusId,String addressDetails,String details){
//		HomeCustomer customer=customerService.selectHomeCustomerByCusId(cusId);
//		String ruitwjPath=ConfigTool.getString("ruitwj.path");
//		String url = null;
//		if(ruitwjPath !=null){
//			url = ruitwjPath+"/shop/delivery/delivery-view?payRecordId="+payRecordId;
//		}else{
//			url = path+"/shop/delivery/delivery-view?payRecordId="+payRecordId;
//		}
//		ob.addProperty("template_id", "_2Bmh0gbLILZivLimGJCPIMp6-MrUjH9Fegb57vh4ZQ");
//		ob.addProperty("url", url);
//		JsonObject data = new JsonObject();
//		//标题
//		JsonObject first = new JsonObject();
//		first.addProperty("value", "你好，您有新的派单");
//		first.addProperty("color", "#000");
//		data.add("first", first);
//		//收件人
//		JsonObject keyword1 = new JsonObject();
//		keyword1.addProperty("value", customer.getCusName());
//		keyword1.addProperty("color", "#000");
//		data.add("keyword1", keyword1);
//		//电话
//		JsonObject keyword2 = new JsonObject();
//		keyword2.addProperty("value",customer.getCusPhone());
//		keyword2.addProperty("color", "#000");
//		data.add("keyword2", keyword2);
//		//地址
//		JsonObject keyword3 = new JsonObject();
//		keyword3.addProperty("value", addressDetails);
//		keyword3.addProperty("color", "#000");
//		data.add("keyword3", keyword3);
//		
//		//时间
//		JsonObject keyword4 = new JsonObject();
//		keyword4.addProperty("value", DateTimeUtil.format(new Date(), "yyyy年MM月dd HH:mm"));
//		keyword4.addProperty("color", "#000");
//		data.add("keyword4", keyword4);
//		
//		//服务
//		JsonObject keyword5 = new JsonObject();
//		keyword5.addProperty("value", "派件");
//		keyword5.addProperty("color", "#000");
//		data.add("keyword5", keyword5);
//		
//		//商品信息
//		JsonObject remark = new JsonObject();
//		remark.addProperty("value", details);
//		remark.addProperty("color", "#000");
//		data.add("remark", remark);
//		ob.add("data", data);
//	
//	}
//	/**
//	 * 微信发起报事报修推送模板
//	 * @param ob
//	 * @param repairApply
//	 * @param nodeId
//	 */
//	public void repairTempl(JsonObject ob,RepairApply repairApply,Integer nodeId){
//		String ruitwjPath=ConfigTool.getString("ruitwj.path");
//		String url = null; 
//		if(ruitwjPath !=null){
//			url = ruitwjPath+"/weixin/process/node-view?nodeId="+nodeId+"&render=handling";
//		}else{
//			url = path+"/weixin/process/node-view?nodeId="+nodeId+"&render=handling";
//		}
//		ob.addProperty("template_id", "np-P7uaia4Ie3OuqYrW2amN3-TL-PhIEfn_OzfkVVIM");
//		ob.addProperty("url", url);
//		JsonObject data = new JsonObject();
//		//标题
//		JsonObject first = new JsonObject();
//		first.addProperty("value", "您有新的报修订单待处理");
//		first.addProperty("color", "#000");
//		data.add("first", first);
//		//报修人
//		JsonObject keyword1 = new JsonObject();
//		keyword1.addProperty("value", repairApply.getCommunityName());
//		keyword1.addProperty("color", "#000");
//		data.add("keyword1", keyword1);
//		//电话
//		JsonObject keyword2 = new JsonObject();
//		if(repairApply.getNodeId()!=null){
//			keyword2.addProperty("value",repairApply.getNodeFullVal());
//		}else{
//			keyword2.addProperty("value","公共区域");
//		}
//		keyword2.addProperty("color", "#000");
//		data.add("keyword2", keyword2);
//		//房号
//		JsonObject keyword3 = new JsonObject();
//		keyword3.addProperty("value", repairApply.getRepairDiscription());
//		keyword3.addProperty("color", "#000");
//		data.add("keyword3", keyword3);
//		//报修内容
//		JsonObject keyword4 = new JsonObject();
//		keyword4.addProperty("value", DateTimeUtil.format(repairApply.getRepairTime(), "yyyy年MM月dd HH:mm"));
//		keyword4.addProperty("color", "#000");
//		data.add("keyword4", keyword4);
//		//报修备注
//		JsonObject remark = new JsonObject();
//		remark.addProperty("value", "点击查看详情");
//		remark.addProperty("color", "#000");
//		data.add("remark", remark);
//		ob.add("data", data);
//	}
//	
//	/**
//	 * 投诉建议模板
//	 * @param ob
//	 * @param complainId
//	 * @return
//	 */
//	public JsonObject complainTemplate(JsonObject ob,Complain complain){
//		//投诉模板id：NkvB-c_iwJJJE_xdvYyKfFgJ2TR0VPqscyW1D60dYm4
//		//Complain complain = commonService.load(Complain.class, complainId);
//		Customer customer = commonService.load(Customer.class, complain.getCusId());
//		String url = path+"/weixin/company/feed-back-info?complainId="+complain.getComplainId()+"&handleState=HANDLE";
//		
//		ob.addProperty("template_id", "NkvB-c_iwJJJE_xdvYyKfFgJ2TR0VPqscyW1D60dYm4");
//		ob.addProperty("url", url);
//		JsonObject data = new JsonObject();
//		//标题
//		JsonObject first = new JsonObject();
//		first.addProperty("value", "您有新的投诉通知待处理");
//		first.addProperty("color", "#000");
//		data.add("first", first);
//		//投诉人
//		JsonObject keyword1 = new JsonObject();
//		keyword1.addProperty("value", customer.getCusName());
//		keyword1.addProperty("color", "#000");
//		data.add("keyword1", keyword1);
//		//电话
//		JsonObject keyword2 = new JsonObject();
//		keyword2.addProperty("value",customer.getCusPhone());
//		keyword2.addProperty("color", "#000");
//		data.add("keyword2", keyword2);
//		//房号
//		JsonObject keyword3 = new JsonObject();
//		keyword3.addProperty("value", complain.getCommunityName());
//		keyword3.addProperty("color", "#000");
//		data.add("keyword3", keyword3);
//		//投诉内容
//		JsonObject keyword4 = new JsonObject();
//		keyword4.addProperty("value", complain.getComplainComment());
//		keyword4.addProperty("color", "#000");
//		data.add("keyword4", keyword4);
//		//投诉时间
//		JsonObject keyword5 = new JsonObject();
//		keyword5.addProperty("value", DateTimeUtil.format(complain.getComplainStartTime(), "yyyy年MM月dd HH:mm"));
//		keyword5.addProperty("color", "#000");
//		data.add("keyword5", keyword5);
//		//报修备注
//		JsonObject remark = new JsonObject();
//		remark.addProperty("value", "点击查看详情");
//		remark.addProperty("color", "#000");
//		data.add("remark", remark);
//		ob.add("data", data);
//		return ob;
//	}
//	
//	
//	public JsonObject adviseTemplate(JsonObject ob,Complain complain){
//		//建议模板id：YKIgU9nLyDMZSD9XC2N42o3FcF39W_MhEIVN04gCDQk
//		Customer customer = commonService.load(Customer.class, complain.getCusId());
//		String url = path+"/weixin/company/feed-back-info?complainId="+complain.getComplainId()+"&handleState=HANDLE";
//		
//		ob.addProperty("template_id", "YKIgU9nLyDMZSD9XC2N42o3FcF39W_MhEIVN04gCDQk");
//		ob.addProperty("url", url);
//		JsonObject data = new JsonObject();
//		//标题
//		JsonObject first = new JsonObject();
//		first.addProperty("value", "您有新的反馈内容");
//		first.addProperty("color", "#000");
//		data.add("first", first);
//		//建议人
//		JsonObject keyword1 = new JsonObject();
//		keyword1.addProperty("value", customer.getCusName());
//		keyword1.addProperty("color", "#000");
//		data.add("keyword1", keyword1);
//		//小区
//		JsonObject keyword2 = new JsonObject();
//		keyword2.addProperty("value",complain.getCommunityName());
//		keyword2.addProperty("color", "#000");
//		data.add("keyword2", keyword2);
//		//内容
//		JsonObject keyword3 = new JsonObject();
//		keyword3.addProperty("value", complain.getComplainComment());
//		keyword3.addProperty("color", "#000");
//		data.add("keyword3", keyword3);
//		//建议备注
//		JsonObject remark = new JsonObject();
//		remark.addProperty("value", "点击查看详情");
//		remark.addProperty("color", "#000");
//		data.add("remark", remark);
//		ob.add("data", data);
//		return ob;
//	}
//	/**
//	 * 客户评价推送
//	 * @param ob
//	 * @param complain
//	 * @param yesNo
//	 * @return
//	 */
//	public JsonObject appraiseTemplate(JsonObject ob,Complain complain,String details){
//		//投诉模板id：NkvB-c_iwJJJE_xdvYyKfFgJ2TR0VPqscyW1D60dYm4
//		//Complain complain = commonService.load(Complain.class, complainId);
//		String url = path+"/weixin/company/feed-back-info?complainId="+complain.getComplainId()+"&handleState=HANDLE";
//		
//		ob.addProperty("template_id", "zTH7JL4dyaVRVIlOpQii-1ZBadF2-ob49cPYxTjyvhA");
//		ob.addProperty("url", url);
//		JsonObject data = new JsonObject();
//		//标题
//		JsonObject first = new JsonObject();
//		first.addProperty("value", "客户评价回复");
//		first.addProperty("color", "#000");
//		data.add("first", first);
//		//服务类型
//		JsonObject keyword1 = new JsonObject();
//		keyword1.addProperty("value",complain.getComplainType().title());
//		keyword1.addProperty("color", "#000");
//		data.add("keyword1", keyword1);
//		//服务完成时间
//		JsonObject keyword2 = new JsonObject();
//		keyword2.addProperty("value", DateTimeUtil.format(complain.getComplainStartTime(), "yyyy年MM月dd HH:mm"));
//		keyword2.addProperty("color", "#000");
//		data.add("keyword2", keyword2);
//		//报修备注
//		JsonObject remark = new JsonObject();
//		remark.addProperty("value", details);
//		remark.addProperty("color", "#000");
//		data.add("remark", remark);
//		ob.add("data", data);
//		return ob;
//	
//		
//	}
//	
}
